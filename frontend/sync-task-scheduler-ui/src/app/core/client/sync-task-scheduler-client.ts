import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';
import { SearchByFieldsRequest } from '../model/search-by-field-request';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  private readonly syncTaskSchedulerPort: string = '8089';
  private readonly retrieveAllTasksApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks`;
  private readonly retrieveTasksByFieldsApiUrl: string = `http://localhost:${this.syncTaskSchedulerPort}/SyncTaskScheduler/tasks/search`;

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasks(): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        return throwError(() => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error));
      })
    );
  }

  retrieveTasksByFields(searchByFieldsRequest: SearchByFieldsRequest, isCombinedMatch: boolean): Observable<Task[]> {
    const params = new HttpParams().set('isCombinedMatch', isCombinedMatch);
    return this.httpClient.post<Task[]>(this.retrieveTasksByFieldsApiUrl, searchByFieldsRequest, { params }).pipe(
      catchError((error) => {
        return throwError(() => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error));
      })
    );
  }
}
