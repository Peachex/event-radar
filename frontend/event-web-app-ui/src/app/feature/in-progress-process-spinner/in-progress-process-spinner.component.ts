import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-in-progress-process-spinner',
  imports: [CommonModule],
  templateUrl: './in-progress-process-spinner.component.html',
  styleUrl: './in-progress-process-spinner.component.css',
})
export class InProgressProcessSpinnerComponent {
  @Input() actionsAreCompleted: boolean[] = [];

  processesAreInProgress(): boolean {
    return this.actionsAreNotCompleted();
  }

  actionsAreNotCompleted() {
    return this.actionsAreCompleted.includes(false);
  }
}
