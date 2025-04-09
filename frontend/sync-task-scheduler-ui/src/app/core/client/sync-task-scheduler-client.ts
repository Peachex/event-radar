import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ExceptionResponse } from '../model/exception-response';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  private readonly syncTaskSchedulerPort: string = '8088';

  // Tasks APIs
  private readonly retrieveAllTasksApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks`;
  private readonly retrieveTasksByFieldsApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks/search`;

  // Scheduler APIs
  private readonly triggerTaskApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/scheduler/run`;

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

  triggerTask(taskId: number): Observable<{}> {
    const finalUrl: string = this.triggerTaskApiUrl + '/' + taskId;
    console.log(finalUrl);
    return this.httpClient.post(finalUrl, {});
  }
}
