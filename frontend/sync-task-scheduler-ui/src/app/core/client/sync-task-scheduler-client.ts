import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TaskSchedule } from '../model/task-schedule';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';

@Injectable({
  providedIn: 'root',
})
export class SyncTaskSchedulerClient {
  private readonly retrieveAllTasksApiUrl: string = 'http://localhost:8088/SyncTaskScheduler/tasks';

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasks(): Observable<TaskSchedule[]> {
    return this.httpClient.get<TaskSchedule[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        console.error('HTTP error occurred:', error);
        return throwError(() => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error));
      })
    );
  }
}
