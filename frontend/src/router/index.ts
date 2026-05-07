import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/login',
    component: () => import('../views/login/index.vue'),
  },
  {
    path: '/',
    component: () => import('../layout/BasicLayout.vue'),
    children: [
      {
        path: 'dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: {
          title: '系统首页',
        },
      },
      {
        path: 'system/user',
        component: () => import('../views/system/user/index.vue'),
        meta: {
          title: '用户管理',
        },
      },
      {
        path: 'system/role',
        component: () => import('../views/system/role/index.vue'),
        meta: {
          title: '角色管理',
        },
      },
      {
        path: 'system/menu',
        component: () => import('../views/system/menu/index.vue'),
        meta: {
          title: '菜单管理',
        },
      },
      {
        path: 'system/config',
        component: () => import('../views/system/config/index.vue'),
        meta: {
          title: '系统配置',
        },
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;