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

  @Input() sharedPageSize: number = 5;
  @Input() sharedCurrentPage: number = 0;
  @Output() currentPageNumberChange = new EventEmitter<number>();

  totalPages = 0;

  constructor(private taskService: TaskService) {}

  performSearch() {
    // Enable pagination for search and take values from the table component.

    this.searchIsCompleted.emit(false);

    this.taskService.findTasksBySearchQuery(this.searchQuery).subscribe({
      next: (tasks: Task[]) => {
        this.fetchedTasks.emit(tasks);
        this.searchIsCompleted.emit(true);
        this.errorMessage.emit(null);
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
    this.searchQuery = '';
    this.currentPageNumberChange.emit(0);
    this.taskService.findAllTasksPaginated(0, this.sharedPageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.sharedCurrentPage = response.page.number;
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
