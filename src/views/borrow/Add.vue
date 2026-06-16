<template>
  <div style="width: 80%">
    <div style="margin-bottom: 30px">新增借书单</div>
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
        <el-input v-model="form.bookName" disabled ></el-input>
      </el-form-item>
      <el-form-item label="图书数量" prop="nums">
        <el-input v-model="form.nums" disabled ></el-input>
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
        <el-input disabled v-model="form.userName" placeholder="请输入用户名称"></el-input>
      </el-form-item>
      <el-form-item label="用户联系方式" prop="userPhone">
        <el-input disabled v-model="form.userPhone" placeholder="请输入用户联系方式"></el-input>
      </el-form-item>
      <el-form-item label="用户积分" prop="account">
        <el-input disabled v-model="form.account" placeholder="请输入用户积分"></el-input>
      </el-form-item>
      <el-form-item label="已借时间" prop="days">
        <el-input-number v-model="form.days" :min="30" :max="90" label="借书时间"></el-input-number>
      </el-form-item>
    </el-form>
    <div style="text-align: center; margin-top: 30px">
      <el-button type="primary"@click="save"size="medium">提交</el-button>
    </div>
  </div>
</template>
<script>
import request from "@/utils/request";
export default {
  name: 'AddBorrow',
  data() {

    return {
      form: {days: 30},
      books: [],
      users: [],
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
  created() {
    request.get('/book/list').then(res=>{
      this.books = res.data
    })
    request.get('/user/list').then(res=>{
      this.users = res.data
    })
  },

  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid)=>{
        if (valid) {
          request.post('borrow/save',this.form).then(res=>{
            if (res.code==='200') {
              this.$notify.success('新增成功')
              this.$refs['ruleForm'].resetFields()
            }else {
              this.$notify.error(res.msg)
            }
          })
        }

      })

    },
    selBook() {
      const book = this.books.find(v => v.bookNo === this.form.bookNo)
      request.get('/book/'+ book.id).then(res=>{
        //强制设置对象属性 才能触发视图更新 1.对象 2.属性名 3.属性值
        this.$set(this.form,'bookName',res.data.name)
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })

    },
    selUser() {
      const user = this.users.find(v => v.username === this.form.userId)
      request.get('/user/'+ user.id).then(res=>{
        this.$set(this.form,'userName',res.data.name)
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })
    }
  }
}

</script>