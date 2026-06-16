<template>
  <div>
<!--表单-->
<div style="margin-bottom: 20px">
<el-input v-model="params.username" style="width: 240px" placeholder="请输入用户名"></el-input>
<el-input v-model="params.phone" style="width: 240px; margin-left:5px" placeholder="请输入联系方式"></el-input><!--params绑定输入框默认值-->
  <el-input v-model="params.email" style="width: 240px; margin-left:5px" placeholder="请输入邮箱"></el-input><!--params绑定输入框默认值-->
  <el-button @click="load" style="margin-left: 5px" type="primary"><i class="el-icon-search"></i>查询</el-button>
<el-button @click="reset" style="margin-left: 5px" type="warning"><i class="el-icon-refresh-right"></i>重置</el-button>

</div>
<el-table :data="tableData" stripe>
  <el-table-column prop="id" label="编号" width="80"></el-table-column>
  <el-table-column prop="username" label="用户名"></el-table-column>
<el-table-column prop="phone" label="联系方式"></el-table-column>
<el-table-column prop="email" label="邮箱"></el-table-column>
  <el-table-column prop="createtime" label="创建时间"></el-table-column>
  <el-table-column prop="updatetime" label="更新时间"></el-table-column>

  <el-table-column label="操作" width="250px">
  <template v-slot="scope"><!--scope.row为当前行数据-->
  <el-button  type="primary" @click="$router.push('/admin/edit?id=' + scope.row.id)">编辑</el-button>
      <el-popconfirm
          style="margin-left: 5px"
          title="您确定删除这行数据吗"
    @confirm="del(scope.row.id)"
      >
    <el-button  type="danger" slot="reference">删除</el-button>
      </el-popconfirm>
        <el-button style="margin-left: 5px" type="warning" @click="handleChangePass(scope.row)" >修改密码</el-button>
  </template>
</el-table-column>
</el-table>
<!--分页-->
<div style="margin-top: 20px">

  <el-pagination :background="true"
                 :current-page="params.pageNum"
                 :page-size="params.pageSize"
                 layout="prev, pager, next"
                 :total="total"
                 @current-change="handlePageChange">
  </el-pagination>
  </div>
    <div>
      <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%" >
        <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
          <el-form-item label="新密码" :label-width="100" prop="newPass">
            <el-input v-model="form.newPass" autocomplete="off" show-password></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="savePass">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import "./Admin.vue";
</script>
<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'Admin',
  data(){
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total:0,
      form:{},
      dialogFormVisible:false,
      params:{
        pageNum:1,
        pageSize:10,
        username:'',
        phone:'',
        email:''
      },
      rules:{
        newPass: [
          {required: true, message: '请输入新密码', trigger: 'blur' },
          {min:3,max:10,message:'长度在3到10个字符', trigger: 'blur'}]
      },
    }
  },
  created() {
    this.load()//调用方法，完成数据渲染
  },
  methods: {

    handleChangePass(row){
      this.form = JSON.parse(JSON.stringify(row))
      //跟当前的行对象隔离，将当前行对象赋值给form，这样修改密码的时候，form的数据会改变，不会影响当前行对象
      this.dialogFormVisible = true;//显示修改密码的对话框
    },
    savePass(){
      this.$refs['formRef'].validate((valid)=>{
      if ( valid){
        request.post('/admin/password',this.form).then(res=>{
          if (res.code==='200') {
            this.$notify.success('修改密码成功')
            if (this.form.id===this.admin.id){ //如果修改的密码是当前登录的密码,那么修改后需要重新登陆
              Cookies.remove('admin')
              this.$router.push({
                path:'/login',})

            }else {
              this.load()
              this.dialogFormVisible = false
            }
          }else {
            this.$notify.error("修改失败")
          }
        })

      }
      })
    },
    //点击分页按钮触发分页
    handlePageChange(pageNum){
      this.params.pageNum = pageNum;
      this.load();
    },
    load(){
      // fetch("http://localhost:2222/user/list")/*查询后台数据*/.then( res=>res.json())/*转接数据*/.then(res=>{
      //   console.log( res)//打印
      //   this.tableData = res//赋值给tableData
      // })如果没有request.js，则可以这样写
      request.get('/admin/page',{
        params:this.params
      }).then(res=>{
        if (res.code==='200') {
          this.tableData = res.data.list//最终数据在list中，所以进行处理从list中获取
          this.total = res.data.total
        }
      })
    },
    //重置
    reset(){
      this.params = {
        pageNum:1,
        pageSize:10,
        username:'',
        phone:'',
        email:''
      }
      this.load();
    },
    del(id){
      request.delete('/admin/delete/'+id).then(res=>{
        if (res.code==='200') {
          this.$notify.success('删除成功')
          this.load();
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }

}
</script>
