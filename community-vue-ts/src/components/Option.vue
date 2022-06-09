<template>
    <div style="display: flex;">
        <div style="width: 50px;">
            <Transition name="ok">
                <n-icon size="40" color="#0e7a0d" v-if="redOnClick">
                    <CheckmarkCircle />
                </n-icon>
            </Transition>
        </div>
        <div style="font-size: 24px;font-weight: 500;" @click="onClickOption(index)">
            {{ option }}
        </div>
    </div>
</template>

<script lang="ts" setup>
import { CheckmarkCircle } from '@vicons/ionicons5'
import { ref, computed } from 'vue'

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
    if (props.chooseIndex) {
        if (props.chooseIndex - 1 === props.index) {
            return true;
        } else {
            return false;
        }
    }
    return false;
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