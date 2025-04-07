import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-success-message-modal',
  imports: [CommonModule],
  templateUrl: './success-message-modal.component.html',
  styleUrl: './success-message-modal.component.css',
})
export class SuccessMessageModalComponent {
  @Input() modalId: string = '';
  @Input() modalTitle: string | null = '';
  @Input() modalBody: string | null = '';
  @Input() errorMessage: string | null = '';
}
