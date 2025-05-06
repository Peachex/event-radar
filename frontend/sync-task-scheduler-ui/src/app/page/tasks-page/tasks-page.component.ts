import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { TaskService } from '../../core/service/task-service';
import { EventManagerTaskId } from '../../core/model/event-manager-task-id';
import { TasksIdsFetchingError } from '../../core/error/tasks-ids-fetching-error';

@Component({
  selector: 'app-tasks-page',
  imports: [CommonModule, FormsModule, InProgressProcessSpinnerComponent, ErrorMessageComponent, EmptyResultsComponent],
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css',
})
export class TasksPageComponent implements OnInit {
  @Input() tasksIdsResponse: EventManagerTaskId[] = [];
  successMessage: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.findAllTasksIds().subscribe({
      next: (ids: EventManagerTaskId[]) => {
        this.tasksIdsResponse = ids;
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

  private resetTasksInCaseOfErrorMessageExists(errorMessage: string | null) {
    if (errorMessage) {
      this.tasksIdsResponse = [];
    }
  }
}
