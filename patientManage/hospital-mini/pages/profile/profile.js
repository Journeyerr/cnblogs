const app = getApp();

Page({
  data: {
    userInfo: {},
    stats: {
      diaryCount: 0,
      followCount: 0,
      fansCount: 0,
      points: 0
    },
    settings: {
      notifications: true
    }
  },

  onLoad() {
    this.loadUserInfo();
    this.loadStats();
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    console.log(userInfo)
    if (userInfo) {
      this.setData({ userInfo });
    } else {
      wx.redirectTo({
        url: '/pages/login/login'
      });
    }
  },

  loadStats() {
    // 模拟统计数据
    const stats = {
      diaryCount: 12,
      followCount: 8,
      fansCount: 15,
      points: 1280
    };
    
    this.setData({ stats });
  },

  onEditProfile() {
    wx.showModal({
      title: '编辑资料',
      content: '请输入新的姓名',
      editable: true,
      placeholderText: this.data.userInfo.name,
      success: (res) => {
        if (res.confirm && res.content) {
          const userInfo = { ...this.data.userInfo, name: res.content };
          this.setData({ userInfo });
          wx.setStorageSync('userInfo', userInfo);
          
          wx.showToast({
            title: '修改成功',
            icon: 'success'
          });
        }
      }
    });
  },

  onMenuTap(e) {
    const type = e.currentTarget.dataset.type;
    
    switch (type) {
      case 'collections':
        wx.showToast({
          title: '我的收藏',
          icon: 'none'
        });
        break;
      case 'trials':
        wx.showToast({
          title: '临床试验申请',
          icon: 'none'
        });
        break;
      case 'reports':
        wx.showToast({
          title: '诊疗报告',
          icon: 'none'
        });
        break;
      case 'doctors':
        wx.showToast({
          title: '我的医生',
          icon: 'none'
        });
        break;
      case 'consultations':
        wx.showToast({
          title: '我的会诊',
          icon: 'none'
        });
        break;
      case 'feedback':
        this.showFeedbackModal();
        break;
      case 'address':
        wx.showToast({
          title: '收货地址',
          icon: 'none'
        });
        break;
      case 'notifications':
        break;
      case 'privacy':
        wx.showToast({
          title: '隐私设置',
          icon: 'none'
        });
        break;
      case 'about':
        this.showAboutModal();
        break;
    }
  },

  showFeedbackModal() {
    wx.showModal({
      title: '留言反馈',
      content: '请输入您的反馈意见',
      editable: true,
      placeholderText: '请输入反馈内容...',
      success: (res) => {
        if (res.confirm && res.content) {
          wx.showToast({
            title: '反馈已提交',
            icon: 'success'
          });
        }
      }
    });
  },

  showAboutModal() {
    wx.showModal({
      title: '关于我们',
      content: '全病程管理 v1.0.0 @AnYuan ',
      showCancel: false
    });
  },

  onNotificationChange(e) {
    const notifications = e.detail.value;
    this.setData({
      'settings.notifications': notifications
    });
    
    wx.showToast({
      title: notifications ? '已开启通知' : '已关闭通知',
      icon: 'success'
    });
  },

  onLogout() {
    const token = wx.getStorageSync('token')
    if (!token) {
      app.clearToken();
      return;
    }

    wx.showModal({
      title: '退出登录',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 调用登出接口
          app.post('/api/auth/logout')
            .then(res => {
              console.log(res);
              app.clearLoginInfo();
              wx.redirectTo({
                url: '/pages/login/login'
              });
          });
        }
      }
    });
  }
}); 