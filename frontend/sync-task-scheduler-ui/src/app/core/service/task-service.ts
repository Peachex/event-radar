import { Injectable } from '@angular/core';
import { TaskStatus } from '../model/task-status';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { Observable } from 'rxjs';
import { SearchByFieldsRequest } from '../model/search-by-field-request';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private schedulesResponse: Task[] = [
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
      name: 'SomeLongTaskName#2',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 63,
      status: TaskStatus.ACTIVE,
      name: 'SyncTask#3',
      description: 'Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 412,
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

  constructor(private taskClient: SyncTaskSchedulerClient) {}

  retrieveTasksSchedulesFromBackendAPI(searchQuery: string): Task[] {
    // Simulate response from backend API

    let filtered = this.schedulesResponse.filter(
      (task) =>
        task.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (task.description && task.description.toLowerCase().includes(searchQuery.toLowerCase())) || // Check if description exists
        task.status.toLowerCase().includes(searchQuery.toLowerCase()) ||
        task.taskIdToExecute.toLowerCase().includes(searchQuery.toLowerCase())
    );

    console.log('Filtere tasks: ' + filtered);

    return filtered;
  }

  findTasksBySearchQuery(searchQuery: string): Observable<Task[]> {
    const searchRequest: SearchByFieldsRequest = {} as SearchByFieldsRequest;

    Task.getTaskFields().forEach((field) => {
      searchRequest[field] = searchQuery;
    });

    return this.taskClient.retrieveTasksByFields(searchRequest, false);
  }

  findAllTasks(): Observable<Task[]> {
    return this.taskClient.retrieveAllTasks();
  }
}
