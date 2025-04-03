import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { TaskSchedule } from '../../core/model/task-schedule';
import { TaskStatus } from '../../core/model/task-status';
import { TaskService } from '../../core/service/task-service';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent implements OnInit {
  @Input() tasksSchedules: TaskSchedule[] = [];

  selectedTask: TaskSchedule | null = null;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.tasksSchedules =
      this.taskService.retrieveAllTasksSchedulesFromBackendAPI();
  }

  openTaskDetails(task: TaskSchedule, event: Event) {
    event.preventDefault();
    this.selectedTask = task;
  }

  closeTaskDetails() {
    this.selectedTask = null;
  }

  runTask(task: TaskSchedule) {
    //todo Call service method.
    console.log(`Running task: ${task.name}`);
  }

  toggleTaskStatus(task: TaskSchedule) {
    //todo Call service method.
    task.status =
      task.status === TaskStatus.ACTIVE ? TaskStatus.PAUSED : TaskStatus.ACTIVE;
  }

  deleteTask(taskId: number) {
    //todo Call service method.
    this.tasksSchedules = this.tasksSchedules.filter(
      (task) => task.id !== taskId
    );
  }
}
