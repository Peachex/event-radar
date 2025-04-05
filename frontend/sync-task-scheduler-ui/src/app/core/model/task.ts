import { TaskStatus } from './task-status';

export interface Task {
  id: number;
  status: TaskStatus;
  name: string;
  description?: string;
  taskIdToExecute: string;
  cronExpression: string;
}
