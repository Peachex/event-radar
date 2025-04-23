import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExceptionResponse } from '../model/exception-response';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskFetchingError } from '../error/task-fetching-error';

@Injectable({
  providedIn: 'root',
})
export class EventManagerClient {
  private readonly eventManagerPort: string = '8090';

  // Tasks APIs
  private readonly retrieveAllTasksApiUrl: string = `http://localhost:${this.eventManagerPort}/EventManager/tasks`;

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasksIds(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TaskFetchingError('Failed to fetch tasks. Please try again later.', error.error, error)
        );
      })
    );
  }
}
