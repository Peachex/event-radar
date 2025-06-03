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

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule, ScheduleActionResultModalComponent, TaskInfoModalComponent, FormsModule],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent implements OnInit {
  @Input() tasks: Task[] = [];
  @Output() tasksChange = new EventEmitter<Task[]>();
  @Output() successMessage = new EventEmitter<string | null>();
  @Output() errorMessage = new EventEmitter<string | null>();
  @Output() modalErrorMessage = new EventEmitter<string | null>();
  @Output() fetchForTableInitIsCompleted = new EventEmitter<boolean>();

  taskScheduleActionIsCompleted: boolean = true;
  selectedTask: Task | null = null;

  currentPage = 0;
  pageSize = 5;
  pageSizes = [1, 5, 10, 20];
  totalPages = 0;

  constructor(private taskService: TaskService, private schedulerService: SchedulerService) {}

  ngOnInit(): void {
    this.loadTasksPage(this.currentPage, this.pageSize);
  }

  loadTasksPage(page: number, size: number): void {
    this.taskService.findAllTasksPaginated(page, size).subscribe({
      next: (response) => {
        this.tasks = response.page.content;
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
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

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadTasksPage(page, this.pageSize);
    }
  }

  onPageSizeChange(event: Event): void {
    this.currentPage = 0;
    this.loadTasksPage(this.currentPage, this.pageSize);
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
