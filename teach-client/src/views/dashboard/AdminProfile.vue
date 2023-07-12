<template>
  <div  class="form-div">
    <el-form ref="editForm" label-position="left" label-width="100px" status-icon style="margin-top: 15px;">
      <br/>
      <el-row align="middle">
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="7">
          <el-form-item label="姓名" >
            <el-input v-model="name" maxlength="20" show-word-limit style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item >
            <el-button type="primary" @click="submit">修改</el-button>
          </el-form-item>
        </el-col>
      </el-row>


      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="工号" >
            <el-input v-model="aid" :disabled="true"  maxlength="20"  style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>&nbsp;</el-row>

      <el-row align="middle">
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="旧密码" prop="oldPas">
            <el-input v-model="oldPassword" :type="passw" style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="新密码" prop="oldPas">
            <el-input v-model="newPassword" :type="passw"  style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item label="确认密码" prop="oldPas">
            <el-input v-model="newPassword1" :type="passw"  style="width: 500px;background-color: #f4f4f5"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row >
        <el-col :span="4">
          &nbsp;
        </el-col>
        <el-col :span="10">
          <el-form-item align="center">
            <el-button type="warning" @click="submitPassword">修改密码</el-button>
          </el-form-item>
        </el-col>
      </el-row>


    </el-form>
  </div>
</template>

<script>
import {changePassword, getProfile, submitProfile} from "@/service/genServ";
export default {
  name: "MemberProfile",
  data(){
    return{
      aid:"",
      name:"",

      passw: 'password',
      oldPassword: '',
      newPassword: '',
      newPassword1: '',
    }
  },
  created() {
    this.doQuery();
  },
  methods:{
    submitPassword() {
      if(!this.newPassword){
        this.$message({
          message:  '请填入新密码',
          type: 'warning',
        })
      }else if (this.newPassword !== this.newPassword1) {
        this.$message({
          message:  '口令不一致',
          type: 'warning',
        })
      } else {
        changePassword({ 'oldPassword': this.oldPassword, 'newPassword': this.newPassword }).then(response => {
          if (response.code === '0') {
            this.$message({
              message:  '成功',
              type: 'success',
            })
          } else {
            this.$message({
              message:  response.msg,
              type: 'warning',
            })
          }
        })
      }
    },
    doQuery(){
      getProfile().then(res=>{
        this.aid=res.data.aid;
        this.name=res.data.name;
      })
    },
    submit(){
      submitProfile({"aid":this.aid,"name":this.name}).then(res=>{
        if(res.code==='0'){
          this.$message({
            message:  '成功',
            type: 'success',
          })
        }else{
          this.$message({
            message:  res.msg,
            type: 'warning',
          })
        }
      })
    },

  }
}
</script>

