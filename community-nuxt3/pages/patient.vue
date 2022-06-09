<template>
    <div v-if="store.userId !== 0">
        <div style="padding: 20px;">
            <div>
                <n-button type="info" @click="router.push('/paper')">返回</n-button>
                <span style="font-size: larger;font-weight: bold;">当前老人:{{ store.oldMan.name }}</span>
            </div>
            <div v-if="loading">
                <n-spin size="large" />
            </div>
            <div v-else style="margin-top: 20px;">

                <div v-for="(answer, index) in answers" :key="answer.id">
                    <n-card v-if="answer.patientId === store.oldMan.id" style="margin-bottom: 10px;"
                        @click="chooseAnswer(index)">
                        <!-- {{ answer }} -->
                        <n-space vertical :size="12">
                            <n-descriptions label-placement="top" bordered>
                                <n-descriptions-item>
                                    <template #label>
                                        回答编号
                                    </template>
                                    {{ answer.id }}
                                </n-descriptions-item>
                                <n-descriptions-item>
                                    <template #label>
                                        问卷编号
                                    </template>
                                    {{ answer.publishPaperId }}
                                </n-descriptions-item>
                                <n-descriptions-item>
                                    <template #label>
                                        老人编号
                                    </template>
                                    {{ answer.patientId }}
                                </n-descriptions-item>
                            </n-descriptions>
                        </n-space>
                    </n-card>
                </div>

            </div>
        </div>
        <n-modal v-model:show="showModal" transform-origin="center">
            <n-card v-if="currentAnswer && currentPaper" style="width: 600px" :title="currentPaper.name"
                :bordered="false" size="huge" role="dialog" aria-modal="true">
                <template #header-extra>
                    {{ currentAnswer.createTime }}
                </template>
                <div v-for="(question, index) in currentPaper.questions">
                    <question-component :question="question" :index="index" see
                        :answerIndex="currentAnswer.answer[index]">
                    </question-component>
                    <n-divider></n-divider>
                </div>
                <template #footer>
                    支持一下
                </template>
            </n-card>
        </n-modal>
    </div>
    <div v-else>
        <h1>还没登陆呢你</h1>
        <n-button @click="router.push({ path: '/login' })">登陆</n-button>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import QuestionComponent from '~~/components/Question.vue'
import getAnswersRequest from '~~/api/getAnswersRequest';
import { useStore } from '~~/stores/userState';
import Answer from '~~/types/Answer';
import Paper from '~~/types/Paper';


const store = useStore()
const router = useRouter()

const { loading, answers, errorMsg } = getAnswersRequest()

const showModal = ref(false)
const currentAnswer = ref<Answer>();

const currentPaper = ref<Paper>();

const chooseAnswer = (index: number) => {
    currentAnswer.value = answers[index];
    const papers = store.papers;
    for (let paper of papers) {
        if (paper.id === currentAnswer.value.publishPaperId) {
            currentPaper.value = paper;
            break;
        }
    }
    showModal.value = true
}
</script>

<style scoped>
</style>