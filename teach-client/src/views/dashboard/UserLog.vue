<template>
  <div >
    <h2>日志列表</h2>
    <div >
      <el-form  :inline="true">
        <el-form-item label="操作类型">
          <el-select v-model="form.rightTypeNames" multiple placeholder="选择操作类型">
            <el-option v-for="item in rightTypes" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="最大等级">
          <el-slider :min="0" :max="6" :step="1" v-model="form.maxLevel" style="width: 300px"/>
        </el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker
              v-model="form.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select v-model="form.operateState" clearable placeholder="未选择">
            <el-option v-for="item in operateStates" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID"><el-input v-model="form.userId" placeholder="请输入用户ID"></el-input></el-form-item>
        <el-form-item label="描述"> <el-input v-model="form.description" placeholder="请输入描述"></el-input></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="logs" style="width: 100%" height="500">
      <el-table-column prop="operateTime"  label="操作时间" sortable>
        <template #default="scope">
          <el-date-picker
              v-model="scope.row.operateTime"
              type="datetime"
              disabled/>
        </template>
      </el-table-column>
      <el-table-column label="操作状态" width="80">
        <template #default="scope">
          <el-tag type="success" v-show="scope.row.operateState===0">成功</el-tag>
          <el-tag type="danger" v-show="scope.row.operateState===1">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作类型" width="120">
        <template #default="scope">
          <el-tag type="info" v-show="scope.row.rightType.adminOnly">{{rightTypesMap.get(scope.row.rightType.value)}}</el-tag>
          <el-tag v-show="!scope.row.rightType.adminOnly">{{rightTypesMap.get(scope.row.rightType.value)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" sortable></el-table-column>
      <el-table-column label="用户名" >
        <template #default="scope">{{scope.row.user.username}}</template>
      </el-table-column>
      <el-table-column label="用户ID" width="80">
        <template #default="scope">{{scope.row.user.userId}}</template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="warning" @click="audit(scope.row.id,scope.$index)" v-show="scope.row.audited==null">审计</el-button>
          <el-button type="danger" @click="generateHash(scope.row.id,scope.$index)" v-show="scope.row.audited===2">更新状态</el-button>
          <el-tag type="success" v-show="scope.row.audited===1">已审计</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div class="page-container">
      <el-pagination class="center" background layout="prev, pager, next" :page-count="totalPages" :current-page="form.page+1" @current-change="changePage"/>
    </div>
  </div>
</template>

<script>
import {searchLogs,audit,generateHash} from "@/service/logServ";
import {getAllRightList} from "@/service/infoServ";
import {ElMessage} from "element-plus"
export default {
  name:'UserLog',
  data() {
    return {
      rightTypes:[],
      rightTypesMap:{},
      operateStates:[{value:0,label:"成功"},{value:1,label:"不成功"}],

      form: {
        rightTypeNames: [],
        maxLevel: 3,
        dateRange: [],
        operateState: null,
        userId: null,
        description: "",
        page:0
      },

      logs:[],
      totalPages:0

    };
  },
  created() {
    getAllRightList().then(res=>{
      this.rightTypes=res.data
      this.rightTypesMap=res.data.reduce((result, { value, label }) => {
        result.set(value, label);
        return result;
      }, new Map());
    })
  },
  methods: {
    changePage(pageNum){
      this.form.page=pageNum-1;
      searchLogs(this.form).then(res=>{
        if(res.code==='0') {
          this.logs = res.data.data
          this.totalPages=res.data.totalPages
        }else{
          ElMessage.error(res.msg);
        }
      })
    },
    search(){
      this.form.page=0;
      if(this.form.dateRange!=null) {
        this.form.startTime = this.form.dateRange[0]
        this.form.endTime = this.form.dateRange[1]
      }
      searchLogs(this.form).then(res=>{
        if(res.code==='0') {
          this.logs = res.data.data
          this.totalPages=res.data.totalPages
        }else{
          ElMessage.error(res.msg);
        }
      })
    },
    audit(logId,index){
      audit({"logId":logId}).then(res=>{
        if(res.data){
          this.logs[index].audited=1
          ElMessage.success("审计通过")
        }else{
          this.logs[index].audited=2
          ElMessage.error("审计不通过")
        }
      })
    },
    generateHash(logId,index){
      generateHash({"logId":logId}).then(res=>{
        ElMessage.warning(res)
        this.logs[index].audited=1
      })
    }

  }
};
</script>

<style scoped>
.el-table {
  height: 400px;
  overflow-y: auto;
}
.filters {
  margin-bottom: 20px;
}
.edit-form {
  width: 300px;
  margin: 0 auto;
}
.page-container{
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
</style>
