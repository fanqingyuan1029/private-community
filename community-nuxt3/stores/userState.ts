import { defineStore } from "pinia";
import Paper from "~~/types/Paper";
import Patient from "~~/types/Patient";

// useStore 可以任意，比如 useUser, useCart
// 参数1是整个应用中唯一的store id
export const useStore = defineStore({
  // id: 必须的，在所有 Store 中唯一
  id: "UserState",
  // state: 返回对象的函数
  state: () => ({
    userId: 0,
    userName: "还没有登录",
    name: "还没有登录",
    userType: 0,
    patient: undefined as unknown as Patient,
    papers: undefined as unknown as Array<Paper>,
  }),
  getters: {},
  actions: {
    setUserId(id: number) {
      console.log("id:" + id);
      this.userId = id;
      console.log(this.userId);
    },
    setUserName(username: string) {
      console.log("username:" + username);
      this.userName = username;
      console.log(this.userName);
    },
    setName(name: string) {
      console.log("name:" + name);
      this.name = name;
      console.log(this.name);
    },
    setUserType(type: number) {
      this.userType = type;
    },
    setPatient(patient: Patient) {
      this.patient = patient;
    },
    setPapers(paperArray: Array<Paper>) {
      this.papers = paperArray;
    },
    resetStore() {
      this.userId = 0;
      this.userName = "还没有登陆";
      this.name = "还没有登陆";
      this.userType = 0;
      this.patient = undefined as unknown as Patient;
      this.papers = undefined as unknown as Array<Paper>;
    },
  },
});
