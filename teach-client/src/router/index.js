import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login'
import AdminProfile from "@/views/dashboard/AdminProfile";
import Navi from '@/components/Navi'

const routes = [
  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: Login
  },
  {
    path: '/Register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },{
  path: '/',
  component: Navi,
  children: [
    {
      path: '',
      name: 'Home',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ '@/views/Home.vue')
    },


    {
      path: '/RightManage',
      name: 'RightManage',
      component: () => import('@/views/dashboard/RightManage.vue')
    },
    {
      path: '/UserLog',
      name: 'UserLog',
      component: () => import('@/views/dashboard/UserLog.vue')
    },
    {
      path: '/SearchOrder',
      name: 'SearchOrder',
      component: () => import('@/views/dashboard/SearchOrder.vue')
    },
    {
      path: '/AdminProfile',
      name: 'AdminProfile',
      component: AdminProfile
    },
    {
      path: '/MemberProfile',
      name: 'MemberProfile',
      component: () => import('@/views/dashboard/MemberProfile.vue')
    }]
}]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
