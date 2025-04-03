import { TaskStatus } from './task-status';

export interface TaskSchedule {
  id: number;
  status: TaskStatus;
  name: string;
  description?: string;
  taskIdToExecute: string;
  cronExpression: string;
}
