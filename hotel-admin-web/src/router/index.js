import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layout/AdminLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardPage.vue'),
        meta: { title: '控制台', icon: 'DataAnalysis' },
      },
      {
        path: 'suppliers',
        name: 'SupplierList',
        component: () => import('@/views/supplier/SupplierList.vue'),
        meta: { title: '供应商管理', icon: 'OfficeBuilding' },
      },
      {
        path: 'suppliers/add',
        name: 'SupplierAdd',
        component: () => import('@/views/supplier/SupplierForm.vue'),
        meta: { title: '新增供应商', hidden: true },
      },
      {
        path: 'suppliers/:id/edit',
        name: 'SupplierEdit',
        component: () => import('@/views/supplier/SupplierForm.vue'),
        meta: { title: '编辑供应商', hidden: true },
      },
      {
        path: 'suppliers/:id/detail',
        name: 'SupplierDetail',
        component: () => import('@/views/supplier/SupplierDetail.vue'),
        meta: { title: '供应商详情', hidden: true },
      },
      {
        path: 'suppliers/:id/price-strategy',
        name: 'PriceStrategy',
        component: () => import('@/views/price-strategy/PriceStrategyPage.vue'),
        meta: { title: '价格策略', hidden: true },
      },
      {
        path: 'recommended',
        name: 'Recommended',
        component: () => import('@/views/recommended/RecommendedList.vue'),
        meta: { title: '推荐酒店', icon: 'Star' },
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/views/log/LogList.vue'),
        meta: { title: '操作日志', icon: 'Document' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
