import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TaskSchedule } from '../../core/model/task-schedule';
import { TaskService } from '../../core/service/task-service';

@Component({
  selector: 'app-scheduler-search-bar',
  imports: [FormsModule],
  templateUrl: './scheduler-search-bar.component.html',
  styleUrl: './scheduler-search-bar.component.css',
})
export class SchedulerSearchBarComponent {
  @Input() searchQuery: string = '';
  @Output() tasksSchedules = new EventEmitter<TaskSchedule[]>();

  constructor(private taskService: TaskService) {}

  performSearch() {
    // Add exception handling
    console.log(`Here is the resuls for query=${this.searchQuery}`);
    this.tasksSchedules.emit(
      this.taskService.retrieveTasksSchedulesFromBackendAPI(this.searchQuery)
    );
  }

  findAll() {
    // Add exception handling
    this.tasksSchedules.emit(
      this.taskService.retrieveAllTasksSchedulesFromBackendAPI()
    );
  }
}
