<template>
    <div style="display: flex;">
        <div style="width: 50px;">
            <div v-if="see">
                <n-icon size="40" color="#0e7a0d" v-if="redOnClick">
                    <span>中</span>
                </n-icon>
            </div>
            <Transition name="ok" v-else>
                <n-icon size="40" color="#0e7a0d" v-if="redOnClick">
                    <span>中</span>
                </n-icon>
            </Transition>
        </div>
        <div style="font-size: 24px;font-weight: 500;" @click="onClickOption(index)">
            {{ option }}
        </div>
    </div>
</template>

<script lang="ts" setup>
// import { CheckmarkCircle } from '@vicons/ionicons5'
import { computed } from 'vue'

const props = defineProps({
    option: {
        type: String,
        require: true
    },
    clickOption: {
        type: Function,
        require: true
    },
    index: {
        type: Number,
        require: true
    },
    chooseIndex: {
        type: Number,
        require: true
    },
    //查看回答相关
    see: {
        type: Boolean,
        default: false
    },
    answerIndex: {
        type: Number
    }
})

// const bgColor = ref('white')
// const myColor = ref('black')

const onClickOption = (optionIndex: number) => {
    if (props.clickOption) {
        props.clickOption(optionIndex)
        //...
    }
}

const redOnClick = computed(() => {
    if (props.see) {
        // 是看答案
        if (props.answerIndex) {
            if (props.answerIndex - 1 === props.index) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    } else {
        // 是做题
        if (props.chooseIndex) {
            if (props.chooseIndex - 1 === props.index) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

})
</script>

<style scoped>
.ok-enter-active,
.ok-leave-active {
    transition: opacity 0.5s ease;
}

.ok-enter-from,
.ok-leave-to {
    opacity: 0;
}
</style>