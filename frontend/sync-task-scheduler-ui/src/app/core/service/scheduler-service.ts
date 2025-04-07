import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SchedulerService {
  constructor(private taskClient: SyncTaskSchedulerClient) {}

  triggerTask(task: Task): Observable<string> {
    //todo Refactor handle error.

    return this.taskClient.triggerTask(task.id).pipe(
      map(() => 'Task started successfully!'),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else {
      switch (error.status) {
        case 400:
          errorMessage = 'Invalid request. Please check your input.';
          break;
        case 404:
          errorMessage = 'Task not found!';
          break;
        case 500:
          errorMessage = 'Internal server error. Try again later.';
          break;
        default:
          errorMessage = `Unexpected error: ${error.status}`;
      }
    }
    return throwError(() => new TaskFetchingError(errorMessage));
  }
}
