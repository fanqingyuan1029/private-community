<script setup lang="ts">
// This starter template is using Vue 3 <script setup> SFCs
// Check out https://vuejs.org/api/sfc-script-setup.html#script-setup
import Home from './views/Home.vue';
import { zhCN } from 'naive-ui'
</script>

<template>
  <!-- <img alt="Vue logo" src="./assets/logo.png" />
  <HelloWorld msg="Hello Vue 3 + TypeScript + Vite" /> -->
  <n-config-provider :locale="zhCN">
    <n-message-provider>
      <n-notification-provider>
        <Home></Home>
      </n-notification-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
  min-width: 760px;
  margin-left: 4px;
  margin-right: 4px;
  /* margin-top: 60px; */
}
</style>
