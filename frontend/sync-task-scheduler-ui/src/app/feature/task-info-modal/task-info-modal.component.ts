import { Component, Input } from '@angular/core';
import { Task } from '../../core/model/task';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-task-info-modal',
  imports: [CommonModule],
  templateUrl: './task-info-modal.component.html',
  styleUrl: './task-info-modal.component.css',
})
export class TaskInfoModalComponent {
  @Input() selectedTask: Task | null = null;
}
