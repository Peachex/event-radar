import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';

@Component({
  selector: 'app-tasks-page',
  imports: [CommonModule, FormsModule, InProgressProcessSpinnerComponent, ErrorMessageComponent, EmptyResultsComponent],
  templateUrl: './tasks-page.component.html',
  styleUrl: './tasks-page.component.css',
})
export class TasksPageComponent implements OnInit {
  @Input() tasksIds: string[] = [];
  successMessage: string | null = '';
  errorMessage: string | null = '';
  fetchForTableInitIsCompleted: boolean = false;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.findAllTasksIds().subscribe({
      next: (ids: string[]) => {
        console.log(ids);

        //todo Create model for event manager response (taskId list)

        this.tasksIds = ids;
        // this.tasksChange.emit(tasks);
        // this.errorMessage.emit(null);
        // this.fetchForTableInitIsCompleted.emit(true);
      },
      error: (error: TaskFetchingError) => {
        console.error('Error fetching tasks:', error);
        // this.errorMessage.emit(error.message);
        // this.fetchForTableInitIsCompleted.emit(true);
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

  private resetErrorMessage() {
    this.errorMessage = null;
  }

  private resetTasksInCaseOfErrorMessageExists(errorMessage: string | null) {
    if (errorMessage) {
      this.tasksIds = [];
    }
  }
}
