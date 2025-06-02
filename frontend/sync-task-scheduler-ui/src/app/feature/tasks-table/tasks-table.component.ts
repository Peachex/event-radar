import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EventManagerTaskId } from '../../core/model/event-manager-task-id';
import { RouterModule } from '@angular/router';
import { ScheduleCreationModalComponent } from '../schedule-creation-modal/schedule-creation-modal.component';

@Component({
  selector: 'app-tasks-table',
  imports: [CommonModule, RouterModule, ScheduleCreationModalComponent],
  templateUrl: './tasks-table.component.html',
  styleUrl: './tasks-table.component.css',
})
export class TasksTableComponent {
  @Input() tasksIdsResponse: EventManagerTaskId[] = [];
  @Output() fetchForTableInitIsCompleted = new EventEmitter<boolean>();

  taskScheduleActionIsCompleted: boolean = true;
  taskIdToExecute: string = '';

  setTaskIdToExecute(id: string) {
    this.taskIdToExecute = id;
  }
}
