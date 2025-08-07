App({
  globalData: {
    userInfo: null,
    isNurse: true, // true为护士，false为病人
    currentPatient: null,
    patients: [],
    todos: [],
    news: []
  },

  onLaunch() {
    // 检查登录状态
    this.checkLoginStatus();
    
    // 初始化数据
    this.initData();
  },

  clearLoginInfo: function() {
    // 清除登录信息
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    // 重置全局数据
    app.globalData.userInfo = null;
    app.globalData.isNurse = true;
    
  },
  
  checkLoginStatus: function() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    if (!token || !userInfo) {
      wx.redirectTo({
        url: '/pages/login/login'
      });
    }
  },

  initData() {
    // 模拟初始化数据
    this.globalData.patients = [
      {
        id: 1,
        name: '张三',
        age: 45,
        gender: '男',
        room: '301',
        bed: '1',
        admissionDate: '2024-01-15',
        diagnosis: '乳腺癌',
        status: '住院中',
        latestCheck: {
          temperature: 36.8,
          bloodPressure: '120/80',
          heartRate: 75,
          bloodSugar: 5.2
        }
      },
      {
        id: 2,
        name: '李四',
        age: 52,
        gender: '女',
        room: '302',
        bed: '2',
        admissionDate: '2024-01-10',
        diagnosis: '肺癌',
        status: '住院中',
        latestCheck: {
          temperature: 37.2,
          bloodPressure: '135/85',
          heartRate: 82,
          bloodSugar: 6.1
        }
      }
    ];

    this.globalData.todos = [
      {
        id: 1,
        title: '张三体温测量',
        patientId: 1,
        patientName: '张三',
        type: 'check',
        priority: 'high',
        dueTime: '14:00',
        completed: false
      },
      {
        id: 2,
        title: '李四用药提醒',
        patientId: 2,
        patientName: '李四',
        type: 'medicine',
        priority: 'medium',
        dueTime: '16:00',
        completed: false
      }
    ];

    this.globalData.news = [
      {
        id: 1,
        title: '温情三月天 暖意沁心田',
        subtitle: '——————湖南省肿瘤医院病友关爱活动',
        image: 'https://images.cnblogs.com/cnblogs_com/blogs/669579/galleries/2469411/t_250804035543_3AB25D3A-6EAA-47ea-9CFA-93AEB52A6380.png',
        reads: 1400,
        date: '2024-03-15'
      },
      {
        id: 2,
        title: '湖南省首个乳腺癌新药临床研究门诊正式开诊',
        subtitle: '为患者提供更多治疗选择',
        image: 'https://images.cnblogs.com/cnblogs_com/blogs/669579/galleries/2469411/t_250804035728_347BA919-ED18-495a-AA6F-1DAF96FA9917.png',
        reads: 4400,
        date: '2024-03-10'
      }
    ];
  },

  // 获取病人列表
  getPatients() {
    return this.globalData.patients;
  },

  // 获取待办事项
  getTodos() {
    return this.globalData.todos;
  },

  // 获取新闻资讯
  getNews() {
    return this.globalData.news;
  },

  // 添加病人反馈
  addPatientFeedback(patientId, feedback) {
    // 这里应该调用API保存到服务器
    console.log('添加病人反馈:', patientId, feedback);
  },

  // 更新病人检查指标
  updatePatientCheck(patientId, checkData) {
    const patient = this.globalData.patients.find(p => p.id === patientId);
    if (patient) {
      patient.latestCheck = { ...patient.latestCheck, ...checkData };
    }
  }
}); 