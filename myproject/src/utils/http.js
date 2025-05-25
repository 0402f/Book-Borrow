// api/http.js
import { useUserStore } from "../store/user";

// 配置基础URL
const baseURL = "/api";
// const baseURL = "http://localhost:8080";
const timeout = 10000;

// 创建请求方法
const http = {
  /**
   * 发起网络请求的核心方法
   * @param {Object} options - 请求配置对象
   * @param {string} options.url - 请求的相对路径
   * @param {string} [options.method='GET'] - 请求方法，如 'GET', 'POST', 'PUT', 'DELETE'
   * @param {Object} [options.data={}] - 请求数据
   * @param {Object} [options.header] - 请求头
   * @returns {Promise} - 返回一个 Promise 对象，用于处理请求结果
   */
  request(options) {
    // 返回一个 Promise 对象，用于处理异步请求
    return new Promise((resolve, reject) => {
      // 显示加载提示，告知用户正在进行网络请求
      uni.showLoading({
        title: "加载中",
        mask: true,
      });

      // 获取用户信息存储实例，用于获取用户的 token
      const userStore = useUserStore();
      // 获取请求头，如果 options 中没有提供，则使用空对象
      const header = options.header || {};
      // 设置请求头的 Content-Type，通常用于指定请求数据的格式

      // 判断用户是否有 token，如果有则添加到请求头中
      if (userStore.token) {
        header.JWT = userStore.token;
      }

      // 发起 uni-app 的网络请求
      uni.request({
        // 拼接完整的请求 URL，使用基础 URL 和传入的相对路径
        url: baseURL + options.url,
        // 如果 options 中没有指定请求方法，则默认使用 GET
        method: options.method || "GET",
        // 如果 options 中没有指定请求数据，则默认使用空对象
        data: options.data || {},
        // 请求头，包含可能的 token
        header,
        // 请求超时时间
        timeout,
        // 请求成功的回调函数
        success: (res) => {
          // 隐藏加载提示，请求成功后不再需要显示加载状态
          uni.hideLoading();

          // 处理401未授权的情况
          if (res.statusCode === 401) {
            const userStore = useUserStore();
            userStore.removeToken();
            uni.reLaunch({
              url: "/pages/login/login",
            });
            reject(res.data);
            return;
          }

          // 处理业务成功的情况，当返回数据的 code 为 1 时表示业务成功
          if (res.data.code === 1) {
            // 解析请求结果，将结果传递给 Promise 的 resolve 方法
            resolve(res);
          } else {
            // 处理业务失败的情况，显示统一的错误提示
            uni.showToast({
              // 如果返回数据中有错误信息，则显示该信息，否则显示默认提示
              title: res.data.msg || "服务异常",
              icon: "error",
            });
            // 将错误信息传递给 Promise 的 reject 方法
            reject(res.data);
          }
        },
        // 请求失败的回调函数
        fail: (err) => {
          // 隐藏加载提示，请求失败后不再需要显示加载状态
          uni.hideLoading();

          // 处理 401 错误，通常表示用户未授权
          if (err.statusCode === 401) {
            // 获取用户信息存储实例，清除用户的 token
            const userStore = useUserStore();
            userStore.removeToken();
            // 重新启动到登录页面，清除所有页面堆栈
            uni.reLaunch({
              url: "/pages/login/login",
            });
          } else {
            // 处理其他网络请求失败的情况，显示统一的错误提示
            uni.showToast({
              title: "网络请求失败",
              icon: "error",
            });
          }
          // 将错误信息传递给 Promise 的 reject 方法
          reject(err);
        },
      });
    });
  },

  get(url, data) {
    return this.request({
      url,
      method: "GET",
      data,
    });
  },

  post(url, data) {
    return this.request({
      url,
      method: "POST",
      data,
    });
  },

  put(url, data) {
    return this.request({
      url,
      method: "PUT",
      data,
    });
  },

  delete(url, data) {
    return this.request({
      url,
      method: "DELETE",
      data,
    });
  },
};

export default http;
export { baseURL };
