document.addEventListener('DOMContentLoaded', function () {
    const chatPanel = document.getElementById('chat-panel');
    const chatMessages = document.getElementById('chat-messages');
    const chatEmpty = document.getElementById('chat-empty');
    const chatForm = document.getElementById('chat-form');
    const chatInput = document.getElementById('chat-input');
    const chatSendBtn = document.getElementById('chat-send-btn');
    const unreadBadge = document.getElementById('chat-unread-badge');
    const statusDot = document.getElementById('chat-status-dot');

    // 비로그인 사용자는 채팅 위젯 자체가 렌더링되지 않으므로 여기서 종료
    if (!chatForm) return;

    const currentUser = document.body.getAttribute('data-current-user') || '';

    let stompClient = null;
    let isPanelOpen = false;
    let unreadCount = 0;
    let isConnected = false;

    // ───────── 패널 열기/닫기 ─────────
    window.toggleChat = function () {
        isPanelOpen = !isPanelOpen;
        chatPanel.classList.toggle('open', isPanelOpen);
        if (isPanelOpen) {
            unreadCount = 0;
            updateUnreadBadge();
            scrollToBottom();
            chatInput.focus();
            if (!isConnected) connect();
        }
    };

    // ───────── 메시지 렌더링 ─────────
    function appendMessage(msg) {
        if (chatEmpty) chatEmpty.style.display = 'none';

        const isMine = currentUser && msg.username === currentUser;

        const wrap = document.createElement('div');
        wrap.className = 'chat-msg ' + (isMine ? 'is-mine' : 'is-other');

        const authorEl = document.createElement('div');
        authorEl.className = 'chat-msg-author';
        authorEl.textContent = isMine ? '나' : msg.username;
        wrap.appendChild(authorEl);

        const row = document.createElement('div');
        row.className = 'chat-bubble-row';

        const bubble = document.createElement('div');
        bubble.className = 'chat-bubble';
        bubble.textContent = msg.content;

        const time = document.createElement('div');
        time.className = 'chat-msg-time';
        time.textContent = msg.time;

        row.appendChild(bubble);
        row.appendChild(time);
        wrap.appendChild(row);

        chatMessages.appendChild(wrap);
        scrollToBottom();

        if (!isPanelOpen && !isMine) {
            unreadCount++;
            updateUnreadBadge();
        }
    }

    function scrollToBottom() {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function updateUnreadBadge() {
        if (unreadCount > 0) {
            unreadBadge.style.display = 'flex';
            unreadBadge.textContent = unreadCount > 99 ? '99+' : unreadCount;
        } else {
            unreadBadge.style.display = 'none';
        }
    }

    // ───────── 이전 대화 내역 로드 ─────────
    function loadHistory() {
        fetch('/api/chat/history?limit=50')
            .then(res => res.json())
            .then(list => {
                list.forEach(appendMessage);
            })
            .catch(err => console.error('채팅 내역 로드 실패', err));
    }

    // ───────── WebSocket(STOMP) 연결 ─────────
    function connect() {
        const socket = new SockJS('/ws/chat');
        stompClient = new StompJs.Client({
            webSocketFactory: () => socket,
            reconnectDelay: 3000,
            onConnect: () => {
                isConnected = true;
                if (statusDot) statusDot.style.background = '#9FE1CB';
                stompClient.subscribe('/topic/chat', (frame) => {
                    const body = JSON.parse(frame.body);
                    appendMessage(body);
                });
            },
            onDisconnect: () => {
                isConnected = false;
            },
            onStompError: (frame) => {
                console.error('STOMP 오류', frame);
            },
        });
        stompClient.activate();
        loadHistory();
    }

    // ───────── 메시지 전송 ─────────
    function sendMessage() {
        const content = chatInput.value.trim();
        if (!content || !stompClient || !isConnected) return;

        stompClient.publish({
            destination: '/app/chat/send',
            body: JSON.stringify({ content }),
        });

        chatInput.value = '';
    }

    chatForm.addEventListener('submit', function (e) {
        e.preventDefault();
        sendMessage();
    });

    chatInput.addEventListener('keydown', function (e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });
});