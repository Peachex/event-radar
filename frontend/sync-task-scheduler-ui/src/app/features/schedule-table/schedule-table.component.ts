import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { TaskSchedule } from '../../core/model/task-schedule';
import { ScheduleStatus } from '../../core/model/schedule-status';
import { TaskService } from '../../core/service/task-service';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent implements OnInit {
  @Input() tasks: TaskSchedule[] = [];

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.tasks = this.taskService.retrieveAllTasksSchedulesFromBackendAPI();
  }

  selectedTask: TaskSchedule | null = null;

  openTaskDetails(task: TaskSchedule, event: Event) {
    event.preventDefault();
    this.selectedTask = task;
  }

  closeTaskDetails() {
    this.selectedTask = null;
  }

  runTask(task: TaskSchedule) {
    console.log(`Running task: ${task.name}`);
  }

  toggleTaskStatus(task: TaskSchedule) {
    task.status =
      task.status === ScheduleStatus.ACTIVE
        ? ScheduleStatus.PAUSED
        : ScheduleStatus.ACTIVE;
  }

  deleteTask(taskId: number) {
    this.tasks = this.tasks.filter((task) => task.id !== taskId);
  }
}
