<template>
    <div class="mainPage" style="height: 100%;width: 100%;display: flex;flex-direction: column;align-items: center;">
        <n-button size="large" round type="primary" style="width: 125px;margin-top: auto;margin-bottom: 10px;" @click="goPaper">马上去做问卷！</n-button>
    </div>

</template>

<script setup lang="ts">
const router = useRouter()
const goPaper = () => {
    router.push('/paper');
}
</script>

<style scoped>
.mainPage{
    background: url("/logo.png") no-repeat;
    background-size: cover;
}
</style>