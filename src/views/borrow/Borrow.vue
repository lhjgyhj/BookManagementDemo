<template>
  <div>
<!--表单-->
<div style="margin-bottom: 20px">
<el-input v-model="params.bookName" style="width: 240px" placeholder="请输入图书名称"></el-input>
  <el-input v-model="params.bookNo" style="width: 240px; margin-left:5px" placeholder="请输入图书标准码"></el-input><!--params绑定输入框默认值-->
<el-input v-model="params.userId" style="width: 240px; margin-left:5px" placeholder="请输入用户卡号"></el-input><!--params绑定输入框默认值-->
  <el-input v-model="params.userName" style="width: 240px; margin-left:5px" placeholder="请输入用户名称"></el-input><!--params绑定输入框默认值-->
  <el-input v-model="params.userPhone" style="width: 240px; margin-left:5px" placeholder="请输入用户联系方式"></el-input><!--params绑定输入框默认值-->
  <el-button @click="load" style="margin-left: 5px" type="primary"><i class="el-icon-search"></i>查询</el-button>
<el-button @click="reset" style="margin-left: 5px" type="warning"><i class="el-icon-refresh-right"></i>重置</el-button>

</div>
<el-table :data="tableData" stripe>
  <el-table-column prop="id" label="编号" width="80"></el-table-column>
  <el-table-column prop="bookName" label="图书名称"></el-table-column>
<el-table-column prop="bookNo" label="图书标准码"></el-table-column>
<el-table-column prop="userId" label="用户卡号"></el-table-column>
  <el-table-column prop="userName" label="用户名称"></el-table-column>
  <el-table-column prop="userPhone" label="用户联系方式"></el-table-column>
  <el-table-column prop="score" label="借书积分"></el-table-column>
  <el-table-column prop="days" label="已借天数"></el-table-column>
  <el-table-column prop="createtime" label="借出时间"></el-table-column>
  <el-table-column prop="returnDate" label="归还日期"></el-table-column>
  <el-table-column prop="status" label="是否偿还"></el-table-column>
  <el-table-column prop="note" label="到期提醒">
  <template v-slot="scope">
    <el-tag type="success" v-if="scope.row.note === '未到期'">{{scope.row.note}}</el-tag>
    <el-tag type="primary" v-if="scope.row.note === '即将到期'">{{scope.row.note}}</el-tag>
    <el-tag type="warning" v-if="scope.row.note === '已到期'">{{scope.row.note}}</el-tag>
    <el-tag type="danger" v-if="scope.row.note === '已过期'">{{scope.row.note}}</el-tag>
  </template>
  </el-table-column>
<!--  <el-table-column prop="updatetime" label="更新时间"></el-table-column>-->

  <el-table-column label="操作" width="250px">
  <template v-slot="scope"><!--scope.row为当前行数据-->
<!--  <el-button  type="primary" @click="$router.push('/borrow/edit?id=' + scope.row.id)">编辑</el-button>-->
    <el-button type="warning" @click="renewBorrow(scope.row)" style="margin-left:5px">续借</el-button>
    <el-button type="primary" @click="returnBooks(scope.row)">还书</el-button>
    <el-popconfirm
          style="margin-left: 5px"
          title="您确定删除这行数据吗"
    @confirm="del(scope.row.id)"
      >
        <el-button  type="danger" slot="reference">删除</el-button>
      </el-popconfirm>
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
        <el-dialog title="图书续借" :visible.sync="renewDialogVisible" width="30%">
          <el-form :model="form" label-width="100px" ref="renewRuleForm" :rules="rules" style="width: 85%">
            <el-form-item label="续借天数" prop="extendDays">
              <el-input v-model.number="form.extendDays" autocomplete="off" type="number" min="1" max="90"></el-input>
            </el-form-item>
            <el-form-item label="续借备注" prop="remark">
              <el-input v-model="form.remark" autocomplete="off" placeholder="可选：填写续借说明"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="renewDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="confirmRenew">确 定</el-button>
          </div>
        </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'Borrow',
  data(){
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total: 0,
      form: {
        extendDays: 10,
        remark: ''
      },
      renewDialogVisible: false,
      params: {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        userId: '',
        userName: '',
        userPhone: ''
      },
      rules: {
        extendDays: [
          {required: true, message: '请输入续借天数', trigger: 'blur'}
        ]
      }
    };
  },
  created() {
    this.load()//调用方法，完成数据渲染
  },
  methods: {

    //点击分页按钮触发分页
    handlePageChange(pageNum) {
      this.params.pageNum = pageNum;
      this.load();
    },
    load() {
      // fetch("http://localhost:2222/user/list")/*查询后台数据*/.then( res=>res.json())/*转接数据*/.then(res=>{
      //   console.log( res)//打印
      //   this.tableData = res//赋值给tableData
      // })如果没有request.js，则可以这样写
      request.get('/borrow/page', {
        params: this.params
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.list//最终数据在list中，所以进行处理从list中获取
          this.total = res.data.total
        }
      })
    },
    //重置
    reset() {
      this.params = {
        pageNum: 1,
        pageSize: 10,
        bookName: '',
        userId: '',
        username: '',
        userPhone: ''
      }
      this.load();
    },
    del(id) {
      request.delete('/borrow/delete/' + id).then(res => {
        if (res.code === '200') {
          this.$notify.success('删除成功')
          this.load();
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    returnBooks(row) {
      request.post('/retur/save', row).then(res => {
        if (res.code === '200') {
          this.$notify.success('还书成功')
          this.tableData = this.tableData.filter(item => item.id !== row.id)
          this.total--
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    renewBorrow(row) {
      this.form.id=row.id;
      this.renewDialogVisible = true;
    },
    confirmRenew() {
      request.post('/borrow/renew/' + this.form.id, {
        extendDays: this.form.extendDays
      }).then(res => {
        if (res.code === '200') {
          this.$notify.success('续借成功')
          this.renewDialogVisible = false;
          this.load();
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
};
</script>
