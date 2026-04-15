<template>
  <div class="schedule">
    <h1>课表管理</h1>
    
    <!-- 学期选择 -->
    <div class="semester-selector">
      <label>学期：</label>
      <input v-model="semesterLabel" type="text" placeholder="如 2026-Spring">
    </div>
    
    <!-- 可视化课表 -->
    <div class="timetable-container">
      <h2>课表定制</h2>
      <div class="timetable">
        <div class="timetable-header">
          <div class="time-slot">时间</div>
          <div class="day-slot" v-for="day in weekDays" :key="day.value">
            {{ day.label }}
          </div>
        </div>
        <div class="timetable-body">
          <div v-for="period in periods" :key="period.value" class="time-row">
            <div class="time-slot">{{ period.label }}</div>
            <div 
              v-for="day in weekDays" 
              :key="`${period.value}-${day.value}`"
              class="course-slot"
              @click="openCourseForm(period.value, day.value)"
            >
              <!-- 显示已添加的课程 -->
              <div 
                v-for="course in getCoursesAtSlot(period.value, day.value)" 
                :key="course.id"
                class="course-item"
                @click.stop="editCourse(course)"
              >
                {{ course.courseName }}
                <button class="delete-course" @click.stop="deleteCourse(course.id)">×</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 课程表单 -->
    <div v-if="showCourseForm" class="course-form-overlay" @click="closeCourseForm">
      <div class="course-form" @click.stop>
        <h3>{{ editingCourse ? '编辑课程' : '添加课程' }}</h3>
        <div class="form-group">
          <label>课程名称：</label>
          <input v-model="currentCourse.courseName" type="text" placeholder="请输入课程名称">
        </div>
        <div class="form-group">
          <label>教室：</label>
          <input v-model="currentCourse.classroom" type="text" placeholder="请输入教室">
        </div>
        <div class="form-group">
          <label>开始周：</label>
          <input v-model="currentCourse.weekStart" type="number" placeholder="如 1">
        </div>
        <div class="form-group">
          <label>结束周：</label>
          <input v-model="currentCourse.weekEnd" type="number" placeholder="如 18">
        </div>
        <div class="form-group">
          <label>教学楼纬度：</label>
          <input v-model="currentCourse.buildingLat" type="number" step="0.0001" placeholder="如 25.0521">
        </div>
        <div class="form-group">
          <label>教学楼经度：</label>
          <input v-model="currentCourse.buildingLng" type="number" step="0.0001" placeholder="如 102.7183">
        </div>
        <div class="form-actions">
          <button @click="saveCourse">保存</button>
          <button @click="closeCourseForm">取消</button>
        </div>
      </div>
    </div>
    
    <!-- 操作按钮 -->
    <div class="action-buttons">
      <button @click="submitSchedule" class="primary-button">提交课表</button>
      <button @click="clearSchedule" class="secondary-button">清空课表</button>
    </div>
    
    <!-- 今日课程 -->
    <div class="today-courses">
      <h2>今日课程</h2>
      <ul v-if="todayCourses.length > 0">
        <li v-for="course in todayCourses" :key="course.courseId">
          <div class="course-info">
            <span class="course-name">{{ course.courseName }}</span>
            <span class="course-time">{{ course.classStartTime }}</span>
            <span class="course-classroom">{{ course.classroom }}</span>
          </div>
          <button @click="saveRoute(course.courseId)">保存路线</button>
        </li>
      </ul>
      <p v-else>今天没有课程</p>
    </div>
  </div>
</template>

<script>
import { importSchedule, getTodayCourses, saveRoute } from '../services/api'

