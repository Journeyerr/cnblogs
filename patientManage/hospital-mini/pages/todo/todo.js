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
    showTodoDetail: false,
    todoDetail:{
      id: '',
      targetUserName: '',
      title: '',
      content: '',
      priority: 1, // 默认正常
      date: '',
      time: '',
      tags: []
    },
    addForm: {
      targetUserName: '',
      title: '',
      content: '',
      priority: 1, // 默认正常
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
  },

  onShow() {
    this.loadTodos();
  },

  loadTodos() {
    app.get("/user/todo/list")
    .then(res=>{
      const todos = res.data;
      // 添加类型文本
      const todosWithText = todos.map(todo => ({
        ...todo,
        // 添加优先级文本
        priorityText: this.getPriorityText(todo.urgency),
        operations: this.getOperations(todo.operations)

      }));

      const pendingCount = todos.filter(todo => todo.status === 0).length;
      const completedCount = todos.filter(todo => todo.status === 2).length;
      this.setData({
        todos: todosWithText,
        pendingCount,
        completedCount
      });

      this.filterTodos();
        console.log("todosWithText",todosWithText)
    })
  },

  filterTodos() {
    let filtered = this.data.todos;
    if (this.data.currentFilter === 'pending') {
      filtered = filtered.filter(todo => todo.status === 0);
    } else if (this.data.currentFilter === 'completed') {
      filtered = filtered.filter(todo => todo.status === 2);
    }

    this.setData({
      filteredTodos: filtered
    });
  },

  getOperations(operations) {
      if (app.isNotEmptyAndDefined(operations)) {
          return operations.split(',');
      }
      return [];
  },

  getPriorityText(priority) {
    const priorityMap = {
      3: '紧急',
      2: '正常',
      1: '轻度'
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

  // 新增待办弹窗相关方法
  onAddTodo() {
    // 设置默认日期为今天
    const today = new Date();
    const dateStr = today.toISOString().split('T')[0];
    const timeStr = today.toISOString().split('T')[1].substr(0,5);
    this.setData({
      showAddModal: true,
      addForm: {
        targetUserName: '',
        title: '',
        content: '',
        priority: 2,
        date: dateStr,
        time: timeStr,
        tags: []
      }
    });
  },


  onTodoHidden() {
    this.setData({ showTodoDetail: false, todoDetail:{} });
  },

  onTodoShow(e) {
    const todoId = e.currentTarget.dataset.id;
    const todo = this.data.todos.find(t => t.id === todoId);
    todo.priorityText = this.getPriorityText(todo.urgency);
    this.setData({ showTodoDetail: true, todoDetail: todo });
  },


  onFinishTodo(e) {
    const todoId = e.currentTarget.dataset.id;
    const todo = this.data.todos.find(t => t.id === todoId);
    console.log(todo)
    app.post("/user/todo/update", {
      id: todoId,
      status: 2
    }).then(res=>{
      wx.showToast({
        title: '已完成',
        icon: 'success'
      });
      // 关闭弹窗
      setTimeout(() => {
        this.onTodoHidden();
      }, 1000);

      this.loadTodos();
    })
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
          app.post("/user/todo/delete/" + todoId, null)
            .then(res=>{
              this.loadTodos();
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              });
          })
        }
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
  onAddTargetUserNameInput(e) {
    this.setData({ 'addForm.targetUserName': e.detail.value });
  },

  onAddTitleInput(e) {
    this.setData({ 'addForm.title': e.detail.value });
  },

  onAddContentInput(e) {
    this.setData({ 'addForm.content': e.detail.value });
  },

  onPrioritySelect(e) {
    const priority = e.currentTarget.dataset.priority;
    this.setData({ 'addForm.priority': parseInt(priority) });
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
      item.select = tags.indexOf(item.value) != -1 ? 1 : 0;
    })

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
    const { targetUserName, title, content, priority, date, time, tags } = this.data.addForm;
    // 表单验证
    if (!targetUserName.trim()) {
      wx.showToast({ title: '请输入病患姓名/床位', icon: 'none' });
      return;
    }
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
      targetUserName: targetUserName.trim(),
      content: content.trim(),
      urgency: priority,
      targetUserId:0,
      executeTime: `${date} ${time}`,
      operations: tags.join()
    };
    // 发送请求
    app.post('/user/todo/create', submitData)
    .then(res => {
      wx.showToast({
        title: '新增成功',
        icon: 'success'
      });
       // 关闭弹窗
      setTimeout(() => {
        this.setData({ showAddModal: false });
      }, 1000);
       // 重新加载待办列表
       this.loadTodos();
    }).catch(err => {
      wx.showToast({
        title: res.data.message || '添加失败',
        icon: 'error'
      });
    });
  },

  // 获取当前标签配置
  getTagOptions() {
    return this.data.tagOptions;
  },
}); 