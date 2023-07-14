import
<template>
  <router-view>
  <div class="home">
    <el-card  style="width: 20%;margin-left: 40%;margin-top: 10px;margin-bottom: 20px" >
      <el-row>
          <el-text>用户名：</el-text>
          <el-text type="primary">{{infoForm.username}}</el-text>
      </el-row>
      <el-row>
          <el-text>角色：</el-text>
          <el-tag type="success">{{infoForm.type}}</el-tag>
      </el-row>
      <el-row>
        <el-text>{{"姓名："+infoForm.name}}</el-text>
      </el-row>
      <el-row>
        <el-text>{{"工号："+infoForm.id}}</el-text>
      </el-row>
    </el-card>
    <el-divider />

  </div>
  </router-view>
</template>

<script>


import {getBasicInfo} from "@/service/infoServ";

export default {
  name: 'HomePage',
  data(){
    return{
      infoForm:{
        name:"",
        id:"",
        username:"",
        type:""
      }
    }
  },
  created() {
    getBasicInfo().then(res=>{
      this.infoForm=res.data
      if(this.infoForm.type==="成员"){
        this.infoForm.type="普通用户"
      }
    })
  }
}
</script>
