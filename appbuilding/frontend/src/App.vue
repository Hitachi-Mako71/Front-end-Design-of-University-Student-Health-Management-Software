<template>
  <div id="app">
    <Navbar />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue'

export default {
  components: {
    Navbar
  }
}
</script>

<style>
:root {
  /* 品牌主色调 */
  --primary-mint: #7ED8D0; /* 薄荷青绿（更柔和） */
  --primary-mint-light: #AEE2DA;
  --primary-mint-dark: #6AC8C0;
  
  /* 辅助色 */
  --secondary-pastel-blue: #E3F2FD;
  --secondary-pastel-green: #E8F5E8;
  --secondary-pastel-purple: #F3E5F5;
  --secondary-pastel-orange: #FFF3E0;
  
  /* 背景和文本 */
  --background-color: #F8FAFC;
  --card-background: #FFFFFF;
  --text-primary: #333333; /* 更深一点，提高可读性 */
  --text-secondary: #666666;
  --border-color: #e5e7eb;
  
  /* 阴影和圆角 */
  --shadow: 0 2px 8px rgba(0,0,0,0.08);
  --shadow-hover: 0 5px 15px rgba(0,0,0,0.1);
  --border-radius: 20px; /* 超级圆角 */
  --border-radius-sm: 12px;
  --border-radius-pill: 50px; /* 药丸形 */
  
  /* 过渡效果 */
  --transition: all 0.3s ease;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'PingFang SC', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: var(--background-color);
  color: var(--text-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  padding-bottom: 80px; /* 为底部导航栏留出空间 */
}

h1 {
  color: var(--primary-mint);
  margin-bottom: 30px;
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid var(--border-color);
}

h2 {
  color: var(--text-primary);
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 500;
}

@media (max-width: 768px) {
  .main-content {
    padding: 10px;
    padding-bottom: 70px; /* 为底部导航栏留出空间 */
  }
  
  h1 {
    font-size: 20px;
    padding: 15px 0;
  }
  
  h2 {
    font-size: 16px;
  }
}
</style>