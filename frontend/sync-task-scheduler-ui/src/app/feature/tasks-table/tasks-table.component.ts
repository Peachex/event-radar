import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EventManagerTaskId } from '../../core/model/event-manager-task-id';
import { ScheduleActionResultModalComponent } from '../schedule-action-result-modal/schedule-action-result-modal.component';

@Component({
  selector: 'app-tasks-table',
  imports: [CommonModule, ScheduleActionResultModalComponent],
  templateUrl: './tasks-table.component.html',
  styleUrl: './tasks-table.component.css',
})
export class TasksTableComponent {
  @Input() tasksIdsResponse: EventManagerTaskId[] = [];
  @Output() successMessage = new EventEmitter<string | null>();
  @Output() errorMessage = new EventEmitter<string | null>();
  @Output() modalErrorMessage = new EventEmitter<string | null>();
  @Output() fetchForTableInitIsCompleted = new EventEmitter<boolean>();

  taskScheduleActionIsCompleted: boolean = true;

  //todo: Create modal for task creation.
  //todo: Setup 'Related Schedules' to redirect user to the scheduler page and perform search by taskId field.
}
