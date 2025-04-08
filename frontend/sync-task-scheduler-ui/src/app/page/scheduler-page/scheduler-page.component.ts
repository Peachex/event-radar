import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../feature/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../feature/scheduler-search-bar/scheduler-search-bar.component';
import { Task } from '../../core/model/task';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { CommonModule } from '@angular/common';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';

@Component({
  selector: 'app-scheduler-page',
  imports: [
    CommonModule,
    FormsModule,
    ScheduleTableComponent,
    SchedulerSearchBarComponent,
    InProgressProcessSpinnerComponent,
    ErrorMessageComponent,
    EmptyResultsComponent,
  ],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  @Input() searchIsCompleted: boolean = true;
  searchQuery: string = '';
  tasks: Task[] = [];
  successMessage: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

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
