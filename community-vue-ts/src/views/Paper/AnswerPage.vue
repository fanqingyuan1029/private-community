<template>
    <div class="paperPage-container">
        <!-- <div style="width:80%;position: absolute;">
            <n-alert title="Welcome" type="info" closable>
                欢迎来到颐养社区问卷中心，您的反馈是我们前进的动力
            </n-alert>
        </div> -->
        <div style="margin: 15px;font-size: larger;font-weight: bolder;position: absolute;z-index: 666;"
            v-if="currentOldMan">当前老人:
            <n-popconfirm positive-text="老人的回答详情" negative-text="更换选中的老人"
                @positive-click="router.push({ name: 'OldMan' })" @negative-click="currentOldMan = null">
                <template #trigger>
                    <n-button text>
                        {{ currentOldMan.name }}
                    </n-button>
                </template>
                编号:{{ currentOldMan.id }}
            </n-popconfirm>
        </div>
        <div v-if="loading || oldmanLoading">
            <n-spin size="large" />
        </div>
        <div v-else style="width: 100%;height: 100%;overflow: hidden;">
            <Transition name="hello">
                <div v-if="!currentOldMan" style="margin: 10px;">
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
                                    <n-button type="primary" @click="chooseOldMan(oldMan)">选择</n-button>
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
                <div v-else-if="!currentPaper && currentOldMan" style="margin: 10px;">
                    <!-- 在这里先选问卷呢 -->
                    <n-space vertical>
                        <span style="font-size: xx-large;font-weight: bolder;">请选择要回答的问卷</span>
                        <div v-for="(paper, index) in papers" :key="paper.id">
                            <paper-component :paper="paper" :choosePaper="choosePaper"></paper-component>
                        </div>

                    </n-space>
                </div>
                <div v-else style="height: 100%;">
                    <n-card hoverable style="height: 100%;">
                        <div style="margin-top: 20px;display: flex;align-items: center;justify-content:space-between">
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
                            <n-popconfirm @positive-click="finallyPaperAnswerCommit" v-if="isCommitButtonShow">
                                <template #trigger>
                                    <n-button strong type="primary" size="large">
                                        <span style="font-size: 30px;font-weight: bold;">提交</span>
                                    </n-button>
                                </template>
                                <span style="font-size: larger;">确定要提交回答吗？</span>
                            </n-popconfirm>
                            <n-button v-else strong type="primary" size="large" disabled>
                                <span style="font-size: 30px;font-weight: bold;">提交</span>
                            </n-button>
                        </div>
                        <n-divider></n-divider>
                        <div class="paperText" style="display: flex;height: 100% ;align-items: center;width: 100%;">
                            <div style="flex: 1;text-align: center;margin-left: 0;">
                                <button size="large" :disabled="leftButtonDisable" @click="leftButtonOnClick">左</button>
                            </div>
                            <div style="flex:1"></div>
                            <div style="flex: 8;width: auto;align-self:stretch;">
                                <!-- {{currentPaper.questions[currentIndex]}} -->
                                <Transition name="question">
                                    <question-component
                                        v-if="currentPaper.questions && currentPaper.questions.length > 0 && changeQuestion"
                                        :question="currentPaper.questions[currentIndex]" :chooseOption="chooseOption"
                                        :index="currentIndex" :chooseIndex="answerList[currentIndex]">
                                    </question-component>
                                </Transition>
                                <!-- <div v-for="(question, index) in currentPaper.questions">
                                    <question-component v-if="index === currentIndex" :question="question"
                                        :chooseOption="chooseOption" :index="index">
                                    </question-component>

                                </div> -->
                            </div>
                            <div style="flex:1"></div>
                            <div style="flex: 1;text-align: center;margin-right: 0;">
                                <button size="large" :disabled="rightButtonDisable"
                                    @click="rightButtonOnClick">右</button>
                            </div>
                        </div>
                    </n-card>
                    <div style="position: absolute;bottom: 10px;left: 10px;">
                        <n-card>
                            <span>{{
                                    count
                            }}/{{ answerList.length }}</span>
                        </n-card>
                    </div>
                </div>
            </Transition>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import Paper from '../../types/Paper';
import PaperComponent from '../../components/Paper.vue'
import QuestionComponent from '../../components/Question.vue'
import getPapersRequest from '../../api/getPapersRequest';
import { useRouter } from 'vue-router';
import { useMessage, useNotification } from 'naive-ui';
import getOldMenRequest from '../../api/getOldMenRequest';
import OldMan from '../../types/OldMan';
import { useStore } from '../../store/store';
import http from '../../api/useHttp';

