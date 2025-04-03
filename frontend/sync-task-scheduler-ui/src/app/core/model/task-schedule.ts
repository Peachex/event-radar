import { ScheduleStatus } from './schedule-status';

export interface TaskSchedule {
  id: number;
  status: ScheduleStatus;
  name: string;
  description?: string;
  taskIdToExecute: string;
  cronExpression: string;
}
