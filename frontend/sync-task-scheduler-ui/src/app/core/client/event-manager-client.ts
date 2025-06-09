import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExceptionResponse } from '../model/exception-response';
import { catchError, Observable, throwError } from 'rxjs';
import { TasksIdsFetchingError } from '../error/tasks-ids-fetching-error';
import { EventManagerTaskId } from '../model/event-manager-task-id';
import { PaginatedEventManagerTaskIdResponse } from '../model/paginated-event-manager-task-id-response';
import { SortField } from '../model/sort-field';

@Injectable({
  providedIn: 'root',
})
export class EventManagerClient {
  private readonly eventManagerPort: string = '8090';

  // Tasks APIs
  private readonly retrieveAllTasksApiUrl: string = `http://localhost:${this.eventManagerPort}/EventManager/tasks`;

  constructor(private httpClient: HttpClient) {}

  retrieveAllTasksIds(): Observable<EventManagerTaskId[]> {
    return this.httpClient.get<EventManagerTaskId[]>(this.retrieveAllTasksApiUrl).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TasksIdsFetchingError('Failed to fetch tasks ids. Please try again later.', error.error, error)
        );
      })
    );
  }

  retrieveAllTasksIdsPaginated(
    page: number,
    size: number,
    sortFields?: SortField[]
  ): Observable<PaginatedEventManagerTaskIdResponse> {
    const body = {
      page,
      size,
      sortFields,
    };

    return this.httpClient.post<PaginatedEventManagerTaskIdResponse>(this.retrieveAllTasksApiUrl, body).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new TasksIdsFetchingError('Failed to fetch tasks ids. Please try again later.', error.error, error)
        );
      })
    );
  }
}
