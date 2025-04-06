import { TaskStatus } from './task-status';

export class Task {
  id: number;
  status: TaskStatus;
  name: string;
  description?: string;
  taskIdToExecute: string;
  cronExpression: string;

  constructor(
    id: number,
    status: TaskStatus,
    name: string,
    taskIdToExecute: string,
    cronExpression: string,
    description?: string
  ) {
    this.id = id;
    this.status = status;
    this.name = name;
    this.description = description;
    this.taskIdToExecute = taskIdToExecute;
    this.cronExpression = cronExpression;
  }

  static getTaskFields(): (keyof Task)[] {
    return ['status', 'name', 'description', 'taskIdToExecute', 'cronExpression'];
  }
}
