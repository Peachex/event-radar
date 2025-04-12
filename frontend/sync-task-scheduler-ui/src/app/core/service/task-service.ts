import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable } from 'rxjs';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ErrorUtil } from '../util/error-util';

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

  deleteTask(task: Task): Observable<string> {
    return this.taskClient.deleteTask(task.id).pipe(
      map(() => 'Task was deleted successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }
}
