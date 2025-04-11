import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { TaskSchedulerError } from '../error/task-scheduler-error';
import { ErrorUtil } from '../util/error-util';
import { ExceptionResponse } from '../model/exception-response';

@Injectable({
  providedIn: 'root',
})
export class SchedulerService {
  constructor(private taskClient: SyncTaskSchedulerClient) {}

  triggerTask(task: Task): Observable<string> {
    return this.taskClient.triggerTask(task.id).pipe(
      map(() => 'Task started successfully!'),
      catchError(this.handleError)
    );
  }

  pauseTask(task: Task): Observable<string> {
    return this.taskClient.pauseTask(task.id).pipe(
      map(() => 'Task paused successfully!'),
      catchError(this.handleError)
    );
  }

  resumeTask(task: Task): Observable<string> {
    return this.taskClient.resumeTask(task.id).pipe(
      map(() => 'Task resumed successfully!'),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else {
      errorMessage = ErrorUtil.createErrorMessageBasedOnErrorStatus(error.status);
    }

    let exceptionResponse = error.error as ExceptionResponse;
    console.error(exceptionResponse);

    return throwError(() => new TaskSchedulerError(errorMessage, error.error));
  }
}
