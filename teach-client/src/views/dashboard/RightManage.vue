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
        <el-table
            :data="pagedPermissions"
            style="width: 100%"
            :default-sort="{ prop: 'createdTime', order: 'descending' }"
            :pagination="paginationConfig"
        >
          <el-table-column label="操作">
            <template #default="scope">
              <el-button v-if="scope.row.editable" type="text" @click="savePermission(scope.row)">确认</el-button>
              <el-button v-else type="text" @click="editPermission(scope.row)">编辑</el-button>
              <el-button type="text" @click="deletePermission(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
          <el-table-column prop="name" label="权限名称">
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-input v-model="scope.row.name"></el-input>
              </div>
              <div v-else>{{ scope.row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="permissionComposition" label="权限组成">
            <template #default="scope">
              <div v-if="scope.row.editable">
                <el-select v-model="scope.row.permissionComposition" multiple>
                  <el-option v-for="option in options" :key="option.value" :label="option.label" :value="option.value"></el-option>
                </el-select>
              </div>
              <div v-else>{{ scope.row.permissionComposition.join(', ') }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="adminId" label="创建者"></el-table-column>
          <el-table-column prop="createdTime" label="创建时间" sortable></el-table-column>
          <el-table-column prop="modifiedTime" label="修改时间" sortable></el-table-column>
        </el-table>

        <el-button type="primary" @click="showCreatePermissionDialog">新增权限</el-button>

        <!-- 其他权限管理相关代码 -->
      </div>

      <!-- 用户权限分配 -->
      <div v-if="activeMenu === 'user'">
        <h2>用户权限分配</h2>
        <el-table
            :data="pagedUsers"
            style="width: 100%"
            :default-sort="{ prop: 'createdTime', order: 'descending' }"
            :pagination="paginationConfig"
        >
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
        <el-button type="primary" @click="showCreatePermissionDialog">新增用户权限</el-button>

        <!-- 其他用户权限分配相关代码 -->
      </div>
    </div>
  </div>
</template>

<script>
import Navi from '@/components/Navi'

export default {
  name: 'RightManage',
  components:{Navi},
  data() {
    return {
      activeMenu: 'permission',
      permissions: [
        {
          id: 1,
          name: '权限1',
          createdTime: '2023-07-01',
          modifiedTime: '2023-07-02',
          adminId: 1,
          permissionComposition: ['A', 'B'],
          editable: false
        },
        {
          id: 2,
          name: '权限2',
          createdTime: '2023-07-02',
          modifiedTime: '2023-07-03',
          adminId: 1,
          permissionComposition: ['C', 'D'],
          editable: false
        },
        // 其他权限数据
      ],
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
      pagedPermissions: [],
      pagedUsers: [],
      pageSize: 10,
      currentPage: 1,
      paginationConfig: {
        layout: 'total, sizes, prev, pager, next, jumper',
        pageSizes: [10, 20, 30, 40],
        pageSize: 10,
        currentPage: 1
      },
      options: [
        { value: 'A', label: '选项A' },
        { value: 'B', label: '选项B' },
        { value: 'C', label: '选项C' },
        { value: 'D', label: '选项D' }
      ]
    };
  },
  computed: {
    totalPermissions() {
      return this.permissions.length;
    },
    totalUsers() {
      return this.users.length;
    }
  },
  created() {
    this.updatePagedPermissions();
    this.updatePagedUsers();
  },
  watch: {
    pageSize() {
      this.updatePagedPermissions();
      this.updatePagedUsers();
    },
    currentPage() {
      this.updatePagedPermissions();
      this.updatePagedUsers();
    }
  },
  methods: {
    handleMenuSelect(index) {
      this.activeMenu = index;
    },
    updatePagedPermissions() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pagedPermissions = this.permissions.slice(startIndex, endIndex);
    },
    updatePagedUsers() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      this.pagedUsers = this.users.slice(startIndex, endIndex);
    },
    deletePermission(permissionId) {
      const index = this.permissions.findIndex(p => p.id === permissionId);
      if (index !== -1) {
        this.permissions.splice(index, 1);
        this.updatePagedPermissions();
      }
    },
    editPermission(permission) {
      permission.editable = true;
    },
    savePermission(permission) {
      permission.editable = false;
    },
    // deletePermission(permissionId) {
    //   const index = this.permissions.findIndex(p => p.id === permissionId);
    //   if (index !== -1) {
    //     this.permissions.splice(index, 1);
    //     this.updatePagedPermissions();
    //   }
    // },
    // showCreatePermissionDialog() {
    //
    // },
    // cancelCreatePermissionDialog() {
    //
    // },
    // createPermission() {
    //
    // },
    // editUser(user) {
    //
    // },
    // deleteUser(userId) {
    //
    // },
    // showCreateUserDialog() {
    //
    // },
    // cancelCreateUserDialog() {
    //
    // },
    // createUser() {
    //
    // }
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
</style>
