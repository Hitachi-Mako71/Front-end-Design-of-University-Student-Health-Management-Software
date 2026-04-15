<template>
  <div class="user">
    <h1>用户管理</h1>
    <div class="user-form">
      <h2>创建用户</h2>
      <div class="form-group">
        <label>昵称：</label>
        <input v-model="nickname" type="text" placeholder="请输入昵称">
      </div>
      <button @click="createUser">创建用户</button>
    </div>
    <div class="health-form" v-if="userId">
      <h2>健康档案</h2>
      <div class="form-group">
        <label>是否有高血压：</label>
        <input v-model="healthProfile.hasHypertension" type="checkbox">
      </div>
      <div class="form-group">
        <label>是否有心脏病：</label>
        <input v-model="healthProfile.hasHeartDisease" type="checkbox">
      </div>
      <div class="form-group">
        <label>是否有焦虑症：</label>
        <input v-model="healthProfile.hasAnxiety" type="checkbox">
      </div>
      <div class="form-group">
        <label>是否有睡眠障碍：</label>
        <input v-model="healthProfile.hasSleepDisorder" type="checkbox">
      </div>
      <div class="form-group">
        <label>通常睡眠时间：</label>
        <input v-model="healthProfile.usualSleepTime" type="time">
      </div>
      <div class="form-group">
        <label>通常起床时间：</label>
        <input v-model="healthProfile.usualWakeTime" type="time">
      </div>
      <div class="form-group">
        <label>敏感度等级：</label>
        <select v-model="healthProfile.sensitivityLevel">
          <option value="1">低</option>
          <option value="2">中</option>
          <option value="3">高</option>
        </select>
      </div>
      <div class="form-group">
        <label>其他说明：</label>
        <textarea v-model="healthProfile.extraNotes" placeholder="请输入其他健康信息"></textarea>
      </div>
      <button @click="submitHealthProfile">提交健康档案</button>
    </div>
    <div class="user-info" v-if="userId">
      <h2>用户信息</h2>
      <p>用户ID：{{ userId }}</p>
      <p>昵称：{{ nickname }}</p>
    </div>
  </div>
</template>

<script>
import { createUser, submitHealthProfile } from '../services/api'

export default {
  data() {
    return {
      nickname: '',
      userId: '',
      healthProfile: {
        hasHypertension: false,
        hasHeartDisease: false,
        hasAnxiety: false,
        hasSleepDisorder: false,
        usualSleepTime: '23:30',
        usualWakeTime: '07:00',
        sensitivityLevel: 2,
        extraNotes: ''
      }
    }
  },
  mounted() {
    // 从本地存储获取用户ID和昵称
    const savedUserId = localStorage.getItem('userId')
    const savedNickname = localStorage.getItem('nickname')
    if (savedUserId && savedNickname) {
      this.userId = savedUserId
      this.nickname = savedNickname
    }
  },
  methods: {
    async createUser() {
      try {
        const user = await createUser(this.nickname)
        this.userId = user.userId
        // 保存到本地存储
        localStorage.setItem('userId', user.userId)
        localStorage.setItem('nickname', user.nickname)
        alert('用户创建成功！')
      } catch (error) {
        console.error('创建用户失败:', error)
        alert('创建用户失败，请重试')
      }
    },
    async submitHealthProfile() {
      try {
        await submitHealthProfile(this.userId, this.healthProfile)
        alert('健康档案提交成功！')
      } catch (error) {
        console.error('提交健康档案失败:', error)
        alert('提交健康档案失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.user {
  padding: 20px;
}

.user-form, .health-form, .user-info {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: var(--shadow);
  transition: var(--transition);
  border: 1px solid var(--primary-mint-light);
}

.user-form:hover, .health-form:hover, .user-info:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-hover);
  border-color: var(--primary-mint);
}

.user-form h2, .health-form h2, .user-info h2 {
  font-size: 18px;
  font-weight: 500;
  color: var(--text-primary);
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
}

.user-form h2::before {
  content: '👤';
  margin-right: 8px;
  font-size: 16px;
  background-color: var(--primary-mint-light);
  padding: 6px;
  border-radius: var(--border-radius-pill);
  color: var(--text-primary);
}

.health-form h2::before {
  content: '🏥';
  margin-right: 8px;
  font-size: 16px;
  background-color: var(--primary-mint-light);
  padding: 6px;
  border-radius: var(--border-radius-pill);
  color: var(--text-primary);
}

.user-info h2::before {
  content: 'ℹ️';
  margin-right: 8px;
  font-size: 16px;
  background-color: var(--primary-mint-light);
  padding: 6px;
  border-radius: var(--border-radius-pill);
  color: var(--text-primary);
}

.form-group {
  margin: 20px 0;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-primary);
  font-size: 14px;
}

input[type="text"], input[type="time"], select, textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-sm);
  font-size: 14px;
  background-color: var(--background-color);
  color: var(--text-primary);
  transition: var(--transition);
}

input[type="text"]:focus, input[type="time"]:focus, select:focus, textarea:focus {
  outline: none;
  border-color: var(--primary-mint);
  box-shadow: 0 0 0 3px rgba(126, 216, 208, 0.1);
}

input[type="checkbox"] {
  width: auto;
  margin-right: 10px;
  transform: scale(1.2);
  accent-color: var(--primary-mint);
}

button {
  background-color: var(--primary-mint);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: var(--border-radius-sm);
  cursor: pointer;
  margin-top: 15px;
  font-size: 14px;
  font-weight: 500;
  transition: var(--transition);
  margin-right: 10px;
}

button:hover {
  background-color: var(--primary-mint-dark);
  transform: translateY(-2px);
  box-shadow: var(--shadow);
}

button:active {
  transform: translateY(0);
}

.user-info p {
  margin: 10px 0;
  padding: 12px;
  background-color: var(--background-color);
  border-radius: var(--border-radius-sm);
  font-size: 14px;
  color: var(--text-primary);
  border-left: 4px solid var(--primary-mint-light);
}

@media (max-width: 768px) {
  .user {
    padding: 10px;
  }
  
  .user-form, .health-form, .user-info {
    padding: 16px;
  }
  
  button {
    padding: 10px 20px;
  }
}
</style>