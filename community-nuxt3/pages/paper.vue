<template>
    <div class="paperPage-container" v-if="store.userId !== 0">
        <div style="width:80%;">
            <n-alert title="Welcome" type="info" closable>
                欢迎来到颐养社区问卷中心，您的反馈是我们前进的动力
            </n-alert>
        </div>
        <div style="font-size: larger;font-weight: bolder;" v-if="currentPatient">当前老人:
            <n-popconfirm positive-text="老人的回答详情" negative-text="更换选中的老人"
                @positive-click="router.push({ path: '/oldMan' })" @negative-click="currentPatient = null">
                <template #trigger>
                    <n-button text>
                        {{ currentPatient.name }}
                    </n-button>
                </template>
                编号:{{ currentPatient.id }}
            </n-popconfirm>
        </div>
        <div v-if="loading || oldmanLoading">
            <n-spin size="large" />
        </div>
        <div v-else style="margin-top: 10px;width: 90%;">
            <Transition name="hello">
                <div v-if="!currentPatient">
                    <span style="font-size: xx-large;font-weight: bolder;">请选择回答问卷的老人</span>
                    <n-table :single-line="false">
                        <thead>
                            <tr>
                                <th>编号</th>
                                <th>姓名</th>
                                <th>性别</th>
                                <th>年龄</th>
                                <th>紧急联系人姓名</th>
                                <th>紧急联系人电话</th>
                                <th>录入时间</th>
                                <th>信息最后修改时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(oldMan, index) in oldMen" :key="oldMan.id">
                                <td>{{ oldMan.id }}</td>
                                <td><span style="font-weight: bold;">{{ oldMan.name }}</span> </td>
                                <td>{{ oldMan.sex === 1 ? '男' : '女' }}</td>
                                <td>{{ oldMan.age }}</td>
                                <td>{{ oldMan.contactName }}</td>
                                <td>{{ oldMan.contactPhone }}</td>
                                <td>{{ oldMan.createTime }}</td>
                                <td>{{ oldMan.lastUpdateTime }}</td>
                                <td>
                                    <n-button type="primary" @click="choosePatient(oldMan)">选择</n-button>
                                </td>
                            </tr>
                            <!-- <tr>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                            <td>...</td>
                        </tr> -->
                        </tbody>
                    </n-table>
                    <!-- 先选老人 {{ oldMen }} -->
                    <!-- <div v-for="(oldMan, index) in oldMen" :key="oldMan.id">
                    {{ oldMan }}
                </div> -->
                </div>
                <!-- {{ answerList }} -->
                <div v-else-if="!currentPaper && currentPatient">
                    <!-- 在这里先选问卷呢 -->
                    <n-space vertical>

                        <div v-for="(paper, index) in papers" :key="paper.id">
                            <paper-component :paper="paper" :choosePaper="choosePaper"></paper-component>
                        </div>

                    </n-space>
                </div>
                <div v-else>
                    <n-card>
                        <div style="display: flex;align-items: center;justify-content:space-between">


                            <n-popconfirm @positive-click="currentPaper = null">
                                <template #trigger>
                                    <n-button strong size="large">
                                        <span style="font-size: 30px;font-weight: bold;">返回</span>
                                    </n-button>
                                </template>
                                <span style="font-size: larger;">确定要离开这里吗？</span>
                            </n-popconfirm>

                            <n-gradient-text type="info" size="50">
                                {{ currentPaper.name }}
                            </n-gradient-text>



                            <n-button strong type="primary" size="large" v-if="isCommitButtonShow">
                                <n-popconfirm @positive-click="finallyPaperAnswerCommit">
                                    <template #trigger>
                                        <n-button strong type="primary" size="large">
                                            <span style="font-size: 30px;font-weight: bold;">提交</span>
                                        </n-button>
                                    </template>
                                    <span style="font-size: larger;">确定要提交回答吗？</span>
                                </n-popconfirm>
                            </n-button>
                            <n-button v-else strong type="primary" size="large" disabled>
                                <span style="font-size: 30px;font-weight: bold;">提交</span>
                            </n-button>

                        </div>
                        <n-divider></n-divider>
                        <div v-for="(question, index) in currentPaper.questions">
                            <question-component :question="question" :chooseOption="chooseOption" :index="index">
                            </question-component>
                            <n-divider></n-divider>
                        </div>
                    </n-card>
                    <div style="position: absolute;bottom: 10px;left: 10px;">
                        <span>{{
                            count
                        }}/{{ answerList.length }}</span>
                    </div>
                </div>
            </Transition>
        </div>
    </div>
    <div v-else>
        <h1>还没登陆呢你</h1>
        <n-button @click="router.push({ path: '/login' })">登陆</n-button>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import Paper from '~~/types/Paper';
