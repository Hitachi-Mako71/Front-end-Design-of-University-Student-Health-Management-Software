<template>
  <div class="health-dashboard">
    <header class="dashboard-header">
      <h1 class="dashboard-title">大学生健康守护 App</h1>
    </header>
    
    <section class="reminder-completion">
      <div class="completion-header">
        <h2>今日健康提醒完成率</h2>
      </div>
      <div class="completion-chart">
        <svg width="200" height="200" viewBox="0 0 200 200">
          <circle cx="100" cy="100" r="80" fill="none" stroke="#E0E0E0" stroke-width="16" />
          <circle cx="100" cy="100" r="80" fill="none" stroke="#7ED8D0" stroke-width="16" stroke-dasharray="502.4" stroke-dashoffset="150.7" stroke-linecap="round" transform="rotate(-90 100 100)" />
          <text x="100" y="95" text-anchor="middle" font-size="24" font-weight="bold" fill="var(--text-primary)">65%</text>
          <text x="100" y="120" text-anchor="middle" font-size="14" fill="var(--text-secondary)">已完成</text>
        </svg>
      </div>
    </section>
    
    <section class="today-courses">
      <h2>今日课程</h2>
      <div v-if="todayCourses.length > 0" class="courses-list">
        <div v-for="course in todayCourses" :key="course.course_id" class="course-item">
          <div class="course-name">{{ course.course_name }}</div>
          <div class="course-time">{{ course.class_start_time }} ({{ course.period_start }}-{{ course.period_end }}节)</div>
          <div class="course-classroom">{{ course.classroom }}</div>
        </div>
      </div>
      <div v-else class="no-courses">
        <p>今日无课程</p>
      </div>
    </section>
    
    <section class="data-visualization">
      <div class="chart-card">
        <h3>一周睡眠时长趋势</h3>
        <div class="chart-container">
          <canvas ref="sleepLineChart"></canvas>
        </div>
      </div>
      
      <div class="chart-card">
        <h3>有效/无效熬夜时长对比</h3>
        <div class="chart-container">
          <canvas ref="sleepBarChart"></canvas>
        </div>
      </div>
      
      <div class="chart-card">
        <h3>熬夜类型分布</h3>
        <div class="chart-container">
          <canvas ref="sleepPieChart"></canvas>
        </div>
      </div>
      
      <div class="chart-card">
        <h3>每日课程时长统计</h3>
        <div class="chart-container">
          <canvas ref="courseBarChart"></canvas>
        </div>
      </div>
      
      <div class="chart-card">
        <h3>亮屏时长统计</h3>
        <div class="chart-container">
          <canvas ref="screenTimeChart"></canvas>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'
import { getTodayCourses, getPendingReminders, getSleepRecord } from '../services/api'

