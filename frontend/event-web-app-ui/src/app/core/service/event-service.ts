import { Injectable } from '@angular/core';
import { EventWebAppClient } from '../client/event-web-app-client';
import { Observable } from 'rxjs';
import { EventData } from '../model/event-data';
import { PaginatedEventDataResponse } from '../model/paginated-event-data-response';
import { SortField } from '../model/sort-field';
import { SortingDirection } from '../model/sorting-direction';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { EventDataUtil } from '../util/event-data-util';
import { SearchByFieldsPaginatedRequest } from '../model/search-by-field-paginated-request';
import { FilterField } from '../model/filter-field';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  constructor(private eventClient: EventWebAppClient) {}

  retrieveEvents(): Observable<EventData[]> {
    return this.eventClient.retrieveAllEvents();
  }

  retrieveEventsPaginated(
    page: number,
    size: number,
    sortFields?: SortField[]
  ): Observable<PaginatedEventDataResponse> {
    const resolvedSortFields: SortField[] = this.resolveSortFields(sortFields);
    return this.eventClient.retrieveAllEventsPaginated(page, size, resolvedSortFields);
  }

  findEventsBySearchQueryPaginated(
    searchQuery: string,
    page: number,
    size: number,
    sortFields: SortField[],
    filterFields: FilterField[]
  ): Observable<PaginatedEventDataResponse> {
    const searchRequest: SearchByFieldsRequest = {} as SearchByFieldsRequest;
    var isCombinedMatch = true;

    if (searchQuery) {
      EventDataUtil.getEventDataSearchableFields().forEach((field) => {
        searchRequest[field] = searchQuery;
      });
      isCombinedMatch = false;
    }

    filterFields.forEach((filter) => {
      searchRequest[filter.name] = filter.value;
    });

    const searchByFieldsPaginatedRequest: SearchByFieldsPaginatedRequest = {
      isCombinedMatch: isCombinedMatch,
      fields: searchRequest,
      pageRequestDTO: {
        page: page,
        size: size,
        sortFields: this.resolveSortFields(sortFields),
      },
    };

    return this.eventClient.retrieveEventsByFieldsPaginated(searchByFieldsPaginatedRequest);
  }

  private resolveSortFields(sortFields?: SortField[]): SortField[] {
    if (sortFields && sortFields.length > 0) {
      return sortFields;
    }

    return [
      {
        field: 'title',
        direction: SortingDirection.ASC,
      },
    ];
  }
}
