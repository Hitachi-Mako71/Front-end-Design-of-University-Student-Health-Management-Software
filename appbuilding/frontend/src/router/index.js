import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/user',
    name: 'User',
    component: () => import('../views/User.vue')
  },
  {
    path: '/schedule',
    name: 'Schedule',
    component: () => import('../views/Schedule.vue')
  },
  {
    path: '/sleep',
    name: 'Sleep',
    component: () => import('../views/Sleep.vue')
  },
  {
    path: '/reminders',
    name: 'Reminders',
    component: () => import('../views/Reminders.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router