import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable } from 'rxjs';
import { ErrorUtil } from '../util/error-util';

@Injectable({
  providedIn: 'root',
})
export class SchedulerService {
  constructor(private taskClient: SyncTaskSchedulerClient) {}

  triggerTask(task: Task): Observable<string> {
    return this.taskClient.triggerTask(task.id).pipe(
      map(() => 'Task was started successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }

  pauseTask(task: Task): Observable<string> {
    return this.taskClient.pauseTask(task.id).pipe(
      map(() => 'Task was paused successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }

  resumeTask(task: Task): Observable<string> {
    return this.taskClient.resumeTask(task.id).pipe(
      map(() => 'Task was resumed successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }
}
