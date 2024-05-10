const API_URL = "http://localhost:8080/api";

export const getEvents = async () => {
  const response = await fetch(`${API_URL}/events`);
  return await response.json();
};