export default {
  data() {
    return {
      todayCourses: [],
      pendingReminders: [],
      sleepScore: 0,
      userId: 'user_123',
      charts: {}
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeUnmount() {
    Object.values(this.charts).forEach(chart => chart.destroy())
  },
  methods: {
    async loadData() {
      try {
        this.todayCourses = await getTodayCourses(this.userId)
        this.pendingReminders = await getPendingReminders(this.userId)
        const yesterday = new Date()
        yesterday.setDate(yesterday.getDate() - 1)
        const dateStr = yesterday.toISOString().split('T')[0]
        const sleepRecord = await getSleepRecord(this.userId, dateStr)
        if (sleepRecord) {
          this.sleepScore = sleepRecord.lateNightScore
        }
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts() {
      this.initSleepLineChart()
      this.initSleepBarChart()
      this.initSleepPieChart()
      this.initCourseBarChart()
      this.initScreenTimeChart()
    },
    initSleepLineChart() {
      const ctx = this.$refs.sleepLineChart.getContext('2d')
      this.charts.sleepLineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          datasets: [{
            label: '睡眠时长(小时)',
            data: [6.5, 6.2, 6.0, 5.0, 7.9, 9.2, 8.3],
            borderColor: '#7ED8D0',
            backgroundColor: 'rgba(126, 216, 208, 0.2)',
            tension: 0.4
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'top',
              labels: {
                color: 'var(--text-primary)'
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '小时',
                color: 'var(--text-primary)'
              },
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            },
            x: {
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            }
          }
        }
      })
    },
    initSleepBarChart() {
      const ctx = this.$refs.sleepBarChart.getContext('2d')
      this.charts.sleepBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['有效熬夜', '无效熬夜'],
          datasets: [{
            label: '熬夜时长(小时)',
            data: [10.5, 38.2],
            backgroundColor: ['#7ED8D0', '#E3F2FD'],
            borderColor: ['#6AC8C0', '#E3F2FD'],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'top',
              labels: {
                color: 'var(--text-primary)'
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '小时',
                color: 'var(--text-primary)'
              },
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            },
            x: {
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            }
          }
        }
      })
    },
    initSleepPieChart() {
      const ctx = this.$refs.sleepPieChart.getContext('2d')
      this.charts.sleepPieChart = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: ['学习熬夜', '娱乐熬夜', '其他'],
          datasets: [{
            data: [30, 60, 10],
            backgroundColor: ['#7ED8D0', '#E3F2FD', '#E8F5E8'],
            borderColor: ['#6AC8C0', '#E3F2FD', '#E8F5E8'],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'top',
              labels: {
                color: 'var(--text-primary)'
              }
            },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const label = context.label || ''
                  const value = context.raw || 0
                  const total = context.dataset.data.reduce((a, b) => a + b, 0)
                  const percentage = Math.round((value / total) * 100)
                  return `${label}: ${value}%`
                }
              }
            }
          }
        }
      })
    },
    initCourseBarChart() {
      const ctx = this.$refs.courseBarChart.getContext('2d')
      this.charts.courseBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          datasets: [{
            label: '每日课程时长(小时)',
            data: [4, 6, 3, 5, 2, 0, 1],
            backgroundColor: '#E3F2FD',
            borderColor: '#E3F2FD',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'top',
              labels: {
                color: 'var(--text-primary)'
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '小时',
                color: 'var(--text-primary)'
              },
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            },
            x: {
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            }
          }
        }
      })
    },
    initScreenTimeChart() {
      const ctx = this.$refs.screenTimeChart.getContext('2d')
      this.charts.screenTimeChart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
          datasets: [{
            label: '亮屏时长(小时)',
            data: [5.2, 4.8, 6.1, 5.5, 4.2, 7.8, 6.5],
            backgroundColor: '#E8F5E8',
            borderColor: '#E8F5E8',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'top',
              labels: {
                color: 'var(--text-primary)'
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                text: '小时',
                color: 'var(--text-primary)'
              },
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            },
            x: {
              ticks: {
                color: 'var(--text-secondary)'
              },
              grid: {
                display: false
              }
            }
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.health-dashboard {
  padding: 0;
  background-color: var(--background-color);
  min-height: 100vh;
}

.dashboard-header {
  margin-bottom: 30px;
  text-align: center;
  padding: 20px 0;
  background: linear-gradient(180deg, var(--primary-mint) 0%, rgba(255,255,255,0) 100%);
  border-bottom-left-radius: var(--border-radius);
  border-bottom-right-radius: var(--border-radius);
}

.dashboard-title {
  font-size: 24px;
  font-weight: bold;
  color: white;
  margin: 0;
  text-shadow: 0 1px 2px rgba(0,0,0,0.1);
}

.reminder-completion {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: var(--shadow);
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: var(--transition);
  border: 1px solid var(--primary-mint-light);
}

.reminder-completion:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-hover);
  border-color: var(--primary-mint);
}

.completion-header {
  margin-bottom: 24px;
  text-align: center;
}

.completion-header h2 {
  font-size: 18px;
  font-weight: 500;
  color: var(--text-primary);
  margin: 0;
  text-align: center;
  display: flex;
  align-items: center;
}

