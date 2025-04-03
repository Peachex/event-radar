import { Injectable } from '@angular/core';
import { TaskStatus } from '../model/task-status';
import { TaskSchedule } from '../model/task-schedule';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private schedulesResponse: TaskSchedule[] = [
    {
      id: 1,
      status: TaskStatus.ACTIVE,
      name: 'SyncTask#1',
      description: 'Task that is needed for events sync.',
      taskIdToExecute: 'fetch_new_events_task',
      cronExpression: '0/10 * * * * ?',
    },
    {
      id: 2,
      status: TaskStatus.PAUSED,
      name: 'SyncTask#2',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 3,
      status: TaskStatus.ACTIVE,
      name: 'SyncTask#3',
      description: 'Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 4,
      status: TaskStatus.PAUSED,
      name: 'SyncTask#4',
      description:
        'Another task for syncing. Another task for syncing. Another task for syncing.Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 5,
      status: TaskStatus.PAUSED,
      name: 'SyncTask#5',
      description:
        'Some dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfome dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewfewufgewo fhiewohfoewfew fewf ewf',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
  ];

  retrieveTasksSchedulesFromBackendAPI(searchQuery: string): TaskSchedule[] {
    // Simulate response from backend API

    let filtered = this.schedulesResponse.filter(
      (task) =>
        task.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (task.description &&
          task.description.toLowerCase().includes(searchQuery.toLowerCase())) || // Check if description exists
        task.status.toLowerCase().includes(searchQuery.toLowerCase()) ||
        task.taskIdToExecute.toLowerCase().includes(searchQuery.toLowerCase())
    );

    console.log('Filtere tasks: ' + filtered);

    return filtered;
  }

  retrieveAllTasksSchedulesFromBackendAPI(): TaskSchedule[] {
    // Simulate response from backend API
    return this.schedulesResponse;
  }
}
