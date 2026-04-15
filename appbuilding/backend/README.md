# 大学生健康守护 App - 后端

## 项目概述

大学生健康守护 App 后端是一个基于 Spring Boot 的 RESTful API 服务，为前端应用提供数据存储和业务逻辑处理。后端使用 Java 语言开发，采用 JPA 进行数据持久化，使用 SQLite 作为数据库。

## 技术栈

- **框架**：Spring Boot 2.7.0+
- **语言**：Java 11+
- **持久化**：Spring Data JPA
- **数据库**：SQLite
- **依赖管理**：Maven

## 项目结构

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/healthguard/
│   │   │   ├── controller/    # 控制器
│   │   │   │   └── MainController.java  # 主控制器
│   │   │   ├── model/         # 模型类
│   │   │   │   ├── User.java  # 用户模型
│   │   │   │   ├── HealthProfile.java  # 健康档案模型
│   │   │   │   ├── Schedule.java  # 课表模型
│   │   │   │   ├── Course.java  # 课程模型
│   │   │   │   ├── ScreenEvent.java  # 亮屏事件模型
│   │   │   │   ├── SleepRecord.java  # 睡眠记录模型
│   │   │   │   ├── ReminderPlan.java  # 提醒计划模型
│   │   │   │   └── RouteWarning.java  # 路线警示点模型
│   │   │   ├── repository/    # 数据访问层
│   │   │   │   ├── UserRepository.java  # 用户数据访问
│   │   │   │   ├── HealthProfileRepository.java  # 健康档案数据访问
│   │   │   │   ├── ScheduleRepository.java  # 课表数据访问
│   │   │   │   ├── CourseRepository.java  # 课程数据访问
│   │   │   │   ├── ScreenEventRepository.java  # 亮屏事件数据访问
│   │   │   │   ├── SleepRecordRepository.java  # 睡眠记录数据访问
│   │   │   │   ├── ReminderPlanRepository.java  # 提醒计划数据访问
│   │   │   │   └── RouteWarningRepository.java  # 路线警示点数据访问
│   │   │   ├── service/       # 服务层
│   │   │   │   ├── UserService.java  # 用户服务
│   │   │   │   ├── ScheduleService.java  # 课表服务
│   │   │   │   ├── ScreenEventService.java  # 亮屏事件服务
│   │   │   │   ├── SleepService.java  # 睡眠服务
│   │   │   │   └── ReminderService.java  # 提醒服务
│   │   │   ├── config/        # 配置类
│   │   │   │   └── DatabaseConfig.java  # 数据库配置
│   │   │   └── HealthGuardApplication.java  # 应用入口
│   │   └── resources/         # 资源文件
│   │       └── application.properties  # 应用配置
│   └── test/                  # 测试代码
├── pom.xml                    # Maven 配置
└── README.md                  # 项目说明
```

## 安装和运行

### 前提条件

- Java 11+
- Maven 3.6.0+

### 安装依赖

```bash
cd backend
mvn install
```

### 运行应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080/` 运行。

### 构建生产版本

```bash
mvn package
```

构建产物将生成在 `target` 目录。

## API 接口

后端提供了以下 API 接口：

### 1. 用户管理

- `POST /createUser` - 创建用户
- `POST /submitHealthProfile` - 提交健康档案
- `GET /getHealthProfile` - 获取健康档案

### 2. 课表管理

- `POST /importSchedule` - 导入课表
- `GET /getTodayCourses` - 获取今日课程
- `POST /saveRoute` - 保存路线
- `GET /getRouteWarnings` - 获取路线警示点

### 3. 数据采集

- `POST /recordScreenOn` - 记录亮屏事件
- `POST /recordScreenOff` - 记录灭屏事件
- `GET /getScreenTimeSummary` - 获取亮屏时长汇总

### 4. 睡眠分析

- `GET /getStayUpScore` - 计算熬夜评分
- `GET /getSleepRecords` - 获取睡眠记录
- `POST /submitSleepQuality` - 提交睡眠质量评价

### 5. 提醒调度

- `POST /generateDailyPlan` - 生成每日提醒计划
- `GET /getPendingReminders` - 获取待处理提醒
- `POST /ackReminder` - 响应提醒

### 6. 外部 API

- `GET /getNavigationTime` - 获取导航时间
- `GET /getTomorrowWeather` - 获取明天天气
- `GET /getAIHealthTip` - 获取 AI 健康提示

## 数据模型

### User
- `id` - 用户唯一标识
- `name` - 用户名
- `created_at` - 创建时间

### HealthProfile
- `id` - 健康档案唯一标识
- `user_id` - 用户 ID
- `age` - 年龄
- `height` - 身高
- `weight` - 体重
- `blood_type` - 血型
- `allergies` - 过敏史
- `created_at` - 创建时间

### Schedule
- `id` - 课表唯一标识
- `user_id` - 用户 ID
- `semester_label` - 学期标签
- `is_active` - 是否激活
- `created_at` - 创建时间

### Course
- `id` - 课程唯一标识
- `schedule_id` - 课表 ID
- `course_name` - 课程名称
- `weekday` - 星期
- `week_start` - 开始周
- `week_end` - 结束周
- `period_start` - 开始节
- `period_end` - 结束节
- `classroom` - 教室
- `building_lat` - 教学楼纬度
- `building_lng` - 教学楼经度

### ScreenEvent
- `id` - 事件唯一标识
- `user_id` - 用户 ID
- `event_type` - 事件类型（on/off）
- `event_ts` - 事件时间

### SleepRecord
- `id` - 记录唯一标识
- `user_id` - 用户 ID
- `date` - 日期
- `sleep_time` - 入睡时间
- `wake_time` - 起床时间
- `quality` - 睡眠质量
- `score` - 熬夜评分

### ReminderPlan
- `id` - 计划唯一标识
- `user_id` - 用户 ID
- `plan_type` - 计划类型
- `message` - 提醒消息
- `priority` - 优先级
- `trigger_ts` - 触发时间
- `status` - 状态
- `created_at` - 创建时间

### RouteWarning
- `id` - 警示点唯一标识
- `course_id` - 课程 ID
- `location_lat` - 位置纬度
- `location_lng` - 位置经度
- `warning_type` - 警示类型
- `description` - 描述

## 数据库配置

后端使用 SQLite 数据库，数据库文件将在应用启动时自动创建。配置信息在 `application.properties` 文件中定义：

```properties
spring.datasource.url=jdbc:sqlite:healthguard.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```

## 错误处理

后端实现了统一的错误处理机制，所有 API 调用都返回标准的 JSON 格式响应：

```json
{
  "code": 200,
  "message": "success",
  "data": {...}
}
```

错误响应格式：

```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

## 后续优化方向

1. 添加用户认证和授权机制
2. 实现数据库迁移和版本控制
3. 添加单元测试和集成测试
4. 优化数据库查询性能
5. 实现缓存机制
6. 添加 API 文档（如 Swagger）
