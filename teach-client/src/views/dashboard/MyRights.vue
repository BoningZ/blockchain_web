import
<template>
  <router-view>
    <div class="home">
      <el-card v-for="(item,index) in rightList" v-bind:key="item" style="width: 70%;margin-left: 15%;margin-top: 5px" >
        <template #header>
          <el-row>
            <el-col :span="4">
              <el-text type="success">{{"权限 "+(index+1)}}</el-text>
            </el-col>
            <el-col :span="12">
              <el-text type="success" size="large">{{"权限名："+item.name}}</el-text>
            </el-col>
            <el-col :span="8">
              <el-text type="info">{{"创建人："+item.admin.name}}</el-text>
            </el-col>
          </el-row>
        </template>
        <el-row>
          <el-col :span="2">
            <el-text type="info">组成：</el-text>
          </el-col>
          <el-col :span="22" style="  display: flex; justify-content: flex-start;">
            <el-tag v-for="type in item.rightTypes" type="primary" v-bind:key="type">{{type.label}}</el-tag>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </router-view>
</template>

<script>


import {getMyRights} from "@/service/rightServ";

export default {
  name: 'MyRights',
  data(){
    return{
      rightList:[],
    }
  },
  created() {
    getMyRights().then(res2=>{
      this.rightList=res2.data.data
    })
  }
}
</script>
