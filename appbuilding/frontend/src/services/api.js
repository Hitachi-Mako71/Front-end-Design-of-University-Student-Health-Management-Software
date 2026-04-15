import axios from 'axios'

// 调试模式标志，设置为 true 时使用虚拟数据
const DEBUG_MODE = true

/**
 * API服务配置
 * 创建axios实例，设置基础URL和超时时间
 */
const api = axios.create({
  baseURL: 'http://localhost:8080', // 后端服务地址
  timeout: 10000 // 超时时间（毫秒）
})

/**
 * 统一处理响应
 * 对API响应进行拦截，处理成功和失败的情况
 */
api.interceptors.response.use(
  response => {
    if (response.data.code === 0) {
      return response.data.data // 成功时返回数据
    } else {
      throw new Error(response.data.msg) // 失败时抛出错误
    }
  },
  error => {
    console.error('API请求失败:', error)
    throw error
  }
)

/**
 * 虚拟数据
 */
const mockData = {
  // 用户相关
  user: {
    userId: 'user_123',
    nickname: '测试用户',
    healthProfile: {
      height: 170,
      weight: 60,
      sleepGoal: 8,
      exerciseGoal: 30
    }
  },
  // 课程相关
  courses: [
    {
      course_id: 'course_1',
      course_name: '高等数学',
      class_start_time: '08:00',
      period_start: 1,
      period_end: 2,
      classroom: '教学楼A101'
    },
    {
      course_id: 'course_2',
      course_name: '大学英语',
      class_start_time: '10:00',
      period_start: 3,
      period_end: 4,
      classroom: '教学楼B202'
    }
  ],
  // 睡眠记录
  sleepRecord: {
    recordId: '1',
    date: new Date().toISOString().split('T')[0],
    lateNightScore: 75,
    sleepTs: Date.now() - 3600000 * 7,
    awakeDurationMs: 3600000 * 2,
    sleepQuality: 3
  },
  // 屏幕使用汇总
  screenSummary: {
    date: new Date().toISOString().split('T')[0],
    totalMs: 3600000 * 5,
    after23Ms: 3600000 * 1,
    sessionCount: 20
  },
  // 提醒
  reminders: [
    {
      planId: 'plan_1',
      planType: '睡眠',
      message: '该睡觉了，保持良好的作息习惯',
      priority: 2,
      triggerTs: Date.now() + 3600000 // 1小时后
    },
    {
      planId: 'plan_2',
      planType: '运动',
      message: '记得做一些运动，保持身体健康',
      priority: 1,
      triggerTs: Date.now() + 7200000 // 2小时后
    }
  ]
}

/**
 * 用户管理相关API
 */

/**
 * 创建用户
 * @param {string} nickname - 用户名
 * @returns {Promise<Object>} 用户信息
 */
export const createUser = (nickname) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      userId: 'user_123',
      nickname: nickname || '测试用户'
    })
  }
  return api.post('/createUser', null, {
    params: { nickname }
  })
}

/**
 * 提交健康档案
 * @param {string} userId - 用户ID
 * @param {Object} profileData - 健康档案数据
 * @returns {Promise<Object>} 健康档案信息
 */
export const submitHealthProfile = (userId, profileData) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      ...mockData.user.healthProfile,
      ...profileData
    })
  }
  return api.post('/submitHealthProfile', profileData, {
    params: { userId }
  })
}

/**
 * 获取健康档案
 * @param {string} userId - 用户ID
 * @returns {Promise<Object>} 健康档案信息
 */
export const getHealthProfile = (userId) => {
  if (DEBUG_MODE) {
    return Promise.resolve(mockData.user.healthProfile)
  }
  return api.get('/getHealthProfile', { params: { userId } })
}

/**
 * 课表管理相关API
 */

/**
 * 导入课表
 * @param {string} userId - 用户ID
 * @param {string} semesterLabel - 学期标签
 * @param {Array} courses - 课程列表
 * @returns {Promise<Object>} 课表导入结果
 */
export const importSchedule = (userId, semesterLabel, courses) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      success: true,
      message: '课表导入成功',
      count: courses.length
    })
  }
  return api.post('/importSchedule', courses, {
    params: { userId, semesterLabel }
  })
}

/**
 * 获取今日课程
 * @param {string} userId - 用户ID
 * @returns {Promise<Array>} 今日课程列表
 */
export const getTodayCourses = (userId) => {
  if (DEBUG_MODE) {
    return Promise.resolve(mockData.courses)
  }
  return api.get('/getTodayCourses', { params: { userId } })
}

/**
 * 保存路线
 * @param {string} courseId - 课程ID
 * @param {Array} routePoints - 路线点列表
 * @returns {Promise<Object>} 路线保存结果
 */
export const saveRoute = (courseId, routePoints) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      success: true,
      message: '路线保存成功',
      routeId: 'route_1'
    })
  }
  return api.post('/saveRoute', routePoints, {
    params: { courseId }
  })
}

/**
 * 获取带警示的路线
 * @param {string} courseId - 课程ID
 * @returns {Promise<Array>} 带警示的路线点列表
 */
export const getRouteWithAlerts = (courseId) => {
  if (DEBUG_MODE) {
    return Promise.resolve([
      {
        lat: 39.9042,
        lng: 116.4074,
        alert: false
      },
      {
        lat: 39.9052,
        lng: 116.4084,
        alert: true,
        alertType: 'traffic'
      },
      {
        lat: 39.9062,
        lng: 116.4094,
        alert: false
      }
    ])
  }
  return api.get('/getRouteWithAlerts', { params: { courseId } })
}

