const BASE_URL = 'http://localhost:8080';

export const fetchChatMessages = async () => {
  const res = await fetch(`${BASE_URL}/api/chat`);
  if (!res.ok) {
    throw new Error('채팅 목록 조회 실패');
  }
  return res.json();
};
