import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Task } from '../../core/model/task';
import { TaskStatus } from '../../core/model/task-status';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';
import { SchedulerService } from '../../core/service/scheduler-service';
import { ScheduleActionResultModalComponent } from '../schedule-action-result-modal/schedule-action-result-modal.component';
import { TaskInfoModalComponent } from '../task-info-modal/task-info-modal.component';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule, ScheduleActionResultModalComponent, TaskInfoModalComponent],
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

  constructor(private taskService: TaskService, private schedulerService: SchedulerService) {}

  ngOnInit(): void {
    this.taskService.findAllTasks().subscribe({
      next: (tasks: Task[]) => {
        this.tasks = tasks;
        this.tasksChange.emit(tasks);
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

  viewTaskDetails(task: Task, event: Event) {
    event.preventDefault();
    this.selectedTask = task;
  }

  closeTaskDetails() {
    this.selectedTask = null;
  }

  runTask(task: Task) {
    this.modalErrorMessage.emit(null);
    this.taskScheduleActionIsCompleted = false;
    this.schedulerService.triggerTask(task).subscribe({
      next: (message: string) => {
        this.successMessage.emit(message);
        this.taskScheduleActionIsCompleted = true;
      },
      error: (error: TaskFetchingError) => {
        this.modalErrorMessage.emit(error.message);
        this.taskScheduleActionIsCompleted = true;
        console.error('Error:', error.message);
      },
    });
  }

  updateTaskStatus(task: Task) {
    this.modalErrorMessage.emit(null);
    this.taskScheduleActionIsCompleted = false;

    const statusUpdateMethod = this.selectStatusUpdateMethod(task.status);

    statusUpdateMethod(task).subscribe({
      next: (message: string) => {
        this.successMessage.emit(message);
        this.taskScheduleActionIsCompleted = true;
        this.switchTaskStatus(task);
      },
      error: (error: TaskFetchingError) => {
        this.modalErrorMessage.emit(error.message);
        this.taskScheduleActionIsCompleted = true;
        console.error('Error:', error.message);
      },
    });
  }

  private switchTaskStatus(task: Task): void {
    task.status = task.status === TaskStatus.ACTIVE ? TaskStatus.PAUSED : TaskStatus.ACTIVE;
  }

  private selectStatusUpdateMethod(currentTaskStatus: TaskStatus): Function {
    return currentTaskStatus === TaskStatus.ACTIVE
      ? (t: Task) => this.schedulerService.pauseTask(t)
      : (t: Task) => this.schedulerService.resumeTask(t);
  }

  deleteTask(taskId: number) {
    //todo Call service method.
    this.tasks = this.tasks.filter((task) => task.id !== taskId);
  }
}
