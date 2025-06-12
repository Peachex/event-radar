import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ExceptionResponse } from '../model/exception-response';
import { PaginatedTasksResponse } from '../model/paginated-tasks-response';
import { SearchByFieldsPaginatedRequest } from '../model/search-by-field-paginated-request';
import { SortField } from '../model/sort-field';
import { ConfigLoader } from '../util/config-loader';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  config = ConfigLoader.get();

  private readonly syncTaskSchedulerHost: string = this.config.syncTaskSchedulerHost;
  private readonly syncTaskSchedulerPort: string = this.config.syncTaskSchedulerPort;
  private readonly syncTaskSchedulerContextPath: string = this.config.syncTaskSchedulerContextPath;

  // Tasks APIs
  private readonly retrieveAllTasksApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks`;
  private readonly retrieveAllTasksPaginatedApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks/all`;
  private readonly retrieveTasksByFieldsApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks/search`;
  private readonly retrieveTasksByFieldsPaginatedApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks/search/pagination`;
  private readonly deleteTaskApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks`;
  private readonly createTaskApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/tasks`;

  // Scheduler APIs
  private readonly triggerTaskApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/scheduler/run`;
  private readonly pauseTaskApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/scheduler/pause`;
  private readonly resumeTaskApiUrl: string = `${this.syncTaskSchedulerHost}:${this.syncTaskSchedulerPort}/${this.syncTaskSchedulerContextPath}/scheduler/resume`;

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

  retrieveAllTasksPaginated(page: number, size: number, sortFields?: SortField[]): Observable<PaginatedTasksResponse> {
    const body = {
      page,
      size,
      sortFields,
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

  retrieveTasksByFieldsPaginated(
    searchByFieldsPaginatedRequest: SearchByFieldsPaginatedRequest
  ): Observable<PaginatedTasksResponse> {
    return this.httpClient
      .post<PaginatedTasksResponse>(this.retrieveTasksByFieldsPaginatedApiUrl, searchByFieldsPaginatedRequest, {})
      .pipe(
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
