<template>
    <div style="height: 100%;width: 100%;">
        <div class="login-form">
            <div style="display: flex;flex-direction: column;">
                <div class="login-form-body">
                    <span style="flex: 2;text-align: center;font-size: x-large;font-weight: bold;">用户名</span>
                    <n-input v-model:value="inputUsername" style="flex:9;" size="large" round placeholder="大"
                        :maxlength="20" />
                    <span style="flex: 1;text-align: center;"></span>
                </div>
                <div class="air" />
                <div class="login-form-body">
                    <span style="flex: 2;text-align: center;font-size: x-large;font-weight: bold;">密码</span>
                    <n-input v-model:value="inputPassword" style="flex:9;" size="large" round type="password"
                        show-password-on="click" placeholder="密码" :maxlength="20" />
                    <span style="flex: 1;text-align: center;"></span>
                </div>
                <div class="air" />
                <div class="login-form-body">
                    <n-button :disabled="loginDisable" strong round type="info"
                        style="margin-left: auto;margin-right: auto;" @click="login">登陆</n-button>
                </div>
                <div v-if="errorMsg" class="login-form-body" style="color: red;margin-top: 10px;">
                    <span style="margin-left: auto;margin-right: auto;"> {{ errorMsg }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useStore } from '~~/stores/userState';
import http from '~~/api/useHttp'

const store = useStore()
const router = useRouter()
const inputUsername = ref<string>('')
const inputPassword = ref<string>('')

const loginDisable = computed(() => {
    if (inputUsername.value.trim().length > 0 || inputPassword.value.trim().length > 0) {
        return false;
    } else {
        return true;
    }
})

const loading = ref(true);
const errorMsg = ref("");
const login = () => {
    // 本地校验username和password的合法性
    if (!inputUsername || !inputUsername) {
        return;
    }
    // 发请求给服务端校验合法性,拿到用户信息存到pinia
    http.post("/user/login", {
        username: inputUsername.value,
        password: inputPassword.value,
    }).then((response) => {
        // 改变加载状态
        loading.value = false;
        if (response.data.code === 200) {
            const resultData = response.data.data;
            store.setUserId(resultData.id);
            store.setUserName(resultData.username);
            store.setName(resultData.name);
            //跳转
            router.push({ path: '/' });
        } else {
            errorMsg.value = response.data.message || "未知错误";
        }
    }).catch((error) => {
        // 改变加载状态
        loading.value = false;
        errorMsg.value = error.message || "未知错误";
    });
}
</script>

<style scoped>
.login-form {
    padding-top: 25%;
}

.air {
    margin-top: 10px;
    margin-bottom: 10px;
}

.login-form-body {
    display: flex;
    align-items: center;
}
</style>