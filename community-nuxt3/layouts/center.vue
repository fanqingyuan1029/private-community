<template>
    <div class="home-container" ref="containerRef">
        <n-space vertical size="large">
            <n-layout style="height: 100%;">
                <!-- 页头 -->
                <n-layout-header style="background-color: gold;height: 100px;border-bottom: solid 1px gainsboro;">
                    <div style="padding-top: 5px;">
                        <n-space justify="space-around" size="large" align="center">
                            <!-- <n-button>Oops!</n-button> -->
                            <n-gradient-text size="60" gradient="linear-gradient(90deg, red 0%, green 50%, blue 100%)">
                                颐养社区系统问卷中心
                            </n-gradient-text>

                        </n-space>
                    </div>
                </n-layout-header>
                <!-- 页中 -->
                <n-layout-content style="height: calc(100vh - 150px);overflow: hidden;">
                    <slot />
                </n-layout-content>
                <!-- 页尾 -->
                <n-layout-footer style="background-color: antiquewhite;height: 50px;border-top: solid 1px gainsboro;">
                    <n-carousel autoplay>
                        <div style="display: flex;align-items: center;flex-direction: column;">
                            <n-gradient-text size="24">老有所养，老有所依。——鲁迅</n-gradient-text>
                        </div>
                        <div style="display: flex;align-items: center;flex-direction: column;">
                            <n-gradient-text size="24">互联网并非法外之地！——周迅</n-gradient-text>
                        </div>
                        <div style="display: flex;align-items: center;flex-direction: column;">
                            <n-gradient-text size="24">现在就想要8小时，以后想要什么，我都不敢想。——周树人</n-gradient-text>
                        </div>
                        <div style="display: flex;align-items: center;flex-direction: column;">
                            <n-gradient-text size="24">美国人这是搬起石头砸自己的脚！——鲁树人</n-gradient-text>
                        </div>
                    </n-carousel>
                    <!-- <div style="display: flex;align-items: center;flex-direction: column;">
                        <n-gradient-text size="24">老有所养，老有所依。——鲁迅</n-gradient-text>
                    </div> -->
                </n-layout-footer>
            </n-layout>
        </n-space>
        <div v-if="route.path !== '/login'" style="position: absolute;z-index: 888;top: 35px;right: 35px;">
            <n-popover trigger="hover" v-if="store.userId === 0">
                <template #trigger>
                    <n-button @click="router.push({ path: '/login' })">登陆</n-button>
                </template>
                <span>点此进入登录页</span>
            </n-popover>
            <div v-else style="text-align: center;">
                <div>当前用户</div>
                <n-popconfirm @positive-click="handlePositiveClick" @negative-click="handleNegativeClick">
                    <template #trigger>
                        <div>{{ store.userName }}</div>
                    </template>
                    确定要切换账户吗
                </n-popconfirm>
            </div>
        </div>
    </div>

</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useStore } from '~~/stores/userState';
// import { useRoute, useRouter } from 'vue-router';

// const route = useRoute()
// const router = useRouter()
const route = useRoute()
const router = useRouter()
const store = useStore()
const containerRef = ref<HTMLElement>(null as unknown as HTMLElement)

const handlePositiveClick = () => {
    store.resetStore()
    router.push({ path: '/login' })
}
const handleNegativeClick = () => {

}
</script>

<style scoped>
.home-container {
    /* height: 100%;
    width: 100%; */
    position: relative;
    border-left: solid 1px gainsboro;
    border-right: solid 1px gainsboro;
}
</style>