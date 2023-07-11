<template>
  <Navi/>
  <div>
    <h2>日志列表</h2>
    <div >
      <el-form  :inline="true">
        <el-form-item label="操作类型">
          <el-select v-model="rightTypeNames" multiple placeholder="选择操作类型">
            <el-option v-for="item in rightTypes" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="最大等级">
          <el-slider :min="0" :max="6" :step="1" v-model="maxLevel" style="width: 300px"/>
        </el-form-item>
        <el-form-item label="起止日期">
          <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"/>
        </el-form-item>
        <el-form-item label="操作状态">
          <el-select v-model="operateState" clearable placeholder="未选择">
            <el-option v-for="item in operateStates" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="用户ID"><el-input v-model="userId" placeholder="请输入用户ID"></el-input></el-form-item>
        <el-form-item label="描述"> <el-input v-model="description" placeholder="请输入描述"></el-input></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="logs" style="width: 100%">
      <el-table-column prop="time" label="操作时间" sortable></el-table-column>
      <el-table-column prop="username" label="用户名" sortable></el-table-column>
      <el-table-column prop="action" label="操作类型" sortable></el-table-column>
      <el-table-column prop="object" label="用户ID" sortable></el-table-column>
      <el-table-column prop="object" label="操作状态" sortable></el-table-column>
    </el-table>
  </div>
</template>

<script>
import Navi from '@/components/Navi'
import {searchLogs} from "@/service/logServ";
import {getBasicRightList} from "@/service/infoServ";
import {ElMessage} from "element-plus"
export default {
  name:'UserLog',
  components:{Navi},
  data() {
    return {
      rightTypes:[],
      operateStates:[{value:0,label:"成功"},{value:1,label:"不成功"}],

      rightTypeNames:[],
      maxLevel:3,
      dateRange:[],
      operateState:null,
      userId:null,
      description:""

    };
  },
  created() {
    getBasicRightList().then(res=>{
      this.rightTypes=res.data
    })
  },
  methods: {
    search(){
      searchLogs({}).then(res=>{
        if(res.code==='0') {
          this.logs = res.data
        }else{
          ElMessage.error(res.msg);
        }
      })
    },

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
</style>