// 发送请求获取Paper列表以供选择
const { loading, papers } = getPapersRequest();
// 发请求拿到所有的老人列表供选择
const { loading: oldmanLoading, oldMen } = getOldMenRequest();

//弹出欢迎提示
const notification = useNotification()
notification.info({ content: '欢迎！', meta: '欢迎来到颐养社区问卷中心，您的反馈是我们前进的动力' })

const store = useStore();
// 当前选定的老人编号
const currentOldMan = ref<OldMan>();
// 选定老人事件
const chooseOldMan = (oldMan: OldMan) => {
    currentOldMan.value = oldMan;
    store.setOldMan(oldMan);
}

// 当前选定后正在做的问卷信息
const currentPaper = ref<Paper>();
// 回答的集合
const answerList = ref<Array<number>>([]);
// 选取问卷后的处理
const choosePaper = (paper: Paper) => {
    const questions = paper.questions;
    answerList.value = [];
    currentIndex.value = 0;
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
    console.log(currentPaper.value.id, currentOldMan.value.id, answerList.value, currentPaper.value.version)
    // 向后台发送post请求
    if (commitLock.value === false) {
        commitLock.value = true;
        http.post('/paper/answer', {
            publishPaperId: currentPaper.value.id,
            publishPaperVersion: currentPaper.value.version,
            oldManId: currentOldMan.value.id,
            answer: JSON.stringify(answerList.value),
        }).then((res) => {
            if (res.data.code === 200) {
                message.success('提交成功!');
                router.push({ name: 'Main' });
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

// 左右选择题目
const currentIndex = ref<number>(0)
const leftButtonDisable = computed<boolean>(() => {
    if (currentIndex.value > 0) {
        return false;
    } else {
        return true;
    }
})
const rightButtonDisable = computed<boolean>(() => {
    if (currentIndex.value < answerList.value.length - 1) {
        return false;
    } else {
        return true;
    }
})

/*
 * 做题相关部分
 */
const changeQuestion = ref<boolean>(true)
const goRight: string = 'translateX(-900px)'
const goLeft: string = 'translateX(900px)'
const paperQuestionEnterFrom = ref<string>(goLeft)
const paperQuestionLeaveTo = ref<string>(goRight)

const leftButtonOnClick = () => {
    if (changeQuestion.value) {
        if (currentIndex.value > 0) {
            paperQuestionEnterFrom.value = goRight
            paperQuestionLeaveTo.value = goLeft
            changeQuestion.value = false;
            currentIndex.value--;
            setTimeout(() => changeQuestion.value = true, 400)
        } else {

        }
    }
    // changeQuestion.value = true;
}
const rightButtonOnClick = () => {
    if (changeQuestion.value) {
        if (currentIndex.value < answerList.value.length - 1) {
            paperQuestionEnterFrom.value = goLeft
            paperQuestionLeaveTo.value = goRight
            changeQuestion.value = false;
            currentIndex.value++;
            setTimeout(() => changeQuestion.value = true, 400)
        } else {

        }
    }
    // changeQuestion.value = true;
}


const prevKeys: string[] = ["ARROWUP", "PAGEUP", "ARROWLEFT"]; // 上翻页快捷键集合
const nextKeys: string[] = ["ARROWDOWN", "ENTER", "PAGEDOWN", "ARROWRIGHT"]; // 下翻页快捷键集合

// 快捷键翻页
const keydownListener = (e: KeyboardEvent) => {
    const key = e.key.toUpperCase();
    if (prevKeys.includes(key)) {
        leftButtonOnClick()
        // 执行上翻页操作
    } else if (nextKeys.includes(key)) {
        rightButtonOnClick()
        // 执行下翻页操作
    };
};

onMounted(() => {
    document.addEventListener("keydown", keydownListener);
});
onUnmounted(() => {
    document.removeEventListener("keydown", keydownListener);
});


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
    transition: all 0.5s ease;
}

.hello-enter-from {
    opacity: 0;
    transform: scale(0.5, 0.5);
    position: absolute;
}

.hello-leave-to {
    opacity: 0;
    transform: scale(0.2, 0.2);
    position: absolute;
}

.question-enter-active,
.question-leave-active {
    transition: all 0.4s ease;
}

.question-enter-from {
    opacity: 0;
    transform: v-bind(paperQuestionEnterFrom);
}

.question-leave-to {
    opacity: 0;
    transform: v-bind(paperQuestionLeaveTo);
}
</style>