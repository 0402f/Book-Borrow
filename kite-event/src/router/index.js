import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: () => import('@/views/Login/Login.vue')
    },
    // 架子
    {
      path: '/',
      component: () => import('@/views/LayOut/LayoutContainer.vue'),
      redirect: '/index',
      children: [
        {
          path: '/index',
          component: () => import('@/views/index.vue')
        },
        {
          path: '/UserManagement',
          component: () => import('@/views/UserPanel/UserManGement.vue')
        },
        {
          path: '/ClassManagement',
          component: () => import('@/views/ClassPanel/ClassManGement.vue')
        },
        {
          path: '/BookManagement',
          component: () => import('@/views/BookPanel/BookManGement.vue')
        },
        {
          path: '/DownBookManagement',
          component: () => import('@/views/BookPanel/DownBookManGement.vue')
        },
        {
          path: '/BorrowManagement',
          component: () => import('@/views/BorrowingRecords/BorowRec.vue')
        },
        {
          path: '/CellWe',
          component: () => import('@/views/CellWe/CellWe.vue')
        }
      ]
    }
  ]
})

// 登录访问拦截  => 默认直接放行
// 根据返回值进行放行
// 返回值:
// 1.undefined: 直接放行
// 2.false: 拦回 from 地址
// 3.具体路径 或 路径对象 拦截到对应的地址
router.beforeEach((to, from) => {
  const useStore = useUserStore()
  if (!useStore.token && to.path !== '/login') {
    return '/login'
  }
  // 其他情况放行
  return true
})

export default router
