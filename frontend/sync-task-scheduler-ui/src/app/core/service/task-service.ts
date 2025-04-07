import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { Observable } from 'rxjs';
import { SearchByFieldsRequest } from '../model/search-by-field-request';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private taskClient: SyncTaskSchedulerClient) {}

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
