import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { TaskService } from '../../core/service/task-service';
import { EventManagerTaskId } from '../../core/model/event-manager-task-id';
import { TasksIdsFetchingError } from '../../core/error/tasks-ids-fetching-error';
import { TasksTableComponent } from '../../feature/tasks-table/tasks-table.component';
import { SuccessMessageComponent } from '../../feature/success-message/success-message.component';
import { PaginationComponent } from '../../feature/pagination/pagination.component';

@Component({
  selector: 'app-tasks-page',
  imports: [
    CommonModule,
    FormsModule,
    PaginationComponent,
    TasksTableComponent,
    InProgressProcessSpinnerComponent,
    SuccessMessageComponent,
    ErrorMessageComponent,
    EmptyResultsComponent,
  ],
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css',
})
export class TasksPageComponent implements OnInit {
  @Input() tasksIdsResponse: EventManagerTaskId[] = [];

  successMessageFromChild: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

  sharedPageSize: number = 1;
  sharedPageNumber: number = 0;
  sharedTotalPages: number = 0;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.invokeRetrievingTasksIdsLogic(this.sharedPageNumber, this.sharedPageSize);
  }

  invokeRetrievingTasksIdsLogic(page: number, pageSize: number) {
    this.taskService.findAllTasksIdsPaginated(page, pageSize).subscribe({
      next: (response) => {
        this.tasksIdsResponse = response.page.content;
        this.sharedTotalPages = response.page.totalPages;
        this.sharedPageNumber = response.page.number;
        this.errorMessage = null;
        this.fetchForTableInitIsCompleted = true;
      },
      error: (error: TasksIdsFetchingError) => {
        console.error('Error fetching tasks ids:', error);
        this.errorMessage = error.message;
        this.fetchForTableInitIsCompleted = true;
      },
    });
  }

  onPageSizeChange(newSize: number) {
    this.sharedPageSize = newSize;
    this.sharedPageNumber = 0;
    this.invokeRetrievingTasksIdsLogic(this.sharedPageNumber, newSize);
  }

  goToPage(page: number) {
    this.sharedPageNumber = page;
    this.invokeRetrievingTasksIdsLogic(this.sharedPageNumber, this.sharedPageSize);
  }

  onSuccessMessageFromChildUpdate(successMessage: string | null) {
    this.successMessageFromChild = successMessage;
  }

  onErrorMessageUpdate(errorMessage: string | null) {
    this.errorMessage = errorMessage;
    this.resetTasksInCaseOfErrorMessageExists(errorMessage);
  }

  onFetchForTableInitIsCompletedUpdate(isCompleted: boolean) {
    this.fetchForTableInitIsCompleted = isCompleted;
  }

  private resetTasksInCaseOfErrorMessageExists(errorMessage: string | null) {
    if (errorMessage) {
      this.tasksIdsResponse = [];
    }
  }
}
