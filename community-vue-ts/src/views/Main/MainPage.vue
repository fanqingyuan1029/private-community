<template>
    <div class="mainPage">
        <n-button size="large" round type="primary" style="width: 125px;margin-top: auto;margin-bottom: 10px;"
            @click="goPaper">马上去做问卷！</n-button>
    </div>

</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter()
const goPaper = () => {
    router.push('/paper');
}
</script>

<style scoped>
.mainPage {
    background: url("./home.png") center center no-repeat;
    background-color: gainsboro;
    background-size: contain;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>