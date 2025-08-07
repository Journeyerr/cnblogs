const app = getApp();

Page({
  data: {
    news: []
  },

  onLoad() {
    this.loadNews();
  },

  loadNews() {
    const news = app.getNews();
    this.setData({ news });
  },

  onNewsTap(e) {
    const newsId = e.currentTarget.dataset.id;
    const newsItem = this.data.news.find(n => n.id === newsId);
    if (newsItem) {
      wx.showModal({
        title: newsItem.title,
        content: `${newsItem.subtitle}\n\n${newsItem.date}\n阅读量: ${newsItem.reads}`,
        showCancel: false
      });
    }
  }
});