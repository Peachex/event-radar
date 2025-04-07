import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../feature/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../feature/scheduler-search-bar/scheduler-search-bar.component';
import { Task } from '../../core/model/task';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { SuccessMessageComponent } from '../../feature/success-message/success-message.component';

@Component({
  selector: 'app-scheduler-page',
  imports: [
    FormsModule,
    ScheduleTableComponent,
    SchedulerSearchBarComponent,
    SuccessMessageComponent,
    ErrorMessageComponent,
    EmptyResultsComponent,
  ],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  searchQuery: string = '';
  tasks: Task[] = [];
  successMessage: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

  fetchResultsFromSearchBarComponent(foundTasks: Task[]) {
    this.tasks = foundTasks;
    this.resetErrorMessage();
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
