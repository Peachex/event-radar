import { Injectable } from '@angular/core';
import { EventWebAppClient } from '../client/event-web-app-client';
import { Observable } from 'rxjs';
import { EventData } from '../model/event-data';
import { PaginatedEventDataResponse } from '../model/paginated-event-data-response';
import { SortField } from '../model/sort-field';
import { SortingDirection } from '../model/sorting-direction';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  constructor(private eventClient: EventWebAppClient) {}

  retrieveEvents(): Observable<EventData[]> {
    return this.eventClient.retrieveAllEvents();
  }

  retrieveEventsPaginated(page: number, size: number): Observable<PaginatedEventDataResponse> {
    const enableSortingByCategory: SortField[] = [
      {
        field: 'category',
        direction: SortingDirection.ASC,
      },
    ];
    return this.eventClient.retrieveAllEventsPaginated(page, size, enableSortingByCategory);
  }
}
