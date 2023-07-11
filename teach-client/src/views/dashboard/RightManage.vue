<template>
  <Navi/>
  <div>
    <div class="sidebar">
      <el-menu :default-active="activeMenu" @select="handleMenuSelect">
        <el-menu-item index="permission">权限管理</el-menu-item>
        <el-menu-item index="user">用户权限分配</el-menu-item>
      </el-menu>
    </div>

    <div class="content">
      <!-- 权限管理 -->
      <div v-if="activeMenu === 'permission'">
        <h2>权限管理</h2>

        <el-form  :inline="true">
          <el-form-item label="名称">
            <el-input v-model="searchRight.name"/>
          </el-form-item>
          <el-form-item label="类型">
            <el-select v-model="searchRight.types" multiple placeholder="选择操作类型">
              <el-option v-for="item in rightTypes" :key="item.id" :label="item.label" :value="item.id"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchRightTable">搜索</el-button>
          </el-form-item>
        </el-form>

        <el-button type="success" @click="newRightVisible=true">新增权限</el-button>
        <el-dialog v-model="newRightVisible" title="新增权限">
          <el-form >
            <el-form-item label="权限名" >
              <el-input v-model="rightForm.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="权限组成" >
              <el-select v-model="rightForm.types" multiple placeholder="选择权限组成">
                <el-option v-for="item in rightTypes" :key="item.id" :label="item.label" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="newRightVisible = false">取消</el-button>
              <el-button type="primary" @click="submitRight">提交</el-button>
            </span>
          </template>
        </el-dialog>

        <el-table
            :data="rights"
            style="width: 100%"
            height="500">
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button v-if="scope.row.editable" link type="success" @click="savePermission(scope.row)">确认</el-button>
              <el-button v-else link type="primary" @click="editPermission(scope.row)">编辑</el-button>
              <el-popconfirm title="确定删除吗？" @confirm="deletePermission(scope.row.id)">
                <template #reference>
                  <el-button link type="danger" >删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="权限名称" width="200">
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-input v-model="scope.row.name"></el-input>
              </div>
              <div v-else>{{ scope.row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column  label="权限组成" >
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-select v-model="scope.row.types" multiple filterable>
                  <el-option v-for="option in rightTypes" :key="option.id" :label="option.label" :value="option.id"></el-option>
                </el-select>
              </div>
              <div v-else>
                <el-tag v-for="id in scope.row.types" :key="id">{{rightTypesMap.get(id)}}</el-tag>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="250" sortable>
            <template #default="scope">
              <el-date-picker
                  v-model="scope.row.createTime"
                  type="datetime"
                  disabled/>
            </template>
          </el-table-column>
          <el-table-column prop="updateTime" label="修改时间" width="250" sortable>
            <template #default="scope">
              <el-date-picker
                  v-model="scope.row.updateTime"
                  type="datetime"
                  disabled/>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-container">
          <el-pagination class="center" background layout="prev, pager, next" :page-count="rightTotalPages" :current-page="searchRight.page+1" @current-change="rightChangePage"/>
        </div>



        <!-- 其他权限管理相关代码 -->
      </div>

      <!-- 用户权限分配 -->
      <div v-if="activeMenu === 'user'">
        <h2>用户权限分配</h2>
        <el-form  :inline="true">
          <el-form-item label="条件">
            <el-input v-model="searchMember.condition"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchMemberTable">搜索</el-button>
          </el-form-item>
        </el-form>
        <el-table
            :data="members"
            style="width: 100%">
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.editable" link type="success" @click="saveMember(scope.row)">确认</el-button>
              <el-button v-else link type="primary" @click="editMember(scope.row)">编辑</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="姓名"></el-table-column>
          <el-table-column prop="mid" label="工号"></el-table-column>
          <el-table-column label="拥有权限" >
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-select v-model="scope.row.rights" multiple filterable>
                  <el-option v-for="option in rightsList" :key="option.id" :label="option.name" :value="option.id"></el-option>
                </el-select>
              </div>
              <div v-else>
                <el-tag v-for="id in scope.row.rights" :key="id">{{rightsMap.get(id)}}</el-tag>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-container">
          <el-pagination class="center" background layout="prev, pager, next" :page-count="memberTotalPages" :current-page="searchMember.page+1" @current-change="memberChangePage"/>
        </div>


        <!-- 其他用户权限分配相关代码 -->
      </div>
    </div>
  </div>
</template>

<script>
import Navi from '@/components/Navi'
import {getBasicRightList} from "@/service/infoServ";
import {getRightList,addRight,deleteRight,updateRight,getRightsMap,getRightsOfAll,updateRightsByMember} from "@/service/rightServ";
import {ElMessage} from "element-plus";

export default {
  name: 'RightManage',
  components:{Navi},
  data() {
    return {
      activeMenu: 'permission',

      newRightVisible:false,
      rightTotalPages:0,
      rightTypes:[],
      rightTypesMap:{},
      searchRight:{
        name:"",
        types:[],
        page:0
      },
      rightForm:{
        name:"",
        types:[]
      },
      rights:[],

      searchMember:{
        condition:"",
        page:0
      },
      memberTotalPages:0,
      rightsMap:{},
      rightsList:[],
      members: []
    };
  },
  created() {
    getBasicRightList().then(res=>{
      this.rightTypes=res.data
      this.rightTypesMap=res.data.reduce((result, { id, label }) => {
        result.set(id, label);
        return result;
      }, new Map());
    })
    this.searchRightTable()
    this.getRightsMap()
    this.searchMemberTable()
  },
  methods: {
    rightChangePage(pageNum){
      this.searchRight.page=pageNum-1;
      getRightList(this.searchRight).then(res=>{
        this.rights=res.data.data.map(obj => {
          return { ...obj, editable: false };
        });
        this.rightTotalPages=res.data.totalPages
      })
    },
    memberChangePage(pageNum){
      this.searchMember.page=pageNum-1;
      getRightList(this.searchMember).then(res=>{
        this.members=res.data.data.map(obj => {
          return { ...obj, editable: false };
        });
        this.memberTotalPages=res.data.totalPages
      })
    },
    searchRightTable(){
      this.searchRight.page=0
      getRightList(this.searchRight).then(res=>{
        this.rights=res.data.data.map(obj => {
          return { ...obj, editable: false };
        });
        this.rightTotalPages=res.data.totalPages
      })
    },
    searchMemberTable(){
      this.searchMember.page=0
      getRightsOfAll(this.searchMember).then(res=>{
        this.members=res.data.data.map(obj => {
          return { ...obj, editable: false };
        });
        this.memberTotalPages=res.data.totalPages
      })
    },
    getRightsMap(){
      getRightsMap().then(res=>{
        this.rightsList=res.data
        this.rightsMap=res.data.reduce((result, { id, name }) => {
          result.set(id, name);
          return result;
        }, new Map());
      })
    },
    submitRight(){
      addRight(this.rightForm).then(res=>{
        ElMessage.success(res)
        this.searchRightTable()
        this.newRightVisible=false
      })
    },
    handleMenuSelect(index) {
      this.activeMenu = index;
    },
    deletePermission(permissionId) {
      deleteRight({"id":permissionId}).then(res=>{
        ElMessage.success(res.data)
        const index = this.rights.findIndex(p => p.id === permissionId);
        this.rights.splice(index, 1);
      })
    },
    editPermission(permission) {
      permission.editable = true;
    },
    editMember(member){
      member.editable=true
    },
    savePermission(permission) {
      updateRight(permission).then(res=>{
        ElMessage.success(res.data)
        permission.editable = false;
        permission.updateTime=new Date()
      })
    },
    saveMember(member){
      updateRightsByMember(member).then(res=>{
        ElMessage.success(res.data)
        member.editable = false;
      })
    }


  }
};
</script>
<style scoped>
.sidebar {
  width: 200px;
  height: 100vh;
  background: #f0f0f0;
  float: left;
}

.content {
  padding: 20px;
  margin-left: 200px;
}

.editable-cell .el-input {
  width: 90%;
}

.editable-cell .el-select {
  width: 90%;
  margin-top: 5px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}
.page-container{
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
}
</style>