export default {
  /**
   * 课表管理页面组件
   * 提供可视化课表定制功能，支持添加、编辑、删除课程，以及提交课表和查看今日课程
   */
  data() {
    return {
      semesterLabel: '2026-Spring', // 学期标签
      courses: [], // 课程列表
      todayCourses: [], // 今日课程
      userId: 'user_123', // 临时用户ID，实际应该从本地存储获取
      showCourseForm: false, // 是否显示课程表单
      currentCourse: {
        id: null, // 课程ID
        courseName: '', // 课程名称
        weekday: 1, // 星期
        weekStart: 1, // 开始周
        weekEnd: 18, // 结束周
        periodStart: 1, // 开始节
        periodEnd: 1, // 结束节
        classroom: '', // 教室
        buildingLat: 25.0521, // 教学楼纬度
        buildingLng: 102.7183 // 教学楼经度
      },
      editingCourse: false, // 是否处于编辑模式
      weekDays: [ // 星期列表
        { value: 1, label: '周一' },
        { value: 2, label: '周二' },
        { value: 3, label: '周三' },
        { value: 4, label: '周四' },
        { value: 5, label: '周五' },
        { value: 6, label: '周六' },
        { value: 7, label: '周日' }
      ],
      periods: [ // 节次列表
        { value: 1, label: '1-2节' },
        { value: 3, label: '3-4节' },
        { value: 5, label: '5-6节' },
        { value: 7, label: '7-8节' },
        { value: 9, label: '9-10节' }
      ]
    }
  },
  mounted() {
    this.loadTodayCourses()
  },
  methods: {
    // 加载今日课程
    async loadTodayCourses() {
      try {
        this.todayCourses = await getTodayCourses(this.userId)
      } catch (error) {
        console.error('获取今日课程失败:', error)
      }
    },
    
    // 打开课程表单
    openCourseForm(period, weekday) {
      this.editingCourse = false
      this.currentCourse = {
        id: null,
        courseName: '',
        weekday: weekday,
        weekStart: 1,
        weekEnd: 18,
        periodStart: period,
        periodEnd: period + 1,
        classroom: '',
        buildingLat: 25.0521,
        buildingLng: 102.7183
      }
      this.showCourseForm = true
    },
    
    // 编辑课程
    editCourse(course) {
      this.editingCourse = true
      this.currentCourse = { ...course }
      this.showCourseForm = true
    },
    
    // 关闭课程表单
    closeCourseForm() {
      this.showCourseForm = false
    },
    
    // 保存课程
    saveCourse() {
      if (!this.currentCourse.courseName || !this.currentCourse.classroom) {
        alert('请填写课程名称和教室')
        return
      }
      
      if (this.editingCourse) {
        // 更新现有课程
        const index = this.courses.findIndex(c => c.id === this.currentCourse.id)
        if (index !== -1) {
          this.courses[index] = { ...this.currentCourse }
        }
      } else {
        // 添加新课程
        this.courses.push({
          ...this.currentCourse,
          id: Date.now().toString()
        })
      }
      
      this.showCourseForm = false
    },
    
    // 删除课程
    deleteCourse(id) {
      this.courses = this.courses.filter(c => c.id !== id)
    },
    
    // 获取指定时间段的课程
    getCoursesAtSlot(period, weekday) {
      return this.courses.filter(course => 
        course.periodStart <= period && course.periodEnd >= period && 
        course.weekday === weekday
      )
    },
    
    // 提交课表
    async submitSchedule() {
      if (this.courses.length === 0) {
        alert('请至少添加一门课程')
        return
      }
      
      try {
        // 转换为后端需要的格式
        const formattedCourses = this.courses.map(course => ({
          courseName: course.courseName,
          weekday: course.weekday,
          weekStart: course.weekStart,
          weekEnd: course.weekEnd,
          periodStart: course.periodStart,
          periodEnd: course.periodEnd,
          classroom: course.classroom,
          buildingLat: course.buildingLat,
          buildingLng: course.buildingLng
        }))
        
        await importSchedule(this.userId, this.semesterLabel, formattedCourses)
        alert('课表导入成功！')
      } catch (error) {
        console.error('导入课表失败:', error)
        alert('导入课表失败，请重试')
      }
    },
    
    // 清空课表
    clearSchedule() {
      if (confirm('确定要清空课表吗？')) {
        this.courses = []
      }
    },
    
    // 保存路线
    async saveRoute(courseId) {
      try {
        // 模拟路线点数据
        const routePoints = [
          {
            pointType: 'crossroads',
            lat: 25.0530,
            lng: 102.7190,
            label: '体育馆路口',
            alertMessage: '注意过马路，别看手机！'
          }
        ]
        await saveRoute(courseId, routePoints)
        alert('路线保存成功！')
      } catch (error) {
        console.error('保存路线失败:', error)
        alert('保存路线失败，请重试')
      }
    }
  }
}
</script>

<style scoped>
.schedule {
  padding: 20px;
}

.semester-selector {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  background: var(--card-background);
  padding: 15px;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
}

.semester-selector label {
  font-weight: 500;
  color: var(--text-primary);
}

.semester-selector input {
  padding: 8px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  width: 200px;
  transition: var(--transition);
  background-color: var(--background-color);
  color: var(--text-primary);
}

