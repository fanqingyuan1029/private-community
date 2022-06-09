import Question from "./Question";

interface Paper {
  id: number;
  name: string;
  registerTime: string;
  endTime: string;
  version: number;
  questions: Array<Question>;
}

export default Paper;
