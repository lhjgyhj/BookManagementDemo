<template>
  <div>
<!--表单-->
<div style="margin-bottom: 20px">
<el-input v-model="params.name" style="width: 240px" placeholder="请输入分类名称"></el-input>
  <el-button @click="load" style="margin-left: 5px" type="primary"><i class="el-icon-search"></i>查询</el-button>
<el-button @click="reset" style="margin-left: 5px" type="warning"><i class="el-icon-refresh-right"></i>重置</el-button>

</div>
<el-table :data="tableData" stripe row-key="id" default-expand-all>
  <el-table-column prop="id" label="编号" width="80"></el-table-column>
  <el-table-column prop="name" label="名称"></el-table-column>
<el-table-column prop="remark" label="备注"></el-table-column>
  <el-table-column prop="createtime" label="创建时间"></el-table-column>
  <el-table-column prop="updatetime" label="更新时间"></el-table-column>

  <el-table-column label="操作" width="250px">
  <template v-slot="scope"><!--scope.row为当前行数据-->
    <el-button  type="success" v-if="!scope.row.pid" @click="handleAdd(scope.row)">添加二级分类</el-button>
    <el-button  type="primary" @click="$router.push('/category/edit?id=' + scope.row.id)">编辑</el-button>
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
    <div>
      <el-dialog title="添加二级分类" :visible.sync="dialogFormVisible" width="30%" >
        <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 85%">
          <el-form-item label="分类名称"  prop="name">
            <el-input v-model="form.name" autocomplete="off" ></el-input>
          </el-form-item>
          <el-form-item label="分类备注"  prop="remark">
            <el-input v-model="form.remark" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import "./Category.vue";
</script>
<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
  name: 'Category',
  data(){
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      tableData: [],
      total:0,
      dialogFormVisible:false,
      form:{},
      pid:null,
      params:{
        pageNum:1,
        pageSize:10,
        name:''
      },
      rules:{
        name: [
          {required: true, message: '请输入分类新名称', trigger: 'blur' }]
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
      request.get('/category/page',{
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
        name:''
      }
      this.load();
    },
    del(id){
      request.delete('/category/delete/'+id).then(res=>{
        if (res.code==='200') {
          this.$notify.success('删除成功')
          this.load();
        }else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleAdd(row){
      //将当前行的ID作为二级分类的pid
      this.pid=row.id
      this.dialogFormVisible = true;//打开弹窗
    },
    save() {
      this.$refs['ruleForm'].validate((valid)=>{
        if (valid) {
          this.form.pid= this.pid//给二级分类复制一个pid
          request.post('/category/save',this.form).then(res=>{
            if (res.code==='200') {
              this.$notify.success('新增二级分类成功')
              this.$refs['ruleForm'].resetFields()
              this.dialogFormVisible = false;
              this.load();
            }else {
              this.$notify.error(res.msg)
            }
          })
        }

      })
  }

}
}
</script>
