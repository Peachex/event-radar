import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable } from 'rxjs';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ErrorUtil } from '../util/error-util';
import { EventManagerClient } from '../client/event-manager-client';
import { EventManagerTaskId } from '../model/event-manager-task-id';
import { PaginatedTasksResponse } from '../model/paginated-tasks-response';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private taskClient: SyncTaskSchedulerClient, private eventManagerClient: EventManagerClient) {}

  findTasksBySearchQuery(searchQuery: string): Observable<Task[]> {
    const searchRequest: SearchByFieldsRequest = {} as SearchByFieldsRequest;

    Task.getTaskFields().forEach((field) => {
      searchRequest[field] = searchQuery;
    });

    return this.taskClient.retrieveTasksByFields(searchRequest, false);
  }

  findAllTasksIds(): Observable<EventManagerTaskId[]> {
    return this.eventManagerClient.retrieveAllTasksIds();
  }

  findAllTasks(): Observable<Task[]> {
    return this.taskClient.retrieveAllTasks();
  }

  findAllTasksPaginated(page: number, size: number): Observable<PaginatedTasksResponse> {
    return this.taskClient.retrieveAllTasksPaginated(page, size);
  }

  deleteTask(task: Task): Observable<string> {
    return this.taskClient.deleteTask(task.id).pipe(
      map(() => 'Task was deleted successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }

  createTask(task: Task): Observable<string> {
    return this.taskClient.createTask(task).pipe(
      map(() => 'Task was created successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }
}
