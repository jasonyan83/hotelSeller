import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home/HomePage.vue'),
    meta: { title: '酒店频道', keepAlive: true }
  },
  {
    path: '/destination',
    name: 'Destination',
    component: () => import('@/views/Destination/DestPage.vue'),
    meta: { title: '选择目的地' }
  },
  {
    path: '/date-select',
    name: 'DateSelect',
    component: () => import('@/views/DateSelect/DateSelectPage.vue'),
    meta: { title: '选择日期' }
  },
  {
    path: '/keyword-select',
    name: 'KeywordSelect',
    component: () => import('@/views/KeywordSelect/KeywordSelectPage.vue'),
    meta: { title: '关键字搜索' }
  },
  {
    path: '/hotels',
    name: 'HotelList',
    component: () => import('@/views/HotelList/HotelListPage.vue'),
    meta: { title: '酒店列表', keepAlive: true }
  },
  {
    path: '/hotels/:hotelId',
    name: 'HotelDetail',
    component: () => import('@/views/HotelDetail/HotelDetailPage.vue'),
    meta: { title: '酒店详情' }
  },
  {
    path: '/booking',
    name: 'Booking',
    component: () => import('@/views/Booking/BookingPage.vue'),
    meta: { title: '信息填写' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
