import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'
import Cookies from "js-cookie";

Vue.use(VueRouter)

const routes = [
    //=======login=========
  {
    path: '/login',
    name: 'Login',
    component:() => import('@/views/login/Login.vue'),
  },
    //=======主页=========
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/home',//重定向
    children: [//子路由
      {
        path: 'home',
        name: 'Home',
        component:() => import('@/views/home/HomeView.vue'),
      },
    {
      path: 'userList',
      name: 'UserList',
      component:() => import('@/views/user/User.vue'),
    },
      //=======User=========
      {
        path: 'user/add',
        name: 'UserAdd',
        component:() => import('@/views/user/Add.vue'),
      },
      {
        path: 'user/edit',
        name: 'UserEdit',
        component:() => import('@/views/user/Edit.vue'),
      },
      //=======Admin=========
      {
        path: 'adminList',
        name: 'AdminList',
        component:() => import('@/views/admin/Admin.vue'),
      },
      {
        path: 'admin/add',
        name: 'AdminAdd',
        component:() => import('@/views/admin/Add.vue'),
      },
      {
        path: 'admin/edit',
        name: 'AdminEdit',
        component:() => import('@/views/admin/Edit.vue'),
      },
      //--------Category---------
      {
        path: '/categoryList',
        name: 'CategoryList',
        component:() => import('@/views/category/Category.vue'),
      },
      {
        path: '/category/add',
        name: 'CategoryAdd',
        component:() => import('@/views/category/Add.vue'),
      },
      {
        path: '/category/edit',
        name: 'CategoryEdit',
        component:() => import('@/views/category/Edit.vue'),
      },
        //--------Book---------
         {
           path: '/bookList',
           name: 'BookList',
           component:() => import('@/views/book/Book.vue'),
         },
         {
           path: '/book/add',
           name: 'BookAdd',
           component:() => import('@/views/book/Add.vue'),
         },
         {
           path: '/book/edit',
           name: 'BookEdit',
           component:() => import('@/views/book/Edit.vue'),
         },
        //--------Borrow---------
         {
           path: '/borrowList',
           name: 'BorrowList',
           component:() => import('@/views/borrow/Borrow.vue'),
         },
         {
           path: '/borrow/add',
           name: 'BorrowAdd',
           component:() => import('@/views/borrow/Add.vue'),
         },
         {
           path: '/borrow/edit',
           name: 'BorrowEdit',
           component:() => import('@/views/borrow/Edit.vue'),
         },
        //--------Return---------
         {
           path: '/returList',
           name: 'ReturList',
           component:() => import('@/views/retur/Retur.vue'),
         }

    ]
  },
  {
    path: '*',
    name: '404',
    component:() => import('@/views/404.vue'),
  }
]


const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
//路由守卫

router.beforeEach((to, from, next) => {//to 目标路由对象，to.path 是要跳转到的页面路径,from 来源路由对象，from.path 是当前所在的页面路径,next 路由放行方法：调用后才会跳转到目标页面
  if (to.path === '/login') return  next();//如果目标页面是登录页（/login），直接放行（next()）
  const admin=Cookies.get('admin')//从浏览器Cookie中读取名为"admin"的值
  if (!admin && to.path !== '/login')
    return  next('/login')//未登录（admin为空） + 目标页面不是登录页 → 强制跳登录页
  next()//已登录（有admin Cookie），或目标页是登录页 → 允许访问目标页面
})

export default router
