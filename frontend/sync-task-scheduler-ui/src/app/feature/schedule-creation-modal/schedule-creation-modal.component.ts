import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../../core/model/task';
import { TaskStatus } from '../../core/model/task-status';
import { TaskValidationErrors } from '../../core/model/task-validation-errors.type';
import { isValidCron } from 'cron-validator';
import { TaskService } from '../../core/service/task-service';
import { TaskSchedulerError } from '../../core/error/task-scheduler-error';
import { ErrorMessageComponent } from '../error-message/error-message.component';
import { SuccessMessageComponent } from '../success-message/success-message.component';
import { InProgressProcessSpinnerComponent } from '../in-progress-process-spinner/in-progress-process-spinner.component';

@Component({
  selector: 'app-schedule-creation-modal',
  imports: [
    CommonModule,
    FormsModule,
    ErrorMessageComponent,
    SuccessMessageComponent,
    InProgressProcessSpinnerComponent,
  ],
  templateUrl: './schedule-creation-modal.component.html',
  styleUrl: './schedule-creation-modal.component.css',
})
export class ScheduleCreationModalComponent {
  @Input() modalId: string = 'createTaskScheduleModal';
  @Input() modalTitle: string = 'Create New Task Schedule';
  @Input() modalBody: string | null = '';
  @Input() taskIdToExecute: string = '';
  @Output() taskScheduleCreated = new EventEmitter<Task>();

  successMessage: string | null = '';
  errorMessage: string | null = '';
  taskScheduleActionIsCompleted: boolean = false;
  creationWasStarted: boolean = false;
  validationErrors: TaskValidationErrors = {};

  taskSchedule: Task = {
    id: -1,
    status: TaskStatus.ACTIVE,
    name: '',
    description: '',
    taskIdToExecute: '',
    cronExpression: '',
  };

  constructor(private taskService: TaskService) {}

  onSubmit() {
    this.taskSchedule.taskIdToExecute = this.taskIdToExecute;
    this.validationErrors = this.getValidationErrors();

    if (this.isValid()) {
      this.createTaskSchedule(this.taskSchedule);
    }
  }

  resetModal(): void {
    this.taskSchedule = {
      id: -1,
      status: TaskStatus.ACTIVE,
      name: '',
      description: '',
      taskIdToExecute: '',
      cronExpression: '',
    };
    this.validationErrors = {};
    this.errorMessage = null;
    this.successMessage = null;
    this.taskScheduleActionIsCompleted = false;
    this.creationWasStarted = false;
  }

  isValid(): boolean {
    return Object.keys(this.validationErrors).length === 0;
  }

  private getValidationErrors(): TaskValidationErrors {
    const errors: TaskValidationErrors = {};

    if (!this.taskSchedule.name || !this.taskSchedule.name.trim()) {
      errors.name = 'Task name is required.';
    }

    if (!this.taskIdToExecute || !this.taskIdToExecute.trim()) {
      errors.taskIdToExecute = 'Task ID is required.';
    }

    const cron = this.taskSchedule.cronExpression?.trim();
    if (!cron) {
      errors.cronExpression = 'Cron expression is required.';
    } else if (!this.isValidCronExpression(cron)) {
      errors.cronExpression = 'Invalid cron expression format.';
    }

    return errors;
  }

  private isValidCronExpression(expression: string): boolean {
    return isValidCron(expression.trim(), { seconds: true, allowBlankDay: true });
  }

  createTaskSchedule(taskSchedule: Task): void {
    this.creationWasStarted = true;
    this.clearErrorAndCompleteFlag();
    this.taskService.createTask(taskSchedule).subscribe({
      next: (message: string) => {
        this.setSuccessMessageAndCompleteFlag(message);
      },
      error: (error: TaskSchedulerError) => {
        this.handleTaskError(error);
      },
    });
  }

  private clearErrorAndCompleteFlag() {
    this.errorMessage = null;
    this.successMessage = null;
    this.taskScheduleActionIsCompleted = false;
  }

  private setSuccessMessageAndCompleteFlag(message: string) {
    this.successMessage = message;
    this.taskScheduleActionIsCompleted = true;
  }

  private handleTaskError(error: TaskSchedulerError) {
    this.errorMessage = error.message;
    this.taskScheduleActionIsCompleted = true;
    console.error('Error:', error.message);
  }
}
