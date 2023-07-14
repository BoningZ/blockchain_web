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

    <el-card v-for="(item,index) in rightList" v-bind:key="item" style="width: 80%;margin-left: 10%;margin-top: 5px" >
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

import router from '@/router/index.js'
import {getBasicInfo} from "@/service/infoServ";
import {getMyRights} from "@/service/rightServ";

export default {
  name: 'HomePage',
  data(){
    return{
      infoForm:{
        name:"",
        id:"",
        username:"",
        type:""
      },
      rightList:[],
    }
  },
  created() {
    getBasicInfo().then(res=>{
      this.infoForm=res.data
      if(this.infoForm.type==="成员"){
        this.infoForm.type="普通用户"
        getMyRights().then(res2=>{
          this.rightList=res2.data.data
        })
      }
    })
  },
  methods:{
    logout() {
      this.$store.commit('logout')
      router.push('/login')
    }
  }
}
</script>
