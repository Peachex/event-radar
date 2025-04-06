import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  private readonly retrieveAllTasksApiUrl: string = 'http://localhost:8089/SyncTaskScheduler/tasks';

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasks(): Observable<Task[]> {
    return this.httpClient.get<Task[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        return throwError(() => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error));
      })
    );
  }
}
