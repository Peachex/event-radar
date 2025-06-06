import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Task } from '../../core/model/task';
import { TaskStatus } from '../../core/model/task-status';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';
import { SchedulerService } from '../../core/service/scheduler-service';
import { ScheduleActionResultModalComponent } from '../schedule-action-result-modal/schedule-action-result-modal.component';
import { TaskInfoModalComponent } from '../task-info-modal/task-info-modal.component';
import { TaskSchedulerError } from '../../core/error/task-scheduler-error';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '../pagination/pagination.component';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule, ScheduleActionResultModalComponent, TaskInfoModalComponent, FormsModule, PaginationComponent],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent implements OnInit {
  @Input() tasks: Task[] = [];
  @Input() searchQuery: string = '';
  @Output() tasksChange = new EventEmitter<Task[]>();
  @Output() successMessage = new EventEmitter<string | null>();
  @Output() errorMessage = new EventEmitter<string | null>();
  @Output() modalErrorMessage = new EventEmitter<string | null>();
  @Output() fetchForTableInitIsCompleted = new EventEmitter<boolean>();

  taskScheduleActionIsCompleted: boolean = true;
  selectedTask: Task | null = null;

  @Input() sharedPageSize: number = 5;
  @Input() sharedCurrentPageNumber: number = 0;
  @Input() sharedTotalPages: number = 0;
  @Output() pageSizeChange = new EventEmitter<number>();
  @Output() currentPageNumberChange = new EventEmitter<number>();

  @Input() searchWasTriggered: boolean = false;

  constructor(private taskService: TaskService, private schedulerService: SchedulerService) {}

  ngOnInit(): void {
    this.loadTasksPage(this.sharedCurrentPageNumber, this.sharedPageSize);
  }

  loadTasksPage(page: number, size: number): void {
    this.taskService.findAllTasksPaginated(page, size).subscribe({
      next: (response) => {
        this.tasks = response.page.content;
        this.sharedTotalPages = response.page.totalPages;
        this.sharedCurrentPageNumber = response.page.number;
        this.tasksChange.emit(this.tasks);
        this.errorMessage.emit(null);
        this.fetchForTableInitIsCompleted.emit(true);
      },
      error: (error: TaskFetchingError) => {
        console.error('Error fetching tasks:', error);
        this.errorMessage.emit(error.message);
        this.fetchForTableInitIsCompleted.emit(true);
      },
    });
  }

  performSearch(page: number, size: number, searchQuery: string) {
    this.fetchForTableInitIsCompleted.emit(false);
    this.taskService.findTasksBySearchQueryPaginated(searchQuery, page, size).subscribe({
      next: (response) => {
        this.sharedTotalPages = response.page.totalPages;
        this.sharedCurrentPageNumber = response.page.number;
        this.tasks = response.page.content;
        this.errorMessage.emit(null);
        this.fetchForTableInitIsCompleted.emit(true);
      },
      error: (error: TaskFetchingError) => {
        this.fetchForTableInitIsCompleted.emit(true);
        console.error('Error fetching tasks:', error);
        this.errorMessage.emit(error.message);
      },
    });
  }

  invokeRetrievingEventsLogic(page: number, pageSize: number, searchQuery: string) {
    if (this.searchWasTriggered) {
      this.performSearch(page, pageSize, searchQuery);
    } else {
      this.loadTasksPage(page, pageSize);
    }
  }

  onPageSizeChange(newSize: number) {
    this.pageSizeChange.emit(newSize);
    this.sharedCurrentPageNumber = 0;
    this.invokeRetrievingEventsLogic(this.sharedCurrentPageNumber, newSize, this.searchQuery);
  }

  goToPage(page: number) {
    this.sharedCurrentPageNumber = page;
    this.currentPageNumberChange.emit(page);
    this.invokeRetrievingEventsLogic(this.sharedCurrentPageNumber, this.sharedPageSize, this.searchQuery);
  }

  getTasksSortedByStatus(): Task[] {
    return this.tasks.slice().sort((t1, t2) => {
      const priority = {
        [TaskStatus.ACTIVE]: 0,
        [TaskStatus.PAUSED]: 1,
      };
      return priority[t1.status] - priority[t2.status];
    });
  }

  viewTaskDetails(task: Task, event: Event) {
    event.preventDefault();
    this.selectedTask = task;
  }

  closeTaskDetails() {
    this.selectedTask = null;
  }

  runTask(task: Task) {
    this.clearErrorAndCompleteFlag();
    this.schedulerService.triggerTask(task).subscribe({
      next: (message: string) => {
        this.setSuccessMessageAndCompleteFlag(message);
      },
      error: (error: TaskSchedulerError) => {
        this.handleTaskError(error);
      },
    });
  }

  updateTaskStatus(task: Task) {
    this.clearErrorAndCompleteFlag();
    const statusUpdateMethod = this.selectStatusUpdateMethod(task.status);

    statusUpdateMethod(task).subscribe({
      next: (message: string) => {
        this.setSuccessMessageAndCompleteFlag(message);
        this.switchTaskStatus(task);
      },
      error: (error: TaskSchedulerError) => {
        this.handleTaskError(error);
      },
    });
  }

  deleteTask(task: Task) {
    this.clearErrorAndCompleteFlag();
    this.taskService.deleteTask(task).subscribe({
      next: (message: string) => {
        this.setSuccessMessageAndCompleteFlag(message);
        this.removeTaskFromList(task);
      },
      error: (error: TaskSchedulerError) => {
        this.handleTaskError(error);
      },
    });
  }

  private removeTaskFromList(task: Task) {
    const index = this.tasks.indexOf(task);
    if (index > -1) {
      this.tasks.splice(index, 1);
    }
  }

  private selectStatusUpdateMethod(currentTaskStatus: TaskStatus): Function {
    return currentTaskStatus === TaskStatus.ACTIVE
      ? (t: Task) => this.schedulerService.pauseTask(t)
      : (t: Task) => this.schedulerService.resumeTask(t);
  }

  private switchTaskStatus(task: Task): void {
    task.status = task.status === TaskStatus.ACTIVE ? TaskStatus.PAUSED : TaskStatus.ACTIVE;
  }

  private clearErrorAndCompleteFlag() {
    this.modalErrorMessage.emit(null);
    this.taskScheduleActionIsCompleted = false;
  }

  private setSuccessMessageAndCompleteFlag(message: string) {
    this.successMessage.emit(message);
    this.taskScheduleActionIsCompleted = true;
  }

  private handleTaskError(error: TaskFetchingError | TaskSchedulerError) {
    this.modalErrorMessage.emit(error.message);
    this.taskScheduleActionIsCompleted = true;
    console.error('Error:', error.message);
  }
}
