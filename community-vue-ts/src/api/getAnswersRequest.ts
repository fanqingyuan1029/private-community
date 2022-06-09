import { reactive, ref } from "vue";
import Answer from "../types/Answer";
import OldMan from "../types/OldMan";
import http from "./useHttp";

/**
 * 向后台发送请求拿到所有的回答
 * @returns loading 是否在加载 answers 回答信息集合 errorMsg 错误信息
 */
export default function () {
  // 加载的状态
  const loading = ref(true);
  // 数据
  let answers = reactive<Answer[]>([]);
  // 错误信息
  const errorMsg = ref("");
  // 发送请求
  http
    .get("/paper/answer/all")
    .then((response) => {
      // 改变加载状态
      loading.value = false;
      if (response.data.code === 200) {
        const result: OldMan = response.data.data;
        if (result instanceof Array) {
          for (let i = 0; i < result.length; i++) {
            answers.push({
              id: result[i].id,
              publishPaperId: result[i].publishPaperId,
              publishPaperVersion: result[i].publishPaperVersion,
              oldManId: result[i].oldManId,
              answer: JSON.parse(result[i].answer),
              createTime: result[i].createTime,
            });
          }
        }
      } else {
        errorMsg.value = response.data.message || "未知错误";
      }
    })
    .catch((error) => {
      // 改变加载状态
      loading.value = false;
      errorMsg.value = error.message || "未知错误";
    });
  return {
    loading,
    answers,
    errorMsg,
  };
}
