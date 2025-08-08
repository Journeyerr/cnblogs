const  baseUrl = 'http://localhost:8080';

export const postRequest = (url, data) => {
  return new Promise((resolve, reject) => {
    // 获取本地存储的token（若有）
    const token = wx.getStorageSync('token') || '';
    wx.request({
      url: baseUrl + url,
      method: 'POST',
      data: JSON.stringify(data), // 序列化数据
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          handleError(res.data.code, res.data.message);
          resolve(res.data);
        } else {
          // 统一错误处理
          handleHttpError(res.statusCode);
          reject(res.data);
        }
      },
      fail: (err) => {
        wx.showToast({ title: '网络异常', icon: 'error' }); 
        reject(err);
      }
    });
  });
};


export const getRequest = (url, data) => {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token') || '';
    wx.request({
      url: baseUrl + url,
      method: "GET",
      data: data,
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          handleError(res.code, res.message);
          resolve(res.data)
        } else {
          handleHttpError(res.statusCode)
          reject(res.data)
        }
      },
      fail: (err) => {
        wx.showToast({ title: '网络异常', icon: 'error' })
        reject(err)
      }
    })
  })
}

// 错误处理函数
function handleHttpError(code) {
  switch(code) {
    case 401:
      wx.navigateTo({ url: '/pages/login/login' }); 
      break;
    case 404:
      wx.showToast({ title: '接口不存在',icon: 'error' });
      break;
    case 403:
    // 清除登录信息
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    wx.redirectTo({
      url: '/pages/login/login'
    });
    break;  
    default:
      wx.showToast({ title: '服务异常' });
  }
}

// 错误处理函数
function handleError(code, message) {
  switch(code) {
    case 500:
      wx.showToast({ title: message, icon: "error" });
      throw new Error(message);
  }

}

