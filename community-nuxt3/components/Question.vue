<template>
    <div style="margin-top:10px;">
        <div style="margin-top:5px;">
            <span style="font-size: 36px;font-weight: bolder;"> {{ index + 1 }}:{{ question.name }}</span>
        </div>
        <div style="margin-top:5px;" v-for="(option, index) in question.options" :key="index">
            <OptionVue :option="option.toString()" :clickOption="clickOption" :index="index"
                :chooseIndex="chooseIndex" :see="see" :answerIndex="answerIndex">
            </OptionVue>
        </div>
    </div>
</template>

<script setup lang="ts">
import Question from '~~/types/Question'
import OptionVue from '~~/components/Option.vue'
import { ref } from 'vue'

const props = defineProps({
    question: {
        type: Object as () => Question,
        require: true
    },
    chooseOption: {
        type: Function,
        require: false
    },
    index: {
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
/**
 * 选中的哪一个,默认0是没有选中
 */
const chooseIndex = ref(0)

const clickOption = (optionIndex: number) => {
    if (props.chooseOption) {
        const flag = props.chooseOption(props.index, optionIndex)
        if (flag) {
            //对应选项标红,其他取消红
            chooseIndex.value = optionIndex + 1
        }
    }
}
</script>

<style scoped>
</style>