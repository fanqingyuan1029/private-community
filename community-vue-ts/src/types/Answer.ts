interface Answer {
  id: number;
  publishPaperId: number;
  publishPaperVersion: number;
  oldManId: number;
  answer: Array<number>;
  createTime: string;
}

export default Answer;
