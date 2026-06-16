<template>
 <div>
   <div style="margin: 20px 0">
     <el-select class="input" v-model="timeRange" placeholder="请选择时间范围" @change="load"><!--从后台加载最新的数据-->
       <el-option
         v-for="item in options"
         :key="item.value"
         :label="item.label"
         :value="item.value">
       </el-option>
     </el-select>

   </div>
   <el-card>
     <div id="line" style="width: 100%; height: 400px"></div>
   </el-card>
 </div>
</template>

<script>
import Cookies from "js-cookie";
import request from "@/utils/request";
import * as echarts from 'echarts'

const option = {
  title: {
    text: '图书借还统计'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['借书数量','还书数量'],
    top: '10px',
    right: '50px'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: {}
    }
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '借书数量',
      type: 'line',
      smooth: true,
      data: []
    },
    {
      name: '还书数量',
      type: 'line',
      smooth: true,
      data: []//从后台动态获取数据
    },
  ]
};
export default {
  data() {
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      lineBox: null,/*定义容器*/
      timeRange:'week',//默认请求一周
      options:[
          {
            value:'week',
            label:'近一周'
          },
          {
            value:'month',
            label:'近一月'
          },
          {
            value:'quarter',
            label:'近三月'
          },
          {
            value:'year',
            label:'近一年'
          }
      ]
    }
  },
  mounted() {//等页面元素全部初始化之后刷新
    this.load()
  },
  methods: {
    load() {
      if (!this.lineBox){
      this.lineBox = echarts.init(document.getElementById('line'))//初始化echards容器
      }
      //从后台获取数据
      request.get('/borrow/lineCharts/' + this.timeRange).then(res=>{
        option.xAxis.data = res.data?.date || []
        option.series[0].data = res.data?.borrow || []
        option.series[1].data = res.data?.retur || []
        this.lineBox.setOption(option)//设置容器属性值，刷新数据
      })
    }
    },
  created() {
    // request.get('/admin'+ this.admin.id).then(res=>{
    //   this.admin = res.data
    // })//主页请求数据，如果要用注释的那种路由守卫，则要加上这个请求数据
  }
}
</script>
<style>
.item{
  margin: 10px 0;
  color: #666;
}
</style>