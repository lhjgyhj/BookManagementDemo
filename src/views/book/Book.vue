<template>
  <div>
<!--表单-->
<div style="margin-bottom: 20px">
<el-input v-model="params.name" style="width: 240px" placeholder="请输入图书名称"></el-input>
<el-input v-model="params.bookNo" style="width: 240px; margin-left:5px" placeholder="请输入图书标准码"></el-input><!--params绑定输入框默认值-->
  <el-button @click="load" style="margin-left: 5px" type="primary"><i class="el-icon-search"></i>查询</el-button>
<el-button @click="reset" style="margin-left: 5px" type="warning"><i class="el-icon-refresh-right"></i>重置</el-button>

</div>
<el-table :data="tableData" stripe>
  <el-table-column prop="id" label="编号" width="80"></el-table-column>
  <el-table-column prop="name" label="图书名称"></el-table-column>
<el-table-column prop="description" width="200" label="图书描述"></el-table-column>
<el-table-column prop="publishDate" label="出版日期"></el-table-column>
  <el-table-column prop="author" label="作者"></el-table-column>
  <el-table-column prop="publisher" label="出版社"></el-table-column>
  <el-table-column prop="category" label="分类"></el-table-column>
  <el-table-column prop="bookNo" label="标准码"></el-table-column>
  <el-table-column prop="nums" label="数量"></el-table-column>
  <el-table-column prop="score" label="所需积分"></el-table-column>
  <el-table-column prop="cover" label="封面">
  <template v-slot="scope">
    <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
  </template>
  </el-table-column>
  <el-table-column prop="createtime" label="创建时间"></el-table-column>
  <el-table-column prop="updatetime" label="更新时间"></el-table-column>

  <el-table-column label="操作" width="250px">
  <template v-slot="scope"><!--scope.row为当前行数据-->
  <el-button  type="primary" @click="$router.push('/book/edit?id=' + scope.row.id)">编辑</el-button>
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
  </div>
</template>

<script setup>
import "./Book.vue";
</script>
<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'Book',
  data(){
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total:0,
      params:{
        pageNum:1,
        pageSize:10,
        name:'',
        bookNo:''

      },
    }
  },
  created() {
    this.load()//调用方法，完成数据渲染
  },
  methods: {

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
      request.get('/book/page',{
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
        name:'',
        bookNo:''
      }
      this.load();
    },
    del(id){
      request.delete('/book/delete/'+id).then(res=>{
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
