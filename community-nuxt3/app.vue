<template>
  <div>
    <!-- <button @click="store.$patch({ count: store.count - 1 })">-</button>
    <p>{{ store.count }}</p>
    <button @click="store.count++">+</button> -->
    <!-- <NuxtWelcome /> -->
    <n-config-provider :locale="zhCN">
      <n-message-provider>
        <NuxtLayout name="center">
          <NuxtPage></NuxtPage>
        </NuxtLayout>
      </n-message-provider>
    </n-config-provider>

  </div>
</template>

<script lang="ts" setup>
import { useStore } from './stores/userState';
import { zhCN } from 'naive-ui'
const store = useStore()
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
  max-width: 1000px;
  margin-left: auto;
  margin-right: auto;
  /* margin-top: 60px; */
}
</style>
