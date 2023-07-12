import
<template>
  <router-view>
  <div class="home">
<!--    <input type="button" value="登出" v-on:click="logout()">-->
    <el-button type="danger" @click='logout'>登出</el-button>
  </div>
  </router-view>
</template>

<script>

import router from '@/router/index.js'

export default {
  name: 'HomePage',
  methods:{
    logout() {
      this.$store.commit('logout')
      router.push('/login')
    }
  }
}
</script>
