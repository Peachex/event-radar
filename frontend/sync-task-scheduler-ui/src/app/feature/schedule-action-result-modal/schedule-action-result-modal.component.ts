import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ErrorMessageComponent } from '../error-message/error-message.component';
import { SuccessMessageComponent } from '../success-message/success-message.component';

@Component({
  selector: 'app-success-message-modal',
  imports: [CommonModule, ErrorMessageComponent, SuccessMessageComponent],
  templateUrl: './schedule-action-result-modal.component.html',
  styleUrl: './schedule-action-result-modal.component.css',
})
export class SuccessMessageModalComponent {
  @Input() modalId: string = '';
  @Input() modalTitle: string | null = '';
  @Input() modalBody: string | null = '';
  @Input() modalErrorMessage: string | null = '';
  @Input() taskScheduleActionIsCompleted: boolean = false;

  processIsInProgress(): boolean {
    return this.taskScheduleActionIsNotCompleted() && this.errorMessageIsNull();
  }

  private taskScheduleActionIsNotCompleted(): boolean {
    return !this.taskScheduleActionIsCompleted;
  }

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
