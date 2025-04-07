import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Task } from '../../core/model/task';
import { TaskStatus } from '../../core/model/task-status';
import { TaskService } from '../../core/service/task-service';
import { TaskFetchingError } from '../../core/error/task-fetching-error';
import { SchedulerService } from '../../core/service/scheduler-service';
import { ModalComponent } from '../success-message-modal/success-message-modal.component';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule, ModalComponent],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent implements OnInit {
  @Input() tasks: Task[] = [];
  @Output() tasksChange = new EventEmitter<Task[]>();
  @Output() successMessage = new EventEmitter<string | null>();
  @Output() errorMessage = new EventEmitter<string | null>();
  @Output() fetchForTableInitIsCompleted = new EventEmitter<boolean>();

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
    // todo: Remove success message after a user click or press something.

    this.errorMessage.emit(null);
    this.successMessage.emit(null);

    this.schedulerService.triggerTask(task).subscribe({
      next: (message: string) => {
        this.successMessage.emit(message);
        console.log(message);
      },
      error: (error: TaskFetchingError) => {
        this.errorMessage.emit(error.message);
        console.error('Error:', error.message);
      },
    });
  }

  updateTaskStatus(task: Task) {
    //todo Call service method.
    task.status = task.status === TaskStatus.ACTIVE ? TaskStatus.PAUSED : TaskStatus.ACTIVE;
  }

  deleteTask(taskId: number) {
    //todo Call service method.
    this.tasks = this.tasks.filter((task) => task.id !== taskId);
  }
}
