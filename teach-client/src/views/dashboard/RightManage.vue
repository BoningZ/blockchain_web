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
          <el-table-column prop="permissionComposition" label="权限组成" >
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-select v-model="scope.row.types" multiple>
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



        <!-- 其他权限管理相关代码 -->
      </div>

      <!-- 用户权限分配 -->
      <div v-if="activeMenu === 'user'">
        <h2>用户权限分配</h2>
        <el-table
            :data="pagedUsers"
            style="width: 100%">
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="text" @click="editUser(scope.row)">编辑</el-button>
              <el-button type="text" @click="deleteUser(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="userId" label="用户ID"></el-table-column>
          <el-table-column prop="permissionName" label="权限名称"></el-table-column>
          <el-table-column prop="admin" label="创建者"></el-table-column>
          <el-table-column prop="createdTime" label="创建时间" sortable></el-table-column>
          <el-table-column prop="modifiedTime" label="修改时间" sortable></el-table-column>

        </el-table>


        <!-- 其他用户权限分配相关代码 -->
      </div>
    </div>
  </div>
</template>

<script>
import Navi from '@/components/Navi'
import {getBasicRightList} from "@/service/infoServ";
// eslint-disable-next-line no-unused-vars
import {getRightList,addRight,deleteRight,updateRight} from "@/service/rightServ";
import {ElMessage} from "element-plus";

export default {
  name: 'RightManage',
  components:{Navi},
  data() {
    return {
      activeMenu: 'permission',
      newRightVisible:false,
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

      users: [
        {
          id: 1,
          username: '用户1',
          createdTime: '2023-07-01',
          modifiedTime: '2023-07-02',
          userId: 1,
          permissionName: '权限1',
          admin: '创建者1'
        },
        {
          id: 2,
          username: '用户2',
          createdTime: '2023-07-02',
          modifiedTime: '2023-07-03',
          userId: 2,
          permissionName: '权限2',
          admin: '创建者2'
        },
        // 其他用户数据
      ],
      options: [
        { value: 'A', label: '选项A' },
        { value: 'B', label: '选项B' },
        { value: 'C', label: '选项C' },
        { value: 'D', label: '选项D' }
      ]
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
  },
  methods: {
    searchRightTable(){
      this.searchRight.page=0
      getRightList(this.searchRight).then(res=>{
        this.rights=res.data.data.map(obj => {
          return { ...obj, editable: false };
        });
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
    savePermission(permission) {
      updateRight(permission).then(res=>{
        ElMessage.success(res.data)
        permission.editable = false;
        permission.updateTime=new Date()
      })
    },


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
</style>
