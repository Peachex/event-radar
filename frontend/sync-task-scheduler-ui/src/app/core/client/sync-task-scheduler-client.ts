import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ExceptionResponse } from '../model/exception-response';
import { PaginatedTasksResponse } from '../model/paginated-tasks-response';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  private readonly syncTaskSchedulerPort: string = '8089';

  // Tasks APIs
  private readonly retrieveAllTasksApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks`;
  private readonly retrieveAllTasksPaginatedApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks/all`;
  private readonly retrieveTasksByFieldsApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks/search`;
  private readonly retrieveTasksByFieldsPaginatedApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks/search/pagination`;
  private readonly deleteTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks`;
  private readonly createTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks`;

  // Scheduler APIs
  private readonly triggerTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/scheduler/run`;
  private readonly pauseTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/scheduler/pause`;
  private readonly resumeTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/scheduler/resume`;

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasks(): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error.error, error)
        );
      })
    );
  }

  retrieveAllTasksPaginated(page: number, size: number): Observable<PaginatedTasksResponse> {
    const body = {
      page,
      size,
    };

    return this.httpClient.post<PaginatedTasksResponse>(this.retrieveAllTasksPaginatedApiUrl, body).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error.error, error)
        );
      })
    );
  }

  retrieveTasksByFields(searchByFieldsRequest: SearchByFieldsRequest, isCombinedMatch: boolean): Observable<Task[]> {
    const params = new HttpParams().set('isCombinedMatch', isCombinedMatch);
    return this.httpClient.post<Task[]>(this.retrieveTasksByFieldsApiUrl, searchByFieldsRequest, { params }).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error.error, error)
        );
      })
    );
  }

  //todo: Create model for search by fields paginated request and update the cliebt method below.
  retrieveTasksByFieldsPaginated(
    searchByFieldsRequest: SearchByFieldsRequest,
    isCombinedMatch: boolean
  ): Observable<Task[]> {
    const params = new HttpParams().set('isCombinedMatch', isCombinedMatch);
    return this.httpClient.post<Task[]>(this.retrieveTasksByFieldsApiUrl, searchByFieldsRequest, { params }).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error.error, error)
        );
      })
    );
  }

  deleteTask(taskId: number): Observable<Task> {
    const finalUrl: string = this.deleteTaskApiUrl + '/' + taskId;
    return this.httpClient.delete<Task>(finalUrl, {});
  }

  triggerTask(taskId: number): Observable<{}> {
    const finalUrl: string = this.triggerTaskApiUrl + '/' + taskId;
    return this.httpClient.post(finalUrl, {});
  }

  pauseTask(taskId: number): Observable<Task> {
    const finalUrl: string = this.pauseTaskApiUrl + '/' + taskId;
    return this.httpClient.post<Task>(finalUrl, {});
  }

  resumeTask(taskId: number): Observable<Task> {
    const finalUrl: string = this.resumeTaskApiUrl + '/' + taskId;
    return this.httpClient.post<Task>(finalUrl, {});
  }

  createTask(task: Task): Observable<Task> {
    return this.httpClient.post<Task>(this.createTaskApiUrl, task);
  }
}
