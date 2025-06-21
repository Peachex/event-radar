import { EventData } from './event-data';

export interface PaginatedEventDataResponse {
  page: {
    content: EventData[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
  };
}
