<template>
  <div class="sleep">
    <h1>睡眠分析</h1>
    <div class="sleep-record">
      <h2>熬夜记录</h2>
      <div class="record-item">
        <div class="record-date">{{ sleepRecord.date }}</div>
        <div class="record-details">
          <div class="score">
            <span>熬夜评分：</span>
            <span class="score-value">{{ sleepRecord.lateNightScore }}</span>
          </div>
          <div class="sleep-time">
            <span>入睡时间：</span>
            <span>{{ formatTime(sleepRecord.sleepTs) }}</span>
          </div>
          <div class="awake-duration">
            <span>清醒时长：</span>
            <span>{{ formatDuration(sleepRecord.awakeDurationMs) }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="sleep-quality">
      <h2>睡眠质量评价</h2>
      <div class="quality-options">
        <button 
          v-for="option in qualityOptions" 
          :key="option.value"
          :class="{ active: selectedQuality === option.value }"
          @click="selectedQuality = option.value"
        >
          {{ option.label }}
        </button>
      </div>
      <button @click="submitSleepQuality" class="submit-btn">提交评价</button>
    </div>
    <div class="screen-summary">
      <h2>亮屏时长汇总</h2>
      <div class="summary-item">
        <span>总亮屏时长：</span>
        <span>{{ formatDuration(screenSummary.totalMs) }}</span>
      </div>
      <div class="summary-item">
        <span>23:00后亮屏时长：</span>
        <span>{{ formatDuration(screenSummary.after23Ms) }}</span>
      </div>
      <div class="summary-item">
        <span>亮屏次数：</span>
        <span>{{ screenSummary.sessionCount }} 次</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getSleepRecord, submitSleepQuality, getDailyScreenSummary } from '../services/api'

export default {
  data() {
    return {
      sleepRecord: {
        recordId: '1',
        date: '2026-04-12',
        lateNightScore: 0,
        sleepTs: Date.now() - 3600000,
        awakeDurationMs: 0,
        sleepQuality: null
      },
      qualityOptions: [
        { value: 1, label: '差' },
        { value: 2, label: '一般' },
        { value: 3, label: '好' }
      ],
      selectedQuality: 2,
      screenSummary: {
        date: '2026-04-12',
        totalMs: 0,
        after23Ms: 0,
        sessionCount: 0
      },
      userId: 'user_123' // 临时用户ID，实际应该从本地存储获取
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    formatTime(timestamp) {
      const date = new Date(timestamp)
      return date.toLocaleTimeString()
    },
    formatDuration(ms) {
      const hours = Math.floor(ms / 3600000)
      const minutes = Math.floor((ms % 3600000) / 60000)
      return `${hours}小时${minutes}分钟`
    },
    async loadData() {
      try {
        const today = new Date().toISOString().split('T')[0]
        // 获取睡眠记录
        const record = await getSleepRecord(this.userId, today)
        if (record) {
          this.sleepRecord = record
        }
        // 获取亮屏汇总
        const summary = await getDailyScreenSummary(this.userId, today)
        if (summary) {
          this.screenSummary = summary
        }
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    async submitSleepQuality() {
      try {
        const today = new Date().toISOString().split('T')[0]
        await submitSleepQuality(this.userId, today, this.selectedQuality)
        alert('睡眠质量评价提交成功！')
      } catch (error) {
        console.error('提交睡眠质量失败:', error)
        alert('提交睡眠质量失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.sleep {
  padding: 20px;
}

.sleep-record, .sleep-quality, .screen-summary {
  background: var(--card-background);
  border-radius: var(--border-radius);
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.sleep-record:hover, .sleep-quality:hover, .screen-summary:hover {
  box-shadow: var(--shadow-hover);
  transform: translateY(-5px);
}

.record-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background-color: var(--secondary-pastel-blue);
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.record-item:hover {
  background-color: var(--secondary-pastel-blue);
  opacity: 0.8;
}

.record-date {
  font-size: 18px;
  font-weight: bold;
  padding: 10px 15px;
  background-color: var(--primary-mint);
  border-radius: var(--border-radius);
  color: white;
  text-align: center;
  min-width: 120px;
}

.record-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
  flex: 1;
  margin-left: 20px;
}

.score-value {
  font-weight: bold;
  color: var(--error-color);
  font-size: 18px;
  padding: 4px 12px;
  background-color: rgba(245, 108, 108, 0.1);
  border-radius: 20px;
  min-width: 60px;
  text-align: center;
}

.quality-options {
  display: flex;
  gap: 15px;
  margin: 20px 0;
}

.quality-options button {
  flex: 1;
  padding: 12px 15px;
  border: 2px solid var(--border-color);
  border-radius: var(--border-radius);
  background: var(--card-background);
  color: var(--text-primary);
  cursor: pointer;
  transition: var(--transition);
  font-weight: 500;
}

.quality-options button:hover {
  border-color: var(--primary-mint);
  transform: translateY(-2px);
}

.quality-options button.active {
  background: var(--primary-mint);
  color: white;
  border-color: var(--primary-mint);
  box-shadow: 0 4px 8px rgba(126, 216, 208, 0.3);
}

.submit-btn {
  background: var(--primary-mint);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: var(--border-radius);
  cursor: pointer;
  width: 100%;
  font-weight: 500;
  transition: var(--transition);
  margin-top: 10px;
}

.submit-btn:hover {
  background: var(--primary-mint-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(126, 216, 208, 0.3);
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 12px 0;
  padding: 12px 15px;
  background-color: var(--secondary-pastel-blue);
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.summary-item:hover {
  background-color: var(--secondary-pastel-blue);
  opacity: 0.8;
  transform: translateX(5px);
}

@media (max-width: 768px) {
  .sleep {
    padding: 10px;
  }
  
  .sleep-record, .sleep-quality, .screen-summary {
    padding: 15px;
  }
  
  .record-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .record-date {
    width: 100%;
    text-align: center;
  }
  
  .record-details {
    margin-left: 0;
    width: 100%;
  }
  
  .quality-options {
    flex-direction: column;
  }
  
  .quality-options button {
    width: 100%;
  }
}
</style>