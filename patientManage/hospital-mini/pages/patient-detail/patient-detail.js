const app = getApp();

Page({
  data: {
    patients: [],
    filteredPatients: [],
    currentFilter: 'all',
    searchKeyword: '',
    hospitalizedCount: 0,
    dischargedCount: 0
  },

  onLoad() {
    this.loadPatients();
  },

  onShow() {
    this.loadPatients();
  },

  loadPatients() {
    const patients = app.getPatients();
    
    // 计算住院天数
    const patientsWithDays = patients.map(patient => {
      const admissionDate = new Date(patient.admissionDate);
      const today = new Date();
      const diffTime = Math.abs(today - admissionDate);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      
      return {
        ...patient,
        hospitalDays: diffDays
      };
    });

    const hospitalizedCount = patientsWithDays.filter(p => p.status === '住院中').length;
    const dischargedCount = patientsWithDays.filter(p => p.status === '已出院').length;

    this.setData({
      patients: patientsWithDays,
      hospitalizedCount,
      dischargedCount
    });

    this.filterPatients();
  },

  filterPatients() {
    let filtered = this.data.patients;

    // 按状态筛选
    if (this.data.currentFilter === 'hospitalized') {
      filtered = filtered.filter(p => p.status === '住院中');
    } else if (this.data.currentFilter === 'discharged') {
      filtered = filtered.filter(p => p.status === '已出院');
    }

    // 按搜索关键词筛选
    if (this.data.searchKeyword) {
      const keyword = this.data.searchKeyword.toLowerCase();
      filtered = filtered.filter(p => 
        p.name.toLowerCase().includes(keyword) ||
        p.room.includes(keyword) ||
        p.diagnosis.toLowerCase().includes(keyword)
      );
    }

    this.setData({
      filteredPatients: filtered
    });
  },

  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
    this.filterPatients();
  },

  onFilterChange(e) {
    const filter = e.currentTarget.dataset.filter;
    this.setData({
      currentFilter: filter
    });
    this.filterPatients();
  },

  onPatientTap(e) {
    const patientId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/patient-detail/patient-detail?id=${patientId}`
    });
  },

  onQuickCheck(e) {
    const patientId = e.currentTarget.dataset.id;
    const patient = this.data.patients.find(p => p.id === patientId);
    
    wx.showActionSheet({
      itemList: ['体温测量', '血压测量', '心率测量', '血糖测量'],
      success: (res) => {
        const checkTypes = ['temperature', 'bloodPressure', 'heartRate', 'bloodSugar'];
        const checkType = checkTypes[res.tapIndex];
        
        this.showCheckInput(patient, checkType);
      }
    });
  },

  showCheckInput(patient, checkType) {
    const checkTypeMap = {
      'temperature': '体温',
      'bloodPressure': '血压',
      'heartRate': '心率',
      'bloodSugar': '血糖'
    };

    wx.showModal({
      title: `${patient.name} - ${checkTypeMap[checkType]}测量`,
      content: '请输入测量值',
      editable: true,
      placeholderText: '请输入数值',
      success: (res) => {
        if (res.confirm && res.content) {
          const value = res.content;
          this.updatePatientCheck(patient.id, checkType, value);
        }
      }
    });
  },

  updatePatientCheck(patientId, checkType, value) {
    const checkData = {};
    checkData[checkType] = value;
    
    app.updatePatientCheck(patientId, checkData);
    
    wx.showToast({
      title: '检查记录已更新',
      icon: 'success'
    });

    // 刷新数据
    this.loadPatients();
  },

  onViewDetail(e) {
    const patientId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/patient-detail/patient-detail?id=${patientId}`
    });
  },

  onAddPatient() {
    wx.showModal({
      title: '添加新病人',
      content: '',
      editable: true,
      placeholderText: '姓名,年龄,性别,房间号,床号,诊断',
      success: (res) => {
        if (res.confirm && res.content) {
          this.parseAndAddPatient(res.content);
        }
      }
    });
  },

  parseAndAddPatient(input) {
    const parts = input.split(',');
    if (parts.length < 6) {
      wx.showToast({
        title: '信息不完整',
        icon: 'none'
      });
      return;
    }

    const [name, age, gender, room, bed, diagnosis] = parts;
    const newPatient = {
      id: Date.now(),
      name: name.trim(),
      age: parseInt(age),
      gender: gender.trim(),
      room: room.trim(),
      bed: bed.trim(),
      admissionDate: new Date().toISOString().split('T')[0],
      diagnosis: diagnosis.trim(),
      status: '住院中',
      latestCheck: {
        temperature: 0,
        bloodPressure: '0/0',
        heartRate: 0,
        bloodSugar: 0
      }
    };

    // 添加到全局数据
    app.globalData.patients.push(newPatient);
    
    wx.showToast({
      title: '病人添加成功',
      icon: 'success'
    });

    this.loadPatients();
  }
}); 