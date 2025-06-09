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
  @Output() searchIsCompleted = new EventEmitter<boolean>();
  @Output() errorMessage = new EventEmitter<string | null>();
  @Output() sharedSearchQueryChange = new EventEmitter<string>();

  @Input() sharedPageSize: number = 5;

  @Output() sharedPageNumberChange = new EventEmitter<number>();
  @Output() sharedTotalPages = new EventEmitter<number>();
  @Output() searchWasTriggered = new EventEmitter<boolean>();

  constructor(private taskService: TaskService) {}

  performSearch() {
    this.searchIsCompleted.emit(false);
    this.sharedSearchQueryChange.emit(this.searchQuery);
    this.searchWasTriggered.emit(true);

    this.taskService.findTasksBySearchQueryPaginated(this.searchQuery, 0, this.sharedPageSize).subscribe({
      next: (response) => {
        this.sharedTotalPages.emit(response.page.totalPages);
        this.sharedPageNumberChange.emit(0);
        this.fetchedTasks.emit(response.page.content);
        this.errorMessage.emit(null);
        this.searchIsCompleted.emit(true);
      },
      error: (error: TaskFetchingError) => {
        this.searchIsCompleted.emit(true);
        console.error('Error fetching tasks:', error);
        this.errorMessage.emit(error.message);
      },
    });
  }

  findAll() {
    this.searchIsCompleted.emit(false);
    this.searchWasTriggered.emit(false);
    this.searchQuery = '';
    this.sharedSearchQueryChange.emit(this.searchQuery);
    this.sharedPageNumberChange.emit(0);

    this.taskService.findAllTasksPaginated(0, this.sharedPageSize).subscribe({
      next: (response) => {
        this.sharedTotalPages.emit(response.page.totalPages);
        this.fetchedTasks.emit(response.page.content);
        this.errorMessage.emit(null);
        this.searchIsCompleted.emit(true);
      },
      error: (error: TaskFetchingError) => {
        console.error('Error fetching tasks:', error);
        this.searchIsCompleted.emit(true);
        this.errorMessage.emit(error.message);
      },
    });
  }
}
