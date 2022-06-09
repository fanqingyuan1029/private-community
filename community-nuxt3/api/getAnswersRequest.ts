// @ts-ignore
import { reactive, ref } from "vue";
import Answer from "~~/types/Answer";
import Patient from "~~/types/Patient";
import http from "~~/api/useHttp";

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
        const result: Patient = response.data.data;
        if (result instanceof Array) {
          for (let i = 0; i < result.length; i++) {
            answers.push({
              id: result[i].id,
              publishPaperId: result[i].publishPaperId,
              publishPaperVersion: result[i].publishPaperVersion,
              patientId: result[i].patientId,
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
