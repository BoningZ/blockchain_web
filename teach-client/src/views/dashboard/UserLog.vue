<template>
  <Navi/>
  <div>
    <h2>日志列表</h2>
    <div class="filters">
      <el-form :model="editForm" label-width="100px" class="edit-form">
      <el-form-item label="用户名"><el-input v-model="filterUsername" placeholder="请输入用户名"></el-input></el-form-item>
      <el-form-item label="操作类型"> <el-input v-model="filterAction" placeholder="请输入操作类型"></el-input></el-form-item>
      <el-form-item label="用户ID"><el-input v-model="filterObject" placeholder="请输入用户ID"></el-input></el-form-item>
      </el-form>
    </div>
    <el-table
        :data="pagedLogs"
        style="width: 100%"
        :pagination="paginationConfig"
        @sort-change="handleSortChange"
    >
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
export default {
  name:'UserLog',
  components:{Navi},
  data() {
    return {
      logs: [
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 11:00:00', username: 'User2', action: '修改', object: '交易B' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        { time: '2023-07-01 10:30:00', username: 'User1', action: '创建', object: '交易A' },
        // 更多日志数据
      ],
      pageSize: 20,
      currentPage: 1,
      paginationConfig: {
        layout: 'total, prev, pager, next, sizes, jumper',
        pageSizes: [20, 30, 40, 50],
        pageSize: 20,
        currentPage: 1
      },
      filterUsername: '',
      filterAction: '',
      filterObject: ''
    };
  },
  computed: {
    totalLogs() {
      return this.logs.length;
    },
    pagedLogs() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      let filteredLogs = this.logs.slice(startIndex, endIndex);

      // 根据筛选条件进行过滤
      if (this.filterUsername) {
        filteredLogs = filteredLogs.filter(log => log.username.includes(this.filterUsername));
      }
      if (this.filterAction) {
        filteredLogs = filteredLogs.filter(log => log.action.includes(this.filterAction));
      }
      if (this.filterObject) {
        filteredLogs = filteredLogs.filter(log => log.object.includes(this.filterObject));
      }

      return filteredLogs;
    }
  },
  watch: {
    pageSize() {
      this.currentPage = 1;
    },
    currentPage() {

    }
  },
  methods: {
    handleSortChange(sort) {
      const { prop, order } = sort;
      if (prop && order) {
        this.logs.sort((a, b) => {
          const aValue = a[prop].toLowerCase();
          const bValue = b[prop].toLowerCase();
          if (order === 'ascending') {
            return aValue.localeCompare(bValue);
          } else if (order === 'descending') {
            return bValue.localeCompare(aValue);
          }
          return 0;
        });
      }
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
</style>