/**
 * 数据采集相关API
 */

/**
 * 记录亮屏事件
 * @param {string} userId - 用户ID
 * @returns {Promise<Object>} 屏幕会话信息
 */
export const recordScreenOn = (userId) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      sessionId: 'session_1',
      startTime: Date.now(),
      userId: userId
    })
  }
  return api.post('/recordScreenOn', null, {
    params: { userId }
  })
}

/**
 * 记录灭屏事件
 * @param {string} userId - 用户ID
 * @param {string} sessionId - 会话ID
 * @returns {Promise<Object>} 屏幕会话信息
 */
export const recordScreenOff = (userId, sessionId) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      sessionId: sessionId,
      startTime: Date.now() - 3600000,
      endTime: Date.now(),
      durationMs: 3600000
    })
  }
  return api.post('/recordScreenOff', null, {
    params: { userId, sessionId }
  })
}

/**
 * 获取每日屏幕使用汇总
 * @param {string} userId - 用户ID
 * @param {string} date - 日期
 * @returns {Promise<Object>} 屏幕使用汇总信息
 */
export const getDailyScreenSummary = (userId, date) => {
  if (DEBUG_MODE) {
    return Promise.resolve(mockData.screenSummary)
  }
  return api.get('/getDailyScreenSummary', { params: { userId, date } })
}

/**
 * 睡眠分析相关API
 */

/**
 * 更新熬夜评分
 * @param {string} userId - 用户ID
 * @param {string} date - 日期
 * @returns {Promise<Object>} 睡眠记录信息
 */
export const updateNightScore = (userId, date) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      ...mockData.sleepRecord,
      lateNightScore: Math.floor(Math.random() * 100)
    })
  }
  return api.post('/updateNightScore', null, {
    params: { userId, date }
  })
}

/**
 * 获取睡眠记录
 * @param {string} userId - 用户ID
 * @param {string} date - 日期
 * @returns {Promise<Object>} 睡眠记录信息
 */
export const getSleepRecord = (userId, date) => {
  if (DEBUG_MODE) {
    return Promise.resolve(mockData.sleepRecord)
  }
  return api.get('/getSleepRecord', { params: { userId, date } })
}

/**
 * 提交睡眠质量评价
 * @param {string} userId - 用户ID
 * @param {string} date - 日期
 * @param {number} quality - 睡眠质量评分
 * @returns {Promise<Object>} 操作结果
 */
export const submitSleepQuality = (userId, date, quality) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      success: true,
      message: '睡眠质量评价提交成功',
      quality: quality
    })
  }
  return api.post('/submitSleepQuality', null, {
    params: { userId, date, quality }
  })
}

/**
 * 提醒调度相关API
 */

/**
 * 生成每日提醒计划
 * @param {string} userId - 用户ID
 * @param {string} targetDate - 目标日期
 * @returns {Promise<Object>} 提醒计划结果
 */
export const generateDailyPlan = (userId, targetDate) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      success: true,
      message: '提醒计划生成成功',
      plans: mockData.reminders
    })
  }
  return api.post('/generateDailyPlan', null, {
    params: { userId, targetDate }
  })
}

/**
 * 获取待处理提醒
 * @param {string} userId - 用户ID
 * @returns {Promise<Array>} 待处理提醒列表
 */
export const getPendingReminders = (userId) => {
  if (DEBUG_MODE) {
    return Promise.resolve(mockData.reminders)
  }
  return api.get('/getPendingReminders', { params: { userId } })
}

/**
 * 响应提醒
 * @param {string} planId - 计划ID
 * @param {string} action - 操作类型
 * @param {number} snoozeMin - 推迟分钟数（可选）
 * @returns {Promise<Object>} 响应结果
 */
export const ackReminder = (planId, action, snoozeMin) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      success: true,
      message: '提醒响应成功',
      action: action
    })
  }
  return api.post('/ackReminder', null, {
    params: { planId, action, snoozeMin }
  })
}

/**
 * 外部API相关
 */

/**
 * 获取导航时间
 * @param {number} originLat - 起点纬度
 * @param {number} originLng - 起点经度
 * @param {number} destLat - 终点纬度
 * @param {number} destLng - 终点经度
 * @returns {Promise<Object>} 导航时间结果
 */
export const getNavTime = (originLat, originLng, destLat, destLng) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      duration: 15, // 分钟
      distance: 2.5, // 公里
      status: 'OK'
    })
  }
  return api.get('/getNavTime', { params: { originLat, originLng, destLat, destLng } })
}

/**
 * 获取明天天气
 * @param {string} city - 城市名称
 * @returns {Promise<Object>} 天气结果
 */
export const getTomorrowWeather = (city) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      city: city,
      temperature: '18-25°C',
      weather: '晴',
      wind: '微风',
      status: 'OK'
    })
  }
  return api.get('/getTomorrowWeather', { params: { city } })
}

/**
 * 获取AI健康提示
 * @param {number} lateNightScore - 熬夜评分
 * @param {Object} profile - 健康档案
 * @returns {Promise<Object>} AI健康提示结果
 */
export const getAiHealthTip = (lateNightScore, profile) => {
  if (DEBUG_MODE) {
    return Promise.resolve({
      tip: lateNightScore > 70 
        ? '您的熬夜评分较高，建议保持规律作息，避免熬夜，保证充足睡眠。' 
        : '您的熬夜评分良好，继续保持健康的作息习惯。',
      status: 'OK'
    })
  }
  return api.post('/getAiHealthTip', profile, {
    params: { lateNightScore }
  })
}

export default api