import http from "../utils/http";

// 登录接口
export const login = (data) => {
  return http.post("/user/login", data);
};

// 获取用户信息
export const getUserInfo = () => {
  return http.get("/user/info");
};

// 用户登出
export const logout = () => {
  return http.post("/user/logout");
};
// 获取当前登录用户的信息
export const getCurrentUser = () => {
  return http.get("/user/userInfo");
};
