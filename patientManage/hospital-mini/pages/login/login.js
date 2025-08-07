const app = getApp();

Page({
  data: {
    phone: 'admin',
    password: '123456',
    code: '',
    loginType: 'password', // 'code' 或 'password'
    userRole: 'nurse', // 'nurse' 或 'patient'
    agreed: true,
    canLogin: true,
    codeSent: false,
    countdown: 60
  },

  onLoad() {
    // 检查是否已登录
    const token = wx.getStorageSync('token');
    if (token) {
      this.redirectToHome();
    }
  },

  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
    this.checkCanLogin();
  },

  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
    this.checkCanLogin();
  },

  onCodeInput(e) {
    this.setData({
      code: e.detail.value
    });
    this.checkCanLogin();
  },

  onSwitchLoginType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      loginType: type,
      password: '',
      code: ''
    });
    this.checkCanLogin();
  },

  onSelectRole(e) {
    const role = e.currentTarget.dataset.role;
    this.setData({
      userRole: role
    });
  },

  onToggleAgreement() {
    this.setData({
      agreed: !this.data.agreed
    });
    this.checkCanLogin();
  },

  checkCanLogin() {
    const { phone, password, code, loginType, agreed } = this.data;
    let canLogin = false;

    if (phone && agreed) {
      if (loginType === 'password' && password) {
        canLogin = true;
      } else if (loginType === 'code' && code) {
        canLogin = true;
      }
    }

    this.setData({
      canLogin: canLogin
    });
  },

  onSendCode() {
    const { phone } = this.data;
    
    if (!phone) {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return;
    }

    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '手机号码格式不正确',
        icon: 'none'
      });
      return;
    }

    // 模拟发送验证码
    wx.showLoading({
      title: '发送中...'
    });

    setTimeout(() => {
      wx.hideLoading();
      this.setData({
        codeSent: true
      });
      this.startCountdown();
      
      wx.showToast({
        title: '验证码已发送',
        icon: 'success'
      });
    }, 1000);
  },

  startCountdown() {
    const timer = setInterval(() => {
      if (this.data.countdown > 1) {
        this.setData({
          countdown: this.data.countdown - 1
        });
      } else {
        clearInterval(timer);
        this.setData({
          codeSent: false,
          countdown: 60
        });
      }
    }, 1000);
  },

  onLogin() {
    const { phone, password, code, loginType, userRole, agreed } = this.data;

    if (!agreed) {
      wx.showToast({
        title: '请先同意用户协议',
        icon: 'none'
      });
      return;
    }

    if (!this.data.canLogin) {
      wx.showToast({
        title: '请完善登录信息',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '登录中...'
    });

    wx.request({
      url: 'http://localhost:8080/api/auth/login',
      method: 'POST',
    
      data: {
        username: phone,
        password: password
      },
      success: (res) => {
        console.log(res);
        if (res.data.code === 200) {
          
          const data = res.data.data;
          const userInfo = {
            name: data.username,
            id: data.id,
            phone: data.phone,
            realName: data.realName
          }
          wx.setStorageSync('token', data.token);
          wx.setStorageSync('userInfo', userInfo);
          app.globalData.userInfo = userInfo;
          app.globalData.isNurse = true;
          wx.hideLoading();
          this.redirectToHome();
        } else {
          wx.showToast({
            title: res.data.message,
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    });
  },

  onWechatLogin() {
    wx.showToast({
      title: '微信登录功能开发中',
      icon: 'none'
    });
  },

  onViewAgreement() {
    wx.showModal({
      title: '用户协议',
      content: '这里是用户协议的内容...',
      showCancel: false
    });
  },

  onViewPrivacy() {
    wx.showModal({
      title: '隐私政策',
      content: '这里是隐私政策的内容...',
      showCancel: false
    });
  },

  redirectToHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
}); 