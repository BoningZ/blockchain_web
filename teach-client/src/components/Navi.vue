<template>
  <div >
    <el-menu
        :default-active="$route.path"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        router>
      <el-menu-item index="/">首页</el-menu-item>
      <el-menu-item  v-for="(v,i) in list" :key="i" :index="'/'+v.name">{{v.title}}</el-menu-item>

      <div class="flex-grow" />
      <el-menu-item><el-button type="danger" @click='logout'>登出</el-button></el-menu-item>
    </el-menu>
    <router-view></router-view>
  </div>
</template>

<script>
import {getMenuList} from '@/service/genServ.js'
import router from "@/router";
export default {
  name: 'MyNavi',
  components: {
  },
  created() {
    this.setMenu()
  },
  data() {
    return {
      list:[]
    }
  },
  methods:{
    logout() {
      this.$store.commit('logout')
      router.push('/login')
    },
    setMenu() {
      var self = this
      getMenuList()
          .then(data => {
                self.list = data.data
              }
          )
    }
  }
}
</script>
<style>
.el-menu {
  align-items: center; /* 垂直居中对齐 */
}


.flex-grow {
  flex-grow: 1;
}

</style>
