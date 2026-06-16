<template>
  <div style="height: 90vh;overflow: hidden;position: relative">
    <el-card class="cover" v-if="loginAdmin.id">
    <slide-verify :l="42"
                  :r="10"
                  :w="310"
                  :h="135"
                  :accuracy="5"
                  slider-text="向右滑动"
                  @success="onSuccess"
                  @fail="onFail"
                  @refresh="onRefresh"
                  :imgs="imgs"
    ></slide-verify>
    </el-card>
<div style="width: 400px;height: 350px;background-color: white;border-radius: 12px;
  margin:150px auto;padding: 50px">
  <div style="margin: 10px;text-align: center;font-size: 30px;font-weight:bold; color:cornflowerblue">登录</div>
  <el-form :model="admin" :rules="admin.rules" ref="loginForm">
    <el-form-item prop="username" style="margin-bottom: 20px">
      <el-input placeholder="请输入账号" prefix-icon="el-icon-user" size="medium" v-model="admin.username"></el-input>
    </el-form-item>
    <el-form-item prop="password" style="margin-bottom: 25px">
      <el-input placeholder="请输入密码" show-password prefix-icon="el-icon-lock" size="medium" v-model="admin.password"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button style="width:100%" size="medium" type="primary" @click="login">登录</el-button>
    </el-form-item>
  </el-form>
</div>
  </div>
</template>


<script>
import request from "@/utils/request";
import Cookies from "js-cookie";//存储用户数据,不用刷新后再重新登陆
import verifyImg1 from '@/assets/verify/1.jpg';
export default {
  name: 'LOGIN',
    data() {
      return {
        imgs:[require('@/assets/verify/1.jpg')],
      loginAdmin: {},
      admin:{
        rules: {
          username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            {min:1,max:100,message:'长度在1到100个字符', trigger: 'blur'}
          ],
          password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            {min:1,max:100,message:'长度在1到100个字符', trigger: 'blur'}
          ],
        }
      }
    }
  },
  methods:{
    login(){
      this.$refs['loginForm'].validate((valid)=>{
        if( valid){
          request.post('/admin/login',this.admin).then(res=>{
            if (res.code==='200') {
              this.loginAdmin = res.data//滑块组件出现
            }else {
              this.$notify.error(res.msg)
            }
          })

        }
      })

      },
    onSuccess(){//滑块组件验证成功之后出现
      Cookies.set('admin',JSON.stringify(this.loginAdmin))
      this.$router.push('/')
      this.$notify.success('登录成功')},
    onFail(){//滑块组件验证失败之后出现
      this.$notify.error('验证失败')
    },
    onRefresh(){//滑块组件刷新之后出现
      console.log('refresh')
    }
    }
}
</script>
<style>
.cover{
  width: fit-content;
  background-color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%,-50%);
  z-index: 1000;
}
</style>