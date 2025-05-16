import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InProgressProcessSpinnerComponent } from '../in-progress-process-spinner/in-progress-process-spinner.component';
import { ErrorMessageComponent } from '../error-message/error-message.component';
import { SuccessMessageComponent } from '../success-message/success-message.component';
import { Task } from '../../core/model/task';
import { TaskStatus } from '../../core/model/task-status';

@Component({
  selector: 'app-schedule-creation-modal',
  imports: [
    CommonModule,
    FormsModule,
    InProgressProcessSpinnerComponent,
    ErrorMessageComponent,
    SuccessMessageComponent,
  ],
  templateUrl: './schedule-creation-modal.component.html',
  styleUrl: './schedule-creation-modal.component.css',
})
export class ScheduleCreationModalComponent {
  @Input() modalId: string = 'createTaskScheduleModal';
  @Input() modalTitle: string = 'Create New Task Schedule';
  @Input() modalBody: string | null = '';
  @Input() modalErrorMessage: string | null = '';
  @Input() taskScheduleActionIsCompleted: boolean = false;
  @Input() taskIdToExecute: string = '';
  @Output() taskScheduleCreated = new EventEmitter<Task>();

  taskSchedule: Task = {
    id: -1,
    status: TaskStatus.ACTIVE,
    name: '',
    description: '',
    taskIdToExecute: '',
    cronExpression: '',
  };

  //Call service method and then close the modal.
  onSubmit() {
    this.taskSchedule.taskIdToExecute = this.taskIdToExecute;
    if (this.taskSchedule.name && this.taskSchedule.taskIdToExecute && this.taskSchedule.cronExpression) {
      // this.taskScheduleCreated.emit(this.taskSchedule);
    } else {
      alert('Please fill in all required fields.');
    }
  }
}
