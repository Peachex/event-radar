import { Task } from './task';

export interface PaginatedTasksResponse {
  page: {
    content: Task[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
  };
}
