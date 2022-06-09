interface Answer {
  id: number;
  publishPaperId: number;
  publishPaperVersion: number;
  patientId: number;
  answer: Array<number>;
  createTime: string;
}

export default Answer;