.semester-selector input:focus {
  outline: none;
  border-color: var(--primary-mint);
  box-shadow: 0 0 0 3px rgba(126, 216, 208, 0.1);
}

.timetable-container {
  background: var(--card-background);
  border-radius: var(--border-radius);
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.timetable-container:hover {
  box-shadow: var(--shadow-hover);
}

.timetable {
  width: 100%;
  border-collapse: collapse;
  overflow-x: auto;
  display: block;
}

.timetable-header {
  display: flex;
  border-bottom: 2px solid var(--border-color);
  background-color: #f9f9f9;
}

.timetable-body {
  display: flex;
  flex-direction: column;
}

.time-row {
  display: flex;
  border-bottom: 1px solid var(--border-color);
  transition: var(--transition);
}

.time-row:hover {
  background-color: #f9f9f9;
}

.time-slot {
  width: 80px;
  padding: 10px;
  border-right: 1px solid var(--border-color);
  background-color: #f5f5f5;
  font-weight: bold;
  text-align: center;
  white-space: nowrap;
}

.day-slot {
  flex: 1;
  padding: 10px;
  border-right: 1px solid var(--border-color);
  text-align: center;
  font-weight: bold;
  color: var(--text-primary);
}

.course-slot {
  flex: 1;
  min-height: 80px;
  padding: 5px;
  border-right: 1px solid var(--border-color);
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
  transition: var(--transition);
  position: relative;
}

.course-slot:hover {
  background-color: #f0f0f0;
  transform: scale(1.02);
}

.course-item {
  background-color: var(--primary-mint-light);
  color: var(--text-primary);
  padding: 8px;
  border-radius: var(--border-radius);
  margin-bottom: 5px;
  font-size: 12px;
  position: relative;
  cursor: pointer;
  transition: var(--transition);
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  border-left: 3px solid var(--primary-mint);
}

.course-item:hover {
  background-color: var(--primary-mint);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.delete-course {
  position: absolute;
  top: 2px;
  right: 2px;
  background: rgba(255,255,255,0.2);
  border: none;
  color: white;
  font-size: 14px;
  cursor: pointer;
  padding: 0;
  width: 16px;
  height: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: var(--transition);
}

.delete-course:hover {
  background: rgba(255,255,255,0.4);
  transform: scale(1.1);
}

.course-form-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
}

.course-form {
  background: var(--card-background);
  padding: 25px;
  border-radius: var(--border-radius);
  width: 400px;
  max-width: 90%;
  box-shadow: 0 10px 30px rgba(0,0,0,0.2);
  animation: slideIn 0.3s ease;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-group {
  margin: 15px 0;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-primary);
}

.form-group input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius);
  transition: var(--transition);
  background-color: var(--background-color);
  color: var(--text-primary);
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-mint);
  box-shadow: 0 0 0 3px rgba(126, 216, 208, 0.1);
}

.form-actions {
  display: flex;
  gap: 10px;
  margin-top: 25px;
  justify-content: flex-end;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

button {
  background: var(--primary-mint);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: var(--border-radius);
  cursor: pointer;
  font-weight: 500;
  transition: var(--transition);
}

button:hover {
  background: var(--primary-mint-dark);
  transform: translateY(-2px);
  box-shadow: var(--shadow);
}

button:active {
  transform: translateY(0);
}

.secondary-button {
  background: #f44336;
}

.secondary-button:hover {
  background: #da190b;
}

.today-courses {
  background: var(--card-background);
  border-radius: var(--border-radius);
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--shadow);
  transition: var(--transition);
}

.today-courses:hover {
  box-shadow: var(--shadow-hover);
}

.course-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 15px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: var(--border-radius);
  transition: var(--transition);
}

.course-info:hover {
  background-color: #f0f0f0;
  transform: translateX(5px);
}

.course-name {
  font-weight: bold;
  color: var(--text-primary);
  font-size: 16px;
}

.course-time, .course-classroom {
  font-size: 14px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .schedule {
    padding: 10px;
  }
  
  .timetable {
    font-size: 12px;
  }
  
  .time-slot {
    width: 60px;
    padding: 5px;
  }
  
  .day-slot {
    padding: 5px;
  }
  
  .course-slot {
    min-height: 60px;
    padding: 3px;
  }
  
  .course-item {
    font-size: 10px;
    padding: 5px;
  }
  
  .course-form {
    padding: 20px;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  button {
    width: 100%;
  }
}
</style>