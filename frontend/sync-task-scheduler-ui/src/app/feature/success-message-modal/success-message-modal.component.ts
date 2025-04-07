import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-success-message-modal',
  imports: [],
  templateUrl: './success-message-modal.component.html',
  styleUrl: './success-message-modal.component.css',
})
export class ModalComponent {
  @Input() modalId: string = '';
  @Input() modalTitle: string | null = '';
  @Input() modalBody: string | null = '';
}
