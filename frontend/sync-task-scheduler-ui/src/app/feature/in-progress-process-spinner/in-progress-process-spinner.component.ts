import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-in-progress-process-spinner',
  imports: [CommonModule],
  templateUrl: './in-progress-process-spinner.component.html',
  styleUrl: './in-progress-process-spinner.component.css',
})
export class InProgressProcessSpinnerComponent {
  @Input() actionIsCompleted: boolean = false;
  @Input() errorMessage: string | null = '';

  processIsInProgress(): boolean {
    return this.actionIsNotCompleted() && this.errorMessageIsNull();
  }

  actionIsNotCompleted() {
    return !this.actionIsCompleted;
  }

  errorMessageIsNull() {
    return !this.errorMessage;
  }
}
