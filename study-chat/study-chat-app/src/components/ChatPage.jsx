import { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const ChatPage = () => {
  const [messages, setMessages] = useState([]);
  const [inputMessage, setInputMessage] = useState('');
  const [selectedUser, setSelectedUser] = useState(1);
  const [isConnected, setIsConnected] = useState(false);
  const messagesEndRef = useRef(null);
  const stompClientRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // API에서 초기 메시지 불러오기
  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/chat');
        if (!response.ok) {
          throw new Error('메시지를 불러올 수 없습니다');
        }
        const data = await response.json();
        setMessages(data);
      } catch (error) {
        console.error('메시지 로드 실패:', error);
        setMessages([
          {
            pk: 1,
            userId: 1,
            message: 'Error!',
            dateTime: new Date().toISOString()
          },
        ]);
      }
    };

    fetchMessages();
  }, []);

  // WebSocket 연결
  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        console.log('WebSocket 연결 성공');
        setIsConnected(true);

        // 메시지 구독
        client.subscribe('/sub/chat', (message) => {
          try {
            const newMessage = JSON.parse(message.body);
            setMessages((prev) => [...prev, newMessage]);
          } catch (err) {
            console.error('메시지 파싱 오류:', err);
          }
        });
      },
      onStompError: (frame) => {
        console.error('STOMP 에러:', frame);
        setIsConnected(false);
      },
      onWebSocketError: (error) => {
        console.error('WebSocket 에러:', error);
        setIsConnected(false);
      },
      debug: (str) => {
        // 디버그 로그 (필요시 주석 해제)
        // console.log(str);
      }
    });

    client.activate();
    stompClientRef.current = client;

    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, []);

  const handleSendMessage = () => {
    if (!inputMessage.trim()) return;

    const messageData = {
      message: inputMessage,
      userId: selectedUser,
    };

    setInputMessage('');

    // WebSocket으로 전송
    if (stompClientRef.current?.connected && isConnected) {
      try {
        stompClientRef.current.publish({
          destination: '/pub/send',
          body: JSON.stringify(messageData),
        });
      } catch (error) {
        console.error('메시지 전송 실패:', error);
        // 전송 실패 시에만 로컬에 추가
        const newMessage = {
          pk: Date.now(),
          userId: selectedUser,
          message: messageData.message,
          dateTime: new Date().toISOString()
        };
        setMessages((prev) => [...prev, newMessage]);
      }
    } else {
      // WebSocket 연결 안 되어 있을 때만 로컬에 추가
      const newMessage = {
        pk: Date.now(),
        userId: selectedUser,
        message: messageData.message,
        dateTime: new Date().toISOString()
      };
      setMessages((prev) => [...prev, newMessage]);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const formatDateTime = (dateTime) => {
    try {
      if (!dateTime) return '방금';
      
      const date = new Date(dateTime);
      if (isNaN(date.getTime())) return '방금';
      
      const today = new Date();
      const isToday = date.toDateString() === today.toDateString();

      if (isToday) {
        return date.toLocaleTimeString('ko-KR', {
          hour: '2-digit',
          minute: '2-digit',
        });
      }
      return date.toLocaleString('ko-KR', {
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    } catch (err) {
      return '방금';
    }
  };

  return (
    <div style={{
      minHeight: '96vh',
      minWidth: '97vw',
      background: 'linear-gradient(to bottom right, #faf5ff, #fce7f3, #eff6ff)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '1rem'
    }}>
      <div style={{ width: '100%', maxWidth: '64rem', margin: '0 auto' }}>
        <div style={{
          background: 'white',
          borderRadius: '1.5rem',
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
          overflow: 'hidden',
          height: '88vh'
        }}>
          {/* 헤더 */}
          <div style={{
            background: 'linear-gradient(to right, #c084fc, #f9a8d4)',
            padding: '1.25rem',
            paddingTop: '1.5rem',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between'
          }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
              <div style={{
                width: '2.5rem',
                height: '2.5rem',
                background: 'white',
                borderRadius: '50%',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
              }}>
                <span style={{ fontSize: '1.5rem' }}>💬</span>
              </div>
              <div>
                <h1 style={{ color: 'white', fontWeight: 'bold', fontSize: '1.25rem', margin: 0 }}>
                  채팅방
                </h1>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.25rem', marginTop: '0.25rem' }}>
                  <div style={{
                    width: '0.5rem',
                    height: '0.5rem',
                    borderRadius: '50%',
                    background: isConnected ? '#86efac' : '#fca5a5'
                  }}></div>
                  <span style={{ color: 'white', fontSize: '0.75rem', opacity: 0.9 }}>
                    {isConnected ? '연결됨' : '연결 중...'}
                  </span>
                </div>
              </div>
            </div>
            
            {/* 사용자 선택 버튼 */}
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                onClick={() => setSelectedUser(1)}
                style={{
                  padding: '0.5rem 1rem',
                  borderRadius: '9999px',
                  fontWeight: '500',
                  border: 'none',
                  cursor: 'pointer',
                  background: selectedUser === 1 ? 'white' : 'rgba(255, 255, 255, 0.3)',
                  color: selectedUser === 1 ? '#c084fc' : 'white',
                  boxShadow: selectedUser === 1 ? '0 4px 6px -1px rgba(0, 0, 0, 0.1)' : 'none',
                  transition: 'all 0.2s'
                }}
              >
                👤 A
              </button>
              <button
                onClick={() => setSelectedUser(2)}
                style={{
                  padding: '0.5rem 1rem',
                  borderRadius: '9999px',
                  fontWeight: '500',
                  border: 'none',
                  cursor: 'pointer',
                  background: selectedUser === 2 ? 'white' : 'rgba(255, 255, 255, 0.3)',
                  color: selectedUser === 2 ? '#f9a8d4' : 'white',
                  boxShadow: selectedUser === 2 ? '0 4px 6px -1px rgba(0, 0, 0, 0.1)' : 'none',
                  transition: 'all 0.2s'
                }}
              >
                👤 B
              </button>
            </div>
          </div>

          {/* 메시지 영역 */}
          <div style={{
            overflowY: 'auto',
            padding: '1.5rem',
            height: 'calc(85vh - 180px)',
            /* 스크롤바 숨기기 */
            scrollbarWidth: 'none',        // Firefox
            msOverflowStyle: 'none',        // IE, Edge
          }}>
            {messages.map((msg) => (
              <div
                key={msg.pk}
                style={{
                  display: 'flex',
                  justifyContent: msg.userId === 1 ? 'flex-start' : 'flex-end',
                  marginBottom: '1rem'
                }}
              >
                <div style={{
                  display: 'flex',
                  alignItems: 'flex-end',
                  gap: '0.5rem',
                  maxWidth: '70%',
                  flexDirection: msg.userId === 2 ? 'row-reverse' : 'row'
                }}>
                  {/* 아바타 */}
                  <div style={{
                    width: '2rem',
                    height: '2rem',
                    borderRadius: '50%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: 'white',
                    fontWeight: 'bold',
                    flexShrink: 0,
                    background: msg.userId === 1 ? '#c084fc' : '#f9a8d4'
                  }}>
                    {msg.userId === 1 ? 'A' : 'B'}
                  </div>
                  
                  {/* 메시지 버블 */}
                  <div style={{ display: 'flex', flexDirection: 'column', gap: '0.25rem' }}>
                    <div style={{
                      padding: '0.75rem 1rem',
                      borderRadius: '1rem',
                      boxShadow: '0 1px 2px 0 rgba(0, 0, 0, 0.05)',
                      background: msg.userId === 1 ? '#f3f4f6' : 'linear-gradient(to right, #c084fc, #f9a8d4)',
                      color: msg.userId === 1 ? '#1f2937' : 'white',
                      borderTopLeftRadius: msg.userId === 1 ? '0.125rem' : '1rem',
                      borderTopRightRadius: msg.userId === 2 ? '0.125rem' : '1rem'
                    }}>
                      <p style={{ margin: 0, wordBreak: 'break-word' }}>{msg.message}</p>
                    </div>
                    <span style={{
                      fontSize: '0.75rem',
                      color: '#9ca3af',
                      padding: '0 0.5rem',
                      textAlign: msg.userId === 2 ? 'right' : 'left'
                    }}>
                      {formatDateTime(msg.dateTime)}
                    </span>
                  </div>
                </div>
              </div>
            ))}
            <div ref={messagesEndRef} />
          </div>

          {/* 입력 영역 */}
          <div style={{
            borderTop: '1px solid #e5e7eb',
            padding: '1rem',
            paddingBottom: '1rem',
            background: '#f9fafb'
          }}>
            <div style={{ display: 'flex', gap: '0.75rem' }}>
              <input
                type="text"
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
                onKeyPress={handleKeyPress}
                placeholder="메시지를 입력하세요..."
                style={{
                  flex: 1,
                  padding: '0.75rem 1.25rem',
                  borderRadius: '9999px',
                  border: '2px solid #e5e7eb',
                  outline: 'none',
                  fontSize: '1rem'
                }}
              />
              <button
                onClick={handleSendMessage}
                disabled={!inputMessage.trim()}
                style={{
                  padding: '0.75rem 1.5rem',
                  borderRadius: '9999px',
                  fontWeight: '500',
                  boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
                  transition: 'all 0.2s',
                  border: 'none',
                  cursor: inputMessage.trim() ? 'pointer' : 'not-allowed',
                  background: inputMessage.trim() 
                    ? 'linear-gradient(to right, #c084fc, #f9a8d4)' 
                    : '#d1d5db',
                  color: inputMessage.trim() ? 'white' : '#6b7280'
                }}
              >
                전송 ✨
              </button>
            </div>
            <div style={{ marginTop: '0.5rem', textAlign: 'center' }}>
              <span style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                현재 사용자: <span style={{ 
                  color: selectedUser === 1 ? '#c084fc' : '#f9a8d4',
                  fontWeight: 'bold' 
                }}>
                  {selectedUser === 1 ? 'A' : 'B'}
                </span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ChatPage;