import PaperComponent from '~~/components/Paper.vue'
import QuestionComponent from '~~/components/Question.vue'
import getPapersRequest from '~~/api/getPapersRequest';
import { useRouter } from 'vue-router';
import { useMessage } from 'naive-ui';
import getPatientsRequest from '~~/api/getPatientsRequest';
import Patient from '~~/types/Patient';
import { useStore } from '~~/stores/userState';
import http from '~~/api/useHttp';

// 发送请求获取Paper列表以供选择
const { loading, papers } = getPapersRequest();
// 发请求拿到所有的老人列表供选择
const { loading: oldmanLoading, oldMen } = getPatientsRequest();

const store = useStore();
// 当前选定的老人编号
const currentPatient = ref<Patient>();
// 选定老人事件
const choosePatient = (oldMan: Patient) => {
    currentPatient.value = oldMan;
    store.setPatient(oldMan);
}

// 当前选定后正在做的问卷信息
const currentPaper = ref<Paper>();
// 回答的集合
const answerList = ref<Number[]>([]);
// 选取问卷后的处理
const choosePaper = (paper: Paper) => {
    const questions = paper.questions;
    answerList.value = [];
    for (let i = 0; i < questions.length; i++) {
        answerList.value.push(0);
    }
    currentPaper.value = paper;
}

// 选择选项后的处理，作为prop传递给子组件
const chooseOption = (questionIndex: number, optionIndex: number) => {
    if (answerList.value instanceof Array && questionIndex < answerList.value.length) {
        answerList.value[questionIndex] = optionIndex + 1;
        return true;
    }
    return false;
}

// 提交按钮是否展示
const isCommitButtonShow = computed(() => {
    for (let answer of answerList.value) {
        if (answer === 0) {
            return false;
        }
    }
    return true;
})

const router = useRouter();
// NaiveUI弹出消息提示框的对象
const message = useMessage();
// 防止重复提交的锁
const commitLock = ref(false);
// 最终问卷的提交
const finallyPaperAnswerCommit = () => {
    console.log(currentPaper.value.id, currentPatient.value.id, answerList.value, currentPaper.value.version)
    // 向后台发送post请求
    if (commitLock.value === false) {
        commitLock.value = true;
        http.post('/paper/answer', {
            publishPaperId: currentPaper.value.id,
            publishPaperVersion: currentPaper.value.version,
            patientId: currentPatient.value.id,
            answer: JSON.stringify(answerList.value),
        }).then((res) => {
            if (res.data.code === 200) {
                message.success('提交成功!');
                router.push({ path: '/' });
            } else {
                message.warning('提交暂未成功,要不您稍后再试试');
            }
            commitLock.value = false;
        }).catch((err) => {
            commitLock.value = false;
        })
    }
}

const count = computed(() => {
    if (answerList.value.length > 0) {
        const array = answerList.value.filter(answerNumber => answerNumber > 0);
        return array.length;
    }
    return 0;
})
</script>

<style scoped>
.paperPage-container {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.hello-enter-active {
    transition: all 1s ease-out;
}

.hello-leave-active {
    transition: all 1s ease;
}

.hello-enter-from {
    opacity: 0;
    transform: translateX(-900px);
    position: absolute;
}

.hello-leave-to {
    opacity: 0;
    transform: translateX(900px);
    position: absolute;
}
</style>