<template>
    <n-card>
        <div style="font-size: 24px;">编号:{{ paper.id }}</div>
        <div style="font-size: 30px;">{{ paper.name }}</div>
        <template #footer>
            <div style="display: flex;flex-direction: row;align-items:center;justify-content:space-between;font-size: 18px;">
                <div>轮次:{{ paper.version }}</div>
                <span>开始时间:{{ paper.registerTime }}</span>
                <span>截止时间:{{ paper.endTime }}</span>
                <n-button @click="choosePaper(paper)">开做！</n-button>
            </div>
        </template>
    </n-card>
</template>

<script setup lang="ts">
import Paper from '../types/Paper';

defineProps({
    paper: {
        type: Object as () => Paper,
        require: true
    },
    choosePaper: {
        type: Function,
        require: true
    }
})
</script>

<style scoped>
</style>