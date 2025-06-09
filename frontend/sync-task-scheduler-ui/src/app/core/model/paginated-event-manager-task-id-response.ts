import { EventManagerTaskId } from './event-manager-task-id';

export interface PaginatedEventManagerTaskIdResponse {
  page: {
    content: EventManagerTaskId[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
  };
}
