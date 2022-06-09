<template>
    <div style="margin-top:10px;width: 100%;height: 100%;">
        <div style="margin-top:5px;">
            <span style="font-size: 36px;font-weight: bolder;"> {{ index + 1 }}:{{ question.name }}</span>
        </div>
        <n-divider></n-divider>
        <!-- <div style="display: flex;flex-direction: column;"> -->
            <div style="margin-top:10px;min-height: 140px;" v-for="(option, index) in question.options" :key="index">
                <option-component :option="option" :clickOption="clickOption" :index="index"
                    :chooseIndex="chooseIndex">
                </option-component>
            </div>
        <!-- </div> -->
    </div>
</template>

<script setup lang="ts">
import Question from '../types/Question'
import OptionComponent from './Option.vue';

const props = defineProps({
    // 当前组件所显示的问题的信息
    question: {
        type: Object as () => Question,
        require: true
    },
    // 改变已经选择的选项的函数
    chooseOption: {
        type: Function,
        require: false
    },
    // 当前组件中问题被选中的选项序号,0代表未选中
    chooseIndex: {
        type: Number,
        require: false,
        default: 0
    },
    index: {
        type: Number,
        require: true
    },
})
/**
 * 选中的哪一个,默认0是没有选中
 */
// const chooseIndex = ref(0)

const clickOption = (optionIndex: number) => {
    if (props.chooseOption) {
        const flag = props.chooseOption(props.index, optionIndex)
        if (flag) {
            //对应选项标红,其他取消红
            // chooseIndex.value = optionIndex + 1
        }
    }
}
</script>

<style scoped>
</style>