import axios from 'axios'

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
 * 用户管理相关API
 */

/**
 * 创建用户
 * @param {string} nickname - 用户名
 * @returns {Promise<Object>} 用户信息
 */
export const createUser = (nickname) => {
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
  return api.get('/getTodayCourses', { params: { userId } })
}

/**
 * 保存路线
 * @param {string} courseId - 课程ID
 * @param {Array} routePoints - 路线点列表
 * @returns {Promise<Object>} 路线保存结果
 */
export const saveRoute = (courseId, routePoints) => {
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
  return api.get('/getNavTime', { params: { originLat, originLng, destLat, destLng } })
}

/**
 * 获取明天天气
 * @param {string} city - 城市名称
 * @returns {Promise<Object>} 天气结果
 */
export const getTomorrowWeather = (city) => {
  return api.get('/getTomorrowWeather', { params: { city } })
}

/**
 * 获取AI健康提示
 * @param {number} lateNightScore - 熬夜评分
 * @param {Object} profile - 健康档案
 * @returns {Promise<Object>} AI健康提示结果
 */
export const getAiHealthTip = (lateNightScore, profile) => {
  return api.post('/getAiHealthTip', profile, {
    params: { lateNightScore }
  })
}

export default api