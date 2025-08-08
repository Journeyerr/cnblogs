const app = getApp();

Page({
  data: {
    stats: {
      totalPatients: 0,
      todayChecks: 0,
      pendingTodos: 0,
      newFeedbacks: 0
    },
    todayTodos: [],
    news: [],
    searchKeyword: ''
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    // 每次显示页面时刷新数据
    this.loadData();
  },

  loadData() {
    // 获取统计数据
    this.loadStats();
    
    // 获取今日待办
    this.loadTodayTodos();
    
    // 获取新闻资讯
    this.loadNews();
  },

  loadStats() {
    const patients = app.getPatients();
    const todos = app.getTodos();
    
    const stats = {
      totalPatients: patients.length,
      todayChecks: todos.filter(todo => todo.type === 'check' && !todo.completed).length,
      pendingTodos: todos.filter(todo => !todo.completed).length,
      newFeedbacks: 3 // 模拟新反馈数量
    };
    
    this.setData({ stats });
  },

  loadTodayTodos() {
    const todos = app.getTodos();
    const todayTodos = todos
      .filter(todo => !todo.completed)
      .slice(0, 3)
      .map(todo => ({
        ...todo,
        priorityText: this.getPriorityText(todo.priority)
      }));
    
    this.setData({ todayTodos });
  },

  loadNews() {
    const news = app.getNews();
    this.setData({ news });
  },

  getPriorityText(priority) {
    const priorityMap = {
      3: '紧急',
      2: '一般',
      1: '普通'
    };
    return priorityMap[priority] || '普通';
  },

  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  onQuickAction(e) {
    const type = e.currentTarget.dataset.type;
    
    switch (type) {
      case 'patient':
        wx.switchTab({
          url: '/pages/patient-list/patient-list'
        });
        break;
      case 'check':
        wx.navigateTo({
          url: '/pages/patient-detail/patient-detail'
        });
        break;
      case 'medicine':
        wx.showToast({
          title: '用药管理功能开发中',
          icon: 'none'
        });
        break;
    }
  },

  onTodoTap(e) {
    const todoId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/todo/todo?id=${todoId}`
    });
  },

  onNewsTap(e) {
    const newsId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/news/news?id=${newsId}`
    });
  },

  navigateToTodo() {
    wx.switchTab({
      url: '/pages/todo/todo'
    });
  },

  navigateToNews() {
    wx.navigateTo({
      url: '/pages/news/news'
    });
  },

  onAddRecord() {
    wx.showActionSheet({
      itemList: ['添加检查记录', '添加用药记录', '添加护理记录'],
      success: (res) => {
        const actions = ['check', 'medicine', 'care'];
        const action = actions[res.tapIndex];
        
        wx.navigateTo({
          url: `/pages/patient-detail/patient-detail?action=${action}`
        });
      }
    });
  }
}); 