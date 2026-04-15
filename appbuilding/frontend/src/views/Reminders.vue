<template>
  <div class="reminders">
    <h1>提醒管理</h1>
    <div class="generate-plan">
      <h2>生成每日提醒计划</h2>
      <div class="form-group">
        <label>目标日期：</label>
        <input v-model="targetDate" type="date">
      </div>
      <button @click="generateDailyPlan">生成计划</button>
    </div>
    <div class="reminder-list">
      <h2>提醒计划</h2>
      <ul v-if="reminderPlans.length > 0">
        <li v-for="plan in reminderPlans" :key="plan.planId">
          <div class="plan-content">
            <span class="plan-type">{{ plan.planType }}</span>
            <span class="plan-message">{{ plan.message }}</span>
            <span class="plan-priority" :class="getPriorityClass(plan.priority)">
              {{ getPriorityText(plan.priority) }}
            </span>
          </div>
          <div class="plan-time">{{ formatTime(plan.triggerTs) }}</div>
          <div class="plan-actions">
            <button @click="ackReminder(plan.planId, 'delivered')">已收到</button>
            <button @click="ackReminder(plan.planId, 'dismissed')">关闭</button>
            <button @click="ackReminder(plan.planId, 'snoozed')">推迟</button>
          </div>
        </li>
      </ul>
      <p v-else>暂无提醒计划</p>
    </div>
    <!-- 权利声明栏 -->
    <div class="copyright">
      <p>&copy; 2026 大学生健康守护 App. 保留所有权利.</p>
      <div class="copyright-links">
        <a href="#">关于我们</a>
        <a href="#">使用条款</a>
        <a href="#">隐私政策</a>
      </div>
    </div>
  </div>
</template>

<script>
import { generateDailyPlan, getPendingReminders, ackReminder } from '../services/api'

export default {
  data() {
    return {
      targetDate: new Date().toISOString().split('T')[0],
      reminderPlans: [],
      userId: 'user_123' // 临时用户ID，实际应该从本地存储获取
    }
  },
  mounted() {
    this.loadPendingReminders()
  },
  methods: {
    formatTime(timestamp) {
      const date = new Date(timestamp)
      return date.toLocaleString()
    },
    getPriorityClass(priority) {
      switch (priority) {
        case 1: return 'priority-low'
        case 2: return 'priority-medium'
        case 3: return 'priority-high'
        default: return ''
      }
    },
    getPriorityText(priority) {
      switch (priority) {
        case 1: return '低'
        case 2: return '中'
        case 3: return '高'
        default: return ''
      }
    },
    async generateDailyPlan() {
      try {
        const result = await generateDailyPlan(this.userId, this.targetDate)
        this.reminderPlans = result.plans
        alert('提醒计划生成成功！')
      } catch (error) {
        console.error('生成提醒计划失败:', error)
        alert('生成提醒计划失败，请重试')
      }
    },
    async ackReminder(planId, action) {
      try {
        await ackReminder(planId, action, action === 'snoozed' ? 10 : undefined)
        alert(`提醒已${action === 'delivered' ? '收到' : action === 'dismissed' ? '关闭' : '推迟'}`)
        // 重新加载待处理提醒
        this.loadPendingReminders()
      } catch (error) {
        console.error('响应提醒失败:', error)
        alert('响应提醒失败，请重试')
      }
    },
    async loadPendingReminders() {
      try {
        this.reminderPlans = await getPendingReminders(this.userId)
      } catch (error) {
        console.error('获取待处理提醒失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.reminders {
  padding: 20px;
}

.generate-plan, .reminder-list {
  background: var(--card-background);
  border-radius: var(--border-radius);
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.generate-plan:hover, .reminder-list:hover {
  box-shadow: var(--shadow-hover);
  transform: translateY(-5px);
}

.form-group {
  margin: 15px 0;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-primary);
}

input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
  background-color: white;
  color: var(--text-primary);
}

input:focus {
  outline: none;
  border-color: var(--primary-mint);
  box-shadow: 0 0 0 2px rgba(126, 216, 208, 0.2);
}

button {
  background: var(--primary-mint);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: var(--border-radius);
  cursor: pointer;
  margin-top: 10px;
  font-weight: 500;
  transition: var(--transition);
}

button:hover {
  background: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(126, 216, 208, 0.3);
}

button:active {
  transform: translateY(0);
}

li {
  background-color: white;
  border-radius: var(--border-radius);
  padding: 15px;
  margin-bottom: 15px;
  transition: var(--transition);
  border-left: 4px solid var(--primary-mint);
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

li:hover {
  background-color: var(--background-light);
  transform: translateX(5px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.plan-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 15px;
}

.plan-type {
  font-size: 12px;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 500;
}

.plan-message {
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
  color: var(--text-primary);
}

.plan-priority {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  color: white;
  font-weight: 500;
  margin-top: 5px;
}

.priority-low {
  background: var(--primary-mint);
}

.priority-medium {
  background: var(--secondary-pastel-blue);
  color: var(--text-primary);
}

.priority-high {
  background: var(--error-color);
}

.plan-time {
  font-size: 12px;
  color: var(--text-secondary);
  margin-bottom: 15px;
  padding-top: 10px;
  border-top: 1px solid var(--border-color);
}

.plan-actions {
  display: flex;
  gap: 10px;
}

.plan-actions button {
  margin-top: 0;
  flex: 1;
  padding: 8px 12px;
  font-size: 14px;
}

.plan-actions button:nth-child(2) {
  background: var(--text-secondary);
}

.plan-actions button:nth-child(2):hover {
  background: var(--text-primary);
}

.plan-actions button:nth-child(3) {
  background: var(--secondary-pastel-blue);
  color: var(--text-primary);
}

.plan-actions button:nth-child(3):hover {
  background: var(--secondary-dark);
  color: white;
}

.copyright {
  background: var(--card-background);
  border-radius: var(--border-radius);
  padding: 20px;
  margin-top: 30px;
  text-align: center;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.copyright:hover {
  box-shadow: var(--shadow-hover);
}

.copyright p {
  margin-bottom: 15px;
  color: var(--text-secondary);
  font-size: 14px;
}

.copyright-links {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
}

.copyright-links a {
  color: var(--primary-mint);
  text-decoration: none;
  font-size: 12px;
  transition: var(--transition);
  padding: 4px 8px;
  border-radius: 4px;
}

.copyright-links a:hover {
  color: var(--primary-dark);
  background-color: rgba(126, 216, 208, 0.1);
  text-decoration: none;
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .reminders {
    padding: 10px;
  }
  
  .generate-plan, .reminder-list, .copyright {
    padding: 15px;
  }
  
  .plan-actions {
    flex-direction: column;
  }
  
  .copyright-links {
    gap: 10px;
  }
}
</style>