import { Injectable } from '@angular/core';
import { Task } from '../model/task';
import { SyncTaskSchedulerClient } from '../client/sync-task-scheduler-client';
import { catchError, map, Observable } from 'rxjs';
import { SearchByFieldsRequest } from '../model/search-by-field-request';
import { ErrorUtil } from '../util/error-util';
import { EventManagerClient } from '../client/event-manager-client';
import { EventManagerTaskId } from '../model/event-manager-task-id';
import { PaginatedTasksResponse } from '../model/paginated-tasks-response';
import { SearchByFieldsPaginatedRequest } from '../model/search-by-field-paginated-request';
import { SortingDirection } from '../model/sorting-direction';
import { SortField } from '../model/sort-field';
import { PaginatedEventManagerTaskIdResponse } from '../model/paginated-event-manager-task-id-response';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  constructor(private taskClient: SyncTaskSchedulerClient, private eventManagerClient: EventManagerClient) {}

  findTasksBySearchQuery(searchQuery: string): Observable<Task[]> {
    const searchRequest: SearchByFieldsRequest = {} as SearchByFieldsRequest;

    Task.getTaskFields().forEach((field) => {
      searchRequest[field] = searchQuery;
    });

    console.log(searchRequest);

    return this.taskClient.retrieveTasksByFields(searchRequest, false);
  }

  findTasksBySearchQueryPaginated(searchQuery: string, page: number, size: number): Observable<PaginatedTasksResponse> {
    const searchRequest: SearchByFieldsRequest = {} as SearchByFieldsRequest;

    Task.getTaskFields().forEach((field) => {
      searchRequest[field] = searchQuery;
    });

    const searchByFieldsPaginatedRequest: SearchByFieldsPaginatedRequest = {
      isCombinedMatch: false,
      fields: searchRequest,
      pageRequestDTO: {
        page: page,
        size: size,
        sortFields: [
          {
            field: 'status',
            direction: SortingDirection.ASC,
          },
        ],
      },
    };

    return this.taskClient.retrieveTasksByFieldsPaginated(searchByFieldsPaginatedRequest);
  }

  findAllTasksIds(): Observable<EventManagerTaskId[]> {
    return this.eventManagerClient.retrieveAllTasksIds();
  }

  findAllTasksIdsPaginated(page: number, size: number): Observable<PaginatedEventManagerTaskIdResponse> {
    const enableSortingByStatus: SortField[] = [
      {
        field: 'taskId',
        direction: SortingDirection.ASC,
      },
    ];
    return this.eventManagerClient.retrieveAllTasksIdsPaginated(page, size, enableSortingByStatus);
  }

  findAllTasks(): Observable<Task[]> {
    return this.taskClient.retrieveAllTasks();
  }

  findAllTasksPaginated(page: number, size: number): Observable<PaginatedTasksResponse> {
    const enableSortingByStatus: SortField[] = [
      {
        field: 'status',
        direction: SortingDirection.ASC,
      },
    ];
    return this.taskClient.retrieveAllTasksPaginated(page, size, enableSortingByStatus);
  }

  deleteTask(task: Task): Observable<string> {
    return this.taskClient.deleteTask(task.id).pipe(
      map(() => 'Task was deleted successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }

  createTask(task: Task): Observable<string> {
    return this.taskClient.createTask(task).pipe(
      map(() => 'Task was created successfully!'),
      catchError(ErrorUtil.handleError)
    );
  }
}
