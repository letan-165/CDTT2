const APIGATEWAY = "http://localhost:8888";

const fetchAPI = async (url, method = "GET", data = null) => {
  const options = {
    method,
    headers: {
      'Content-Type': 'application/json'
    }
  };

  if (data) {
    options.body = JSON.stringify(data);
  }

  try {
    const response = await fetch(`${APIGATEWAY}${url}`, options);

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || JSON.stringify(errorData));
    }
    return await response.json();
    
  } catch (error) {
    console.error("Lỗi khi gọi API:", error);
    throw error;
  }
};

export default fetchAPI;
