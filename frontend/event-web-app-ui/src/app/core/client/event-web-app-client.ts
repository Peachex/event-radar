import { Injectable } from '@angular/core';
import { ConfigLoader } from '../util/config-loader';
import { catchError, Observable, throwError } from 'rxjs';
import { EventData } from '../model/event-data';
import { HttpClient } from '@angular/common/http';
import { ExceptionResponse } from '../model/exception-response';
import { EventsFetchingError } from '../error/events-fetching-error';
import { SortField } from '../model/sort-field';
import { PaginatedEventDataResponse } from '../model/paginated-event-data-response';
import { SearchByFieldsPaginatedRequest } from '../model/search-by-field-paginated-request';

@Injectable({
  providedIn: 'root',
})
export class EventWebAppClient {
  config = ConfigLoader.get();

  private readonly eventWebAppHost: string = this.config.eventWebAppHost;
  private readonly eventWebAppPort: string = this.config.eventWebAppPort;
  private readonly eventWebAppContextPath: string = this.config.eventWebAppContextPath;

  // Events APIs
  private readonly retrieveAllEventsApiUrl: string = `${this.eventWebAppHost}:${this.eventWebAppPort}/${this.eventWebAppContextPath}/events`;
  private readonly retrieveAllEventsPaginatedApiUrl: string = `${this.eventWebAppHost}:${this.eventWebAppPort}/${this.eventWebAppContextPath}/events/all`;
  private readonly retrieveEventsByFieldsPaginatedApiUrl: string = `${this.eventWebAppHost}:${this.eventWebAppPort}/${this.eventWebAppContextPath}/events/search/pagination`;

  constructor(private httpClient: HttpClient) {}

  retrieveAllEvents(): Observable<EventData[]> {
    return this.httpClient.get<EventData[]>(this.retrieveAllEventsApiUrl).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new EventsFetchingError('Failed to fetch events. Please try again later.', error.error, error)
        );
      })
    );
  }

  retrieveAllEventsPaginated(
    page: number,
    size: number,
    sortFields?: SortField[]
  ): Observable<PaginatedEventDataResponse> {
    const body = {
      page,
      size,
      sortFields,
    };

    return this.httpClient.post<PaginatedEventDataResponse>(this.retrieveAllEventsPaginatedApiUrl, body).pipe(
      catchError((error) => {
        console.error(error.error as ExceptionResponse);
        return throwError(
          () => new EventsFetchingError('Failed to fetch events. Please try again later.', error.error, error)
        );
      })
    );
  }

  retrieveEventsByFieldsPaginated(
    searchByFieldsPaginatedRequest: SearchByFieldsPaginatedRequest
  ): Observable<PaginatedEventDataResponse> {
    return this.httpClient
      .post<PaginatedEventDataResponse>(this.retrieveEventsByFieldsPaginatedApiUrl, searchByFieldsPaginatedRequest, {})
      .pipe(
        catchError((error) => {
          console.error(error.error as ExceptionResponse);
          return throwError(
            () => new EventsFetchingError('Failed to fetch events. Please try again later.', error.error, error)
          );
        })
      );
  }
}
