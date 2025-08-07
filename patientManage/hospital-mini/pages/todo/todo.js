const app = getApp();

Page({
  data: {
    todos: [],
    filteredTodos: [],
    currentFilter: 'all',
    pendingCount: 0,
    completedCount: 0,
    // 新增弹窗相关数据
    showAddModal: false,
    addForm: {
      title: '',
      content: '',
      priority: 'medium', // 默认正常
      date: '',
      time: '',
      tags: []
    },
    // 可配置的操作标签选项
    tagOptions: [
      { label: '量体温', value: '量体温', priority: 'low', select: 0 },
      { label: '抽血', value: '抽血', priority: 'medium', select: 0 },
      { label: '检查', value: '检查', priority: 'high', select: 0 },
      { label: '换药水', value: '换药水', priority: 'medium', select: 0 },
      { label: '输液', value: '输液', priority: 'high', select: 0 },
      { label: '换药', value: '换药', priority: 'medium', select: 0 },
      { label: '测量血压', value: '测量血压', priority: 'low', select: 0 },
      { label: '心电图', value: '心电图', priority: 'high', select: 0 }
    ]
  },

  onLoad() {
    app.checkLoginStatus();
    this.loadTodos();
  },

  onShow() {
    this.loadTodos();
  },

  loadTodos() {
    const todos = app.getTodos();
    
    // 添加类型文本
    const todosWithText = todos.map(todo => ({
      ...todo,
      typeText: this.getTypeText(todo.type),
      priorityText: this.getPriorityText(todo.priority)
    }));

    const pendingCount = todos.filter(todo => !todo.completed).length;
    const completedCount = todos.filter(todo => todo.completed).length;

    this.setData({
      todos: todosWithText,
      pendingCount,
      completedCount
    });

    this.filterTodos();
  },

  filterTodos() {
    let filtered = this.data.todos;

    if (this.data.currentFilter === 'pending') {
      filtered = filtered.filter(todo => !todo.completed);
    } else if (this.data.currentFilter === 'completed') {
      filtered = filtered.filter(todo => todo.completed);
    }

    this.setData({
      filteredTodos: filtered
    });
  },

  getTypeText(type) {
    const typeMap = {
      'check': '检查',
      'medicine': '用药',
      'care': '护理',
      'other': '其他'
    };
    return typeMap[type] || '其他';
  },

  getPriorityText(priority) {
    const priorityMap = {
      'high': '紧急',
      'medium': '正常',
      'low': '轻度'
    };
    return priorityMap[priority] || '正常';
  },

  onFilterChange(e) {
    const filter = e.currentTarget.dataset.filter;
    this.setData({
      currentFilter: filter
    });
    this.filterTodos();
  },

  onToggleTodo(e) {
    const todoId = e.currentTarget.dataset.id;
    const todos = this.data.todos;
    const todoIndex = todos.findIndex(todo => todo.id === todoId);
    
    if (todoIndex !== -1) {
      todos[todoIndex].completed = !todos[todoIndex].completed;
      
      this.setData({
        todos: todos
      });
      
      this.loadTodos();
      
      wx.showToast({
        title: todos[todoIndex].completed ? '已完成' : '已取消完成',
        icon: 'success'
      });
    }
  },

  onTodoTap(e) {
    const todoId = e.currentTarget.dataset.id;
    const todo = this.data.todos.find(t => t.id === todoId);
    
    wx.showModal({
      title: todo.title,
      content: `病人: ${todo.patientName}\n时间: ${todo.dueTime}\n类型: ${todo.typeText}\n优先级: ${todo.priorityText}`,
      showCancel: false
    });
  },

  onEditTodo(e) {
    const todoId = e.currentTarget.dataset.id;
    const todo = this.data.todos.find(t => t.id === todoId);
    
    wx.showModal({
      title: '编辑待办',
      content: '请输入新的待办内容',
      editable: true,
      placeholderText: todo.title,
      success: (res) => {
        if (res.confirm && res.content) {
          todo.title = res.content;
          this.setData({
            todos: this.data.todos
          });
          
          wx.showToast({
            title: '编辑成功',
            icon: 'success'
          });
        }
      }
    });
  },

  onDeleteTodo(e) {
    const todoId = e.currentTarget.dataset.id;
    const todo = this.data.todos.find(t => t.id === todoId);
    
    wx.showModal({
      title: '确认删除',
      content: `确定要删除"${todo.title}"吗？`,
      success: (res) => {
        if (res.confirm) {
          const todos = this.data.todos.filter(t => t.id !== todoId);
          this.setData({
            todos: todos
          });
          
          this.loadTodos();
          
          wx.showToast({
            title: '删除成功',
            icon: 'success'
          });
        }
      }
    });
  },

  // 新增待办弹窗相关方法
  onAddTodo() {
    // 设置默认日期为今天
    const today = new Date();
    const dateStr = today.toISOString().split('T')[0];
    const timeStr = today.toISOString().split('T')[1].substr(0,5);
    this.setData({
      showAddModal: true,
      addForm: {
        title: '',
        content: '',
        priority: 'medium',
        date: dateStr,
        time: timeStr,
        tags: []
      }
    });
  },

  onModalMaskTap() {
    this.setData({ showAddModal: false });
  },

  onModalContentTap() {
    // 阻止冒泡
  },

  onAddModalCancel() {
    this.setData({ showAddModal: false });
  },

  // 表单输入事件
  onAddTitleInput(e) {
    this.setData({ 'addForm.title': e.detail.value });
  },

  onAddContentInput(e) {
    this.setData({ 'addForm.content': e.detail.value });
  },

  onPrioritySelect(e) {
    const priority = e.currentTarget.dataset.priority;
    this.setData({ 'addForm.priority': priority });
  },

  onDateChange(e) {
    this.setData({ 'addForm.date': e.detail.value });
  },

  onTimeChange(e) {
    this.setData({ 'addForm.time': e.detail.value });
  },

  onTagToggle(e) {
    const tag = String(e.currentTarget.dataset.tag); // 强制转字符串
    let tags = this.data.addForm.tags || []; // 确保tags是数组
    tags = tags.map(String); // 保证全是字符串

    const newItems = [...this.data.tagOptions]
    const isSelected = tags.includes(tag);
    if (isSelected) {
      tags = tags.filter(t => t !== tag);
    } else {
      tags.push(tag);
    }

    newItems.forEach(item => {
      console.log(item.value,tags.indexOf(item.value));
      if (tags.indexOf(item.value) != -1) {
        item.select = 1;
      } else {
        item.select = 0;
      }
    })

    console.log("全部tagOptions");
    console.log(newItems);

    // 更新数据并触发视图更新
    this.setData({ 
      'addForm.tags': tags,
      'tagOptions': newItems
    });
  },

  // 全选标签
  onSelectAllTags() {
    const newItems = [...this.data.tagOptions]
    newItems.forEach(item => item.select = 1);
    const allTags = this.data.tagOptions.map(item => String(item.value));
    this.setData({ 'addForm.tags': allTags, 'tagOptions': newItems });
    wx.showToast({
      title: '已全选',
      icon: 'success'
    });
  },

  // 清空标签
  onClearAllTags() {
    const newItems = [...this.data.tagOptions]
    newItems.forEach(item => item.select = 0);
    this.setData({ 'addForm.tags': [], 'tagOptions': newItems });
    wx.showToast({
      title: '已清空',
      icon: 'success'
    });
  },

  // 提交新增待办
  onAddModalSubmit() {
    const { title, content, priority, date, time, tags } = this.data.addForm;
    
    // 表单验证
    if (!title.trim()) {
      wx.showToast({ title: '请输入事项名称', icon: 'none' });
      return;
    }
    if (!content.trim()) {
      wx.showToast({ title: '请输入事项内容', icon: 'none' });
      return;
    }
    if (!date || !time) {
      wx.showToast({ title: '请选择执行时间', icon: 'none' });
      return;
    }

    // 构建提交数据
    const submitData = {
      title: title.trim(),
      content: content.trim(),
      priority: priority,
      dueTime: `${date} ${time}`,
      tags: tags,
      completed: false
    };

    // 调用接口
    wx.request({
      url: 'http://localhost:8080/api/todo',
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + wx.getStorageSync('token')
      },
      data: submitData,
      success: (res) => {
        console.log('新增待办响应:', res);
        if (res.statusCode === 200 && res.data.code === 200) {
          wx.showToast({
            title: '添加成功',
            icon: 'success'
          });
          
          // 关闭弹窗
          this.setData({ showAddModal: false });
          
          // 重新加载待办列表
          this.loadTodos();
        } else {
          wx.showToast({
            title: res.data.message || '添加失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        console.error('新增待办失败:', err);
        wx.showToast({
          title: '网络错误，请重试',
          icon: 'none'
        });
      }
    });
  },

  // 获取当前标签配置
  getTagOptions() {
    return this.data.tagOptions;
  },

}); 