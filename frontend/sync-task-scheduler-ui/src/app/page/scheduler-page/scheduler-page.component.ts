import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../feature/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../feature/scheduler-search-bar/scheduler-search-bar.component';
import { Task } from '../../core/model/task';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { CommonModule } from '@angular/common';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';
import { ActivatedRoute } from '@angular/router';
import { PaginationComponent } from '../../feature/pagination/pagination.component';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';

@Component({
  selector: 'app-scheduler-page',
  imports: [
    CommonModule,
    FormsModule,
    PaginationComponent,
    ScheduleTableComponent,
    SchedulerSearchBarComponent,
    InProgressProcessSpinnerComponent,
    ErrorMessageComponent,
    EmptyResultsComponent,
  ],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent implements OnInit, AfterViewInit {
  @ViewChild(SchedulerSearchBarComponent) searchBarComponent!: SchedulerSearchBarComponent;
  @Input() searchIsCompleted: boolean = true;

  searchQuery: string = '';
  tasks: Task[] = [];
  successMessage: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

  sharedPageSize: number = 1;
  sharedPageNumber: number = 0;
  sharedTotalPages: number = 0;

  searchWasTriggered: boolean = false;

  constructor(private route: ActivatedRoute, private taskService: TaskService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.searchQuery = params['searchQuery'] || null;
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (this.searchQuery && this.searchBarComponent) {
        this.searchBarComponent.searchQuery = this.searchQuery;
        this.searchBarComponent.performSearch();
      }
    });
  }

  onPageSizeChange(newSize: number) {
    this.sharedPageSize = newSize;
    this.sharedPageNumber = 0;
    this.invokeRetrievingEventsLogic(this.sharedPageNumber, newSize, this.searchQuery);
  }

  goToPage(page: number) {
    this.sharedPageNumber = page;
    this.invokeRetrievingEventsLogic(this.sharedPageNumber, this.sharedPageSize, this.searchQuery);
  }

  invokeRetrievingEventsLogic(page: number, pageSize: number, searchQuery: string) {
    if (this.searchWasTriggered) {
      this.performSearch(page, pageSize, searchQuery);
    } else {
      this.loadTasksPage(page, pageSize);
    }
  }

  loadTasksPage(page: number, size: number): void {
    this.taskService.findAllTasksPaginated(page, size).subscribe({
      next: (response) => {
        this.tasks = response.page.content;
        this.sharedTotalPages = response.page.totalPages;
        this.sharedPageNumber = response.page.number;
        this.errorMessage = null;
        this.fetchForTableInitIsCompleted = true;
      },
      error: (error: TaskFetchingError) => {
        console.error('Error fetching tasks:', error);
        this.errorMessage = error.message;
        this.fetchForTableInitIsCompleted = true;
      },
    });
  }

  performSearch(page: number, size: number, searchQuery: string) {
    this.fetchForTableInitIsCompleted = false;
    this.taskService.findTasksBySearchQueryPaginated(searchQuery, page, size).subscribe({
      next: (response) => {
        this.sharedTotalPages = response.page.totalPages;
        this.sharedPageNumber = response.page.number;
        this.tasks = response.page.content;
        this.errorMessage = null;
        this.fetchForTableInitIsCompleted = true;
      },
      error: (error: TaskFetchingError) => {
        this.fetchForTableInitIsCompleted = true;
        console.error('Error fetching tasks:', error);
        this.errorMessage = error.message;
      },
    });
  }

  fetchResultsFromSearchBarComponent(foundTasks: Task[]) {
    this.tasks = foundTasks;
    this.resetErrorMessage();
  }

  onSearchIsCompletedUpdate(searchIsCompleted: boolean) {
    this.searchIsCompleted = searchIsCompleted;
  }

  onSuccessMessageUpdate(successMessage: string | null) {
    this.successMessage = successMessage;
  }

  onErrorMessageUpdate(errorMessage: string | null) {
    this.errorMessage = errorMessage;
    this.resetTasksInCaseOfErrorMessageExists(errorMessage);
  }

  onFetchForTableInitIsCompletedUpdate(isCompleted: boolean) {
    this.fetchForTableInitIsCompleted = isCompleted;
  }

  private resetErrorMessage() {
    this.errorMessage = null;
  }

  private resetTasksInCaseOfErrorMessageExists(errorMessage: string | null) {
    if (errorMessage) {
      this.tasks = [];
    }
  }
}
