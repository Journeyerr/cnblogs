const app = getApp();

Page({
  data: {
    content: '',
    selectedSymptoms: [],
    urgency: 'medium',
    phone: '',
    canSubmit: false,
    feedbackHistory: []
  },

  onLoad() {
    this.loadFeedbackHistory();
  },

  loadFeedbackHistory() {
    // 模拟历史反馈数据
    const history = [
      {
        id: 1,
        content: '今天感觉身体好多了，胃口也不错，睡眠质量有所改善。',
        time: '2024-03-15 10:30',
        status: 'replied',
        reply: '很高兴听到您的情况有所改善，请继续保持良好的作息习惯。'
      },
      {
        id: 2,
        content: '昨晚睡眠质量一般，希望能改善睡眠环境。',
        time: '2024-03-14 20:00',
        status: 'replied',
        reply: '我们会调整病房环境，为您提供更好的休息条件。'
      }
    ];
    
    this.setData({ feedbackHistory: history });
  },

  onContentInput(e) {
    this.setData({
      content: e.detail.value
    });
    this.checkCanSubmit();
  },

  onSymptomSelect(e) {
    const symptom = e.currentTarget.dataset.symptom;
    const selectedSymptoms = [...this.data.selectedSymptoms];
    
    const index = selectedSymptoms.indexOf(symptom);
    if (index > -1) {
      selectedSymptoms.splice(index, 1);
    } else {
      selectedSymptoms.push(symptom);
    }
    
    this.setData({
      selectedSymptoms: selectedSymptoms
    });
    this.checkCanSubmit();
  },

  onUrgencySelect(e) {
    const urgency = e.currentTarget.dataset.urgency;
    this.setData({
      urgency: urgency
    });
    this.checkCanSubmit();
  },

  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
    this.checkCanSubmit();
  },

  checkCanSubmit() {
    const { content, urgency, phone } = this.data;
    const canSubmit = content.trim().length > 0 && urgency && phone.trim().length > 0;
    
    this.setData({
      canSubmit: canSubmit
    });
  },

  onSubmit() {
    const { content, selectedSymptoms, urgency, phone } = this.data;
    
    if (!this.data.canSubmit) {
      wx.showToast({
        title: '请完善反馈信息',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '提交中...'
    });

    // 模拟提交
    setTimeout(() => {
      wx.hideLoading();
      
      // 添加到历史记录
      const newFeedback = {
        id: Date.now(),
        content: content,
        time: this.formatTime(new Date()),
        status: 'pending',
        symptoms: selectedSymptoms,
        urgency: urgency
      };
      
      const feedbackHistory = [newFeedback, ...this.data.feedbackHistory];
      
      this.setData({
        feedbackHistory: feedbackHistory,
        content: '',
        selectedSymptoms: [],
        urgency: 'medium',
        phone: ''
      });
      
      this.checkCanSubmit();
      
      wx.showToast({
        title: '反馈提交成功',
        icon: 'success'
      });
    }, 1500);
  },

  formatTime(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`;
  }
}); 