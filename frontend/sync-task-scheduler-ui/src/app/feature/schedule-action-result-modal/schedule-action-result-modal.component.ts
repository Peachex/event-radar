import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ErrorMessageComponent } from '../error-message/error-message.component';
import { SuccessMessageComponent } from '../success-message/success-message.component';
import { InProgressProcessSpinnerComponent } from '../in-progress-process-spinner/in-progress-process-spinner.component';

@Component({
  selector: 'app-schedule-action-result-modal',
  imports: [CommonModule, InProgressProcessSpinnerComponent, ErrorMessageComponent, SuccessMessageComponent],
  templateUrl: './schedule-action-result-modal.component.html',
  styleUrl: './schedule-action-result-modal.component.css',
})
export class ScheduleActionResultModalComponent {
  @Input() modalId: string = '';
  @Input() modalTitle: string | null = '';
  @Input() modalBody: string | null = '';
  @Input() modalErrorMessage: string | null = '';
  @Input() taskScheduleActionIsCompleted: boolean = false;

  private errorMessageIsNull(): boolean {
    return !this.modalErrorMessage;
  }

  processIsCompletedWithoutErrors(): boolean {
    return this.taskScheduleActionIsCompleted && this.errorMessageIsNull();
  }

  processIsCompletedWithErrors(): boolean {
    return this.taskScheduleActionIsCompleted && this.modalErrorMessage !== null;
  }
}
