import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { useStore } from "../store/store";

// 仅做示例，根据业务修改路径等
const Login = () => import("../views/Login/LoginPage.vue");
const Main = () => import("../views/Main/MainPage.vue");
const Paper = () => import("../views/Paper/AnswerPage.vue");
const OldMan = () => import("../views/OldMan/OldManPage.vue");

const routes: RouteRecordRaw[] = [
  {
    path: "/",
    component: Main,
    name: "Main",
    meta: {
      title: "主页",
    },
  },
  {
    path: "/login",
    component: Login,
    name: "Login",
    meta: {
      title: "登录页",
    },
  },
  {
    path: "/paper",
    component: Paper,
    name: "Paper",
    meta: {
      title: "问卷页",
      requireAuth: true,
    },
  },
  {
    path: "/oldMan",
    component: OldMan,
    name: "OldMan",
    meta: {
      title: "老人页",
      requireAuth: true,
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 前置路由
router.beforeEach((to, from, next) => {
  const store = useStore();
  if (to.meta.requireAuth) {
    console.log(store.userId);
    if (store.userId !== 0) {
      next();
    } else {
      console.log("这里要登陆");
      next("/login");
    }
  } else {
    next();
  }
});

export default router;
