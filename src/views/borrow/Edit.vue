<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">编辑借书管理</div>
    <el-form :inline="true" :model="form" rules="rules"  ref="ruleForm" label-width="100px">
      <el-form-item label="图书标准码" prop="bookNo">
        <el-select v-model="form.bookNo" clearable filterable placeholder="请选择" @change="selBook">
          <el-option
              v-for="item in books"
              :key="item.id"
              :label="item.bookNo"
              :value="item.bookNo">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" disabled placeholder="请输入图书名称"></el-input>
      </el-form-item>
      <el-form-item label="图书数量" prop="nums">
        <el-input v-model="form.nums" disabled placeholder="请输入图书名称"></el-input>
      </el-form-item>
      <el-form-item label="所需积分"  prop="score">
        <el-input v-model="form.score" disabled ></el-input>
      </el-form-item>
      <br/>
      <el-form-item label="用户卡号" prop="userId">
        <el-select v-model="form.userId" clearable filterable placeholder="请选择" @change="selUser">
          <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.name +'  '+'('+ item.username +')'"
              :value="item.username">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item  label="用户名称" prop="userName">
        <el-input  v-model="form.userName" disabled placeholder="请输入用户名称"></el-input>
      </el-form-item>
      <el-form-item label="用户联系方式" prop="userPhone">
        <el-input  v-model="form.userPhone" disabled placeholder="请输入用户联系方式"></el-input>
      </el-form-item>

    </el-form>
    <div style="text-align: center; margin-top: 30px">
      <el-button type="primary"@click="save"size="medium">提交</el-button>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request";
import book from "@/views/book/Book.vue";
export default {
  name: 'EditBorrow',
  data() {
    return {
      form: {
      },
      rules: {
        bookNo: [
          {required: true, message: '请选择图书标准码', trigger: 'blur'}
        ],
        userId: [
          {required: true, message: '请选择用户卡号', trigger: 'blur'}
        ]
      }
    }
  },
  created(){
      request.get('/book/list').then(res=>{
        this.books = res.data
      })
      request.get('/user/list').then(res=>{
        this.users = res.data
      })
    const id = this.$route.query.id
    request.get('/borrow/'+ id).then(res=>{
      this.form = res.data
    })
  },
  methods: {
    save(){
      request.put/*后端如果是postMapping则用post,PutMapping则用put*/('borrow/update',this.form).then(res=>{
        if (res.code==='200') {
          this.$notify.success('更新成功')
          this.$router.push('/borrowList')
        }else {
          this.$notify.error(res.msg)
        }
      })

    },
    selBook() {
      const book = this.books.find(v => v.bookNo === this.form.bookNo)
      request.get('/book/'+ book.id).then(res=>{
        this.form.bookName = res.data.name
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })

    },
    selUser() {
      const user = this.users.find(v => v.username === this.form.userId)
      request.get('/user/'+ user.id).then(res=>{
        this.form.userName = res.data.name
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })

    }
  }
}
</script>