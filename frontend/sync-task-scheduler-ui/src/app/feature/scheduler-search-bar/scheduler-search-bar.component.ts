import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../../core/model/task';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';

@Component({
  selector: 'app-scheduler-search-bar',
  imports: [FormsModule],
  templateUrl: './scheduler-search-bar.component.html',
  styleUrl: './scheduler-search-bar.component.css',
})
export class SchedulerSearchBarComponent {
  @Input() searchQuery: string = '';
  @Output() fetchedTasks = new EventEmitter<Task[]>();
  @Output() errorMessage = new EventEmitter<string | null>();

  constructor(private taskService: TaskService) {}

  performSearch() {
    // Add exception handling
    console.log(`Here is the resuls for query=${this.searchQuery}`);
    this.fetchedTasks.emit(this.taskService.retrieveTasksSchedulesFromBackendAPI(this.searchQuery));
  }

  findAll() {
    this.searchQuery = '';
    this.taskService.retrieveAllTasks().subscribe({
      next: (tasks: Task[]) => {
        this.fetchedTasks.emit(tasks);
        this.errorMessage.emit(null);
      },
      error: (error: TaskFetchingError) => {
        console.error('Error fetching tasks:', error);
        this.errorMessage.emit(error.message);
      },
    });
  }
}
