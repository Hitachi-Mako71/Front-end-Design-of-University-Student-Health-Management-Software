<template>
  <!-- 顶部导航栏组件 -->
  <nav class="navbar">
    <div class="navbar-content">
      <div class="navbar-title">健康守护</div>
      <div class="navbar-actions">
        <button class="action-button">+</button>
      </div>
    </div>
    <div class="navbar-links">
      <!-- 首页导航链接 -->
      <router-link to="/" class="navbar-link">
        <div class="link-icon">🏠</div>
        <div class="link-text">首页</div>
      </router-link>
      <!-- 用户管理导航链接 -->
      <router-link to="/user" class="navbar-link">
        <div class="link-icon">👤</div>
        <div class="link-text">用户</div>
      </router-link>
      <!-- 课表管理导航链接 -->
      <router-link to="/schedule" class="navbar-link">
        <div class="link-icon">📅</div>
        <div class="link-text">课表</div>
      </router-link>
      <!-- 睡眠分析导航链接 -->
      <router-link to="/sleep" class="navbar-link">
        <div class="link-icon">😴</div>
        <div class="link-text">睡眠</div>
      </router-link>
      <!-- 提醒管理导航链接 -->
      <router-link to="/reminders" class="navbar-link">
        <div class="link-icon">🔔</div>
        <div class="link-text">提醒</div>
      </router-link>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'Navbar'
}
</script>

<style scoped>
.navbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: var(--card-background);
  box-shadow: 0 -2px 10px rgba(0,0,0,0.1);
  z-index: 100;
  padding: 10px 0;
}

.navbar-content {
  display: none; /* 隐藏顶部内容，只保留导航链接 */
}

.navbar-links {
  display: flex;
  justify-content: space-around;
  padding: 0 20px;
}

.navbar-link {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: var(--text-primary);
  text-decoration: none;
  padding: 8px 16px;
  border-radius: var(--border-radius-pill);
  transition: var(--transition);
  min-width: 60px;
}

.navbar-link:hover {
  color: var(--primary-mint);
  background-color: rgba(94, 216, 208, 0.1);
  transform: translateY(-2px);
}

.navbar-link.router-link-active {
  color: var(--primary-mint);
  font-weight: bold;
  background-color: rgba(94, 216, 208, 0.1);
}

.link-icon {
  font-size: 1.2rem;
  margin-bottom: 4px;
}

.link-text {
  font-size: 0.8rem;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .navbar-links {
    padding: 0 10px;
  }
  
  .link-icon {
    font-size: 1rem;
  }
  
  .link-text {
    font-size: 0.7rem;
  }
  
  .navbar-link {
    min-width: 50px;
    padding: 6px 12px;
  }
}
</style>