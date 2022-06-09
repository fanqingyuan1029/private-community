import { defineStore } from "pinia";
import Paper from "../types/Paper";
import OldMan from "../types/OldMan";

// defineStore 调用后返回一个函数，调用该函数获得 Store 实体
export const useStore = defineStore({
  // id: 必须的，在所有 Store 中唯一
  id: "UserState",
  // state: 返回对象的函数
  state: () => ({
    userId: 0,
    userName: "还没有登录",
    name: "还没有登录",
    userType: 0,
    oldMan: undefined as unknown as OldMan,
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
    setOldMan(oldman: OldMan) {
      this.oldMan = oldman;
    },
    setPapers(paperArray: Array<Paper>) {
      this.papers = paperArray;
    },
    resetStore() {
      this.userId = 0;
      this.userName = "还没有登陆";
      this.name = "还没有登陆";
      this.userType = 0;
      this.oldMan = undefined as unknown as OldMan;
      this.papers = undefined as unknown as Array<Paper>;
    },
  },
});
