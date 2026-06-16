<template>
  <div>
    <!-- 顶部导航栏 -->
    <div style = "height: 60px; line-height: 60px; background-color: white; margin-bottom: 2px;display: flex">
      <div style="width: 300px">
      <img src = "@/assets/logo.png" alt="" style="width: 40px;  position:relative; top: 10px; left:15px;">
      <span style="margin-left: 25px; font-size: 24px">图书管理系统</span>
      </div>
      <div style="flex: 1;text-align: right; padding-right: 50px">
        <el-dropdown size="medium">
          <span class="el-dropdown-link" style="cursor: pointer">
             {{admin.username}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown" style="margin-top: -5px">
            <el-dropdown-item><div style="width: 50px;text-align: center" @click="logout">退出</div></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <!-- 侧边栏和主题-->
    <div style="display: flex;">
      <!-- 侧边栏导航-->
      <div style="width: 200px; min-height:calc(100vh - 82px); overflow: hidden; margin-right: 2px; background-color: white">
        <el-menu
            :default-active="($route.path)" router class="el-menu-demo" style="border-right: none"
        ><!-- :default-openeds="['/']"默认打开的首页，如果想要将用户列表和管理员列表默认打开，则写成 :default-openeds="['user','admin']"  default-openeds存在，则我点击子列表是总列表会关闭-->
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <span>首页</span>
          </el-menu-item>
          <el-submenu index="user" >
            <template slot="title">
              <i class="el-icon-question"></i>
              <span>会员管理</span>
            </template>
            <el-menu-item index="/user/add">会员添加</el-menu-item>
            <el-menu-item index="/userList">会员列表</el-menu-item>
          </el-submenu>
          <el-submenu index="admin" >
            <template slot="title">
              <i class="el-icon-user"></i>
              <span>管理员管理</span>
            </template>
            <el-menu-item index="/admin/add">管理员添加</el-menu-item>
            <el-menu-item index="/adminList">管理员列表</el-menu-item>
          </el-submenu>
          <el-submenu index="category" >
            <template slot="title">
              <i class="el-icon-s-operation"></i>
              <span>图书分类管理</span>
            </template>
            <el-menu-item index="/category/add">分类添加</el-menu-item>
            <el-menu-item index="/categoryList">分类列表</el-menu-item>
          </el-submenu>
          <el-submenu index="book" >
            <template slot="title">
              <i class="el-icon-notebook-1"></i>
              <span>图书管理</span>
            </template>
            <el-menu-item index="/book/add">图书添加</el-menu-item>
            <el-menu-item index="/bookList">图书列表</el-menu-item>
          </el-submenu>
          <el-submenu index="borrow" >
            <template slot="title">
              <i class="el-icon-s-management"></i>
              <span>借书管理</span>
            </template>
            <el-menu-item index="/borrow/add">借书登记</el-menu-item>
            <el-menu-item index="/borrowList">已借列表</el-menu-item>
          </el-submenu>
          <el-submenu index="retur" >
            <template slot="title">
              <i class="el-icon-document"></i>
              <span>还书管理</span>
            </template>
            <el-menu-item index="/returList">还书列表</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>
      <!-- 主体数据--><!--width:0;可以限制容器的宽度,不被子元素无限撑开 -->
      <div style="flex: 1;width:0; background-color: white;padding: 10px">
        <router-view/>
      </div>
    </div>
  </div>
</template>


<script>
import Cookies from "js-cookie";
export default {
  name: 'Layout.vue',
  data(){
    return {
      admin:Cookies.get('admin') ?JSON.parse(Cookies.get('admin')):{}

    }

  },
  methods:{
    logout(){
      //清楚浏览器用户数据
      Cookies.remove('admin')
      //跳转到用户界面
      this.$router.push('/login')

    }

  }
}
</script>