.completion-header h2::before {
  content: '🔔';
  margin-right: 8px;
  font-size: 16px;
  background-color: var(--primary-mint-light);
  padding: 6px;
  border-radius: var(--border-radius-pill);
  color: var(--text-primary);
}

.completion-chart {
  display: flex;
  justify-content: center;
}

.today-courses {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.today-courses:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-hover);
}

.today-courses h2 {
  font-size: 18px;
  font-weight: 500;
  color: var(--text-primary);
  margin: 0 0 16px 0;
  display: flex;
  align-items: center;
}

.today-courses h2::before {
  content: '📅';
  margin-right: 8px;
  font-size: 16px;
}

.courses-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.course-item {
  padding: 16px;
  background-color: var(--secondary-pastel-blue);
  border-radius: var(--border-radius);
  transition: var(--transition);
  border-left: 4px solid var(--primary-mint);
}

.course-item:hover {
  background-color: var(--secondary-pastel-blue);
  transform: translateY(-3px);
  box-shadow: var(--shadow);
}

.course-name {
  font-weight: bold;
  color: var(--text-primary);
  margin-bottom: 8px;
  font-size: 16px;
}

.course-time, .course-classroom {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 4px;
  display: flex;
  align-items: center;
}

.course-time::before {
  content: '⏰';
  margin-right: 6px;
  font-size: 12px;
}

.course-classroom::before {
  content: '🏫';
  margin-right: 6px;
  font-size: 12px;
}

.no-courses {
  padding: 24px;
  text-align: center;
  color: var(--text-primary);
  background-color: var(--secondary-pastel-blue);
  border-radius: var(--border-radius);
  font-weight: 500;
}

.data-visualization {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.chart-card {
  background-color: var(--card-background);
  border-radius: var(--border-radius);
  padding: 24px;
  box-shadow: var(--shadow);
  transition: var(--transition);
  position: relative;
  overflow: hidden;
}

.chart-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-hover);
}

.chart-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background-color: var(--primary-mint);
}

.chart-card h3 {
  font-size: 16px;
  font-weight: 500;
  color: var(--text-primary);
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  padding-left: 12px;
}

.chart-card:nth-child(1) h3::before {
  content: '😴';
  margin-right: 8px;
  font-size: 14px;
  background-color: var(--secondary-pastel-purple);
  padding: 6px;
  border-radius: var(--border-radius-pill);
}

.chart-card:nth-child(2) h3::before {
  content: '📊';
  margin-right: 8px;
  font-size: 14px;
  background-color: var(--secondary-pastel-blue);
  padding: 6px;
  border-radius: var(--border-radius-pill);
}

.chart-card:nth-child(3) h3::before {
  content: '🎯';
  margin-right: 8px;
  font-size: 14px;
  background-color: var(--secondary-pastel-orange);
  padding: 6px;
  border-radius: var(--border-radius-pill);
}

.chart-card:nth-child(4) h3::before {
  content: '📅';
  margin-right: 8px;
  font-size: 14px;
  background-color: var(--secondary-pastel-green);
  padding: 6px;
  border-radius: var(--border-radius-pill);
}

.chart-card:nth-child(5) h3::before {
  content: '📱';
  margin-right: 8px;
  font-size: 14px;
  background-color: var(--secondary-pastel-orange);
  padding: 6px;
  border-radius: var(--border-radius-pill);
}

.chart-container {
  height: 300px;
  position: relative;
  margin-top: 12px;
}

@media (max-width: 768px) {
  .health-dashboard {
    padding: 16px;
  }
  
  .dashboard-title {
    font-size: 20px;
    padding: 15px 0;
  }
  
  .reminder-completion, .today-courses, .chart-card {
    padding: 15px;
  }
  
  .chart-container {
    height: 200px;
  }
  
  .chart-card h3 {
    font-size: 14px;
  }
  
  .chart-card h3::before {
    font-size: 12px;
    padding: 4px;
  }
}
</style>