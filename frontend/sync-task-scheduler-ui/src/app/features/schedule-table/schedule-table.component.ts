import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-schedule-table',
  imports: [CommonModule],
  templateUrl: './schedule-table.component.html',
  styleUrl: './schedule-table.component.css',
})
export class ScheduleTableComponent {
  tasks: Task[] = [
    {
      id: 1,
      status: 'ACTIVE',
      name: 'SyncTask#1',
      description: 'Task that is needed for events sync.',
      taskIdToExecute: 'fetch_new_events_task',
      cronExpression: '0/10 * * * * ?',
    },
    {
      id: 2,
      status: 'PAUSED',
      name: 'SyncTask#2',
      description: 'Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 3,
      status: 'PAUSED',
      name: 'SyncTask#3',
      description: 'Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 4,
      status: 'PAUSED',
      name: 'SyncTask#4',
      description:
        'Another task for syncing. Another task for syncing. Another task for syncing.Another task for syncing.',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
    {
      id: 5,
      status: 'PAUSED',
      name: 'SyncTask#5',
      description:
        'Some dwefiewjfiew hfuewibewuf hewui fewifgeuwohfioq fuewofh ehfuewogfuoegfo ewufgewo fhiewohfoewfew fewf ewf',
      taskIdToExecute: 'update_records_task',
      cronExpression: '0 0/30 * * * ?',
    },
  ];

  @Input() searchQuery: string = '';

  get filteredTasks() {
    const query = this.searchQuery.toLowerCase();

    return this.tasks.filter((task) =>
      Object.values(task).some((value) =>
        value.toString().toLowerCase().includes(query)
      )
    );
  }

  selectedTask: Task | null = null;

  openTaskDetails(task: Task, event: Event) {
    event.preventDefault();
    this.selectedTask = task;
  }

  closeTaskDetails() {
    this.selectedTask = null;
  }

  runTask(task: Task) {
    console.log(`Running task: ${task.name}`);
  }

  toggleTaskStatus(task: Task) {
    task.status = task.status === 'ACTIVE' ? 'PAUSED' : 'ACTIVE';
  }

  deleteTask(taskId: number) {
    this.tasks = this.tasks.filter((task) => task.id !== taskId);
  }
}

interface Task {
  id: number;
  status: string;
  name: string;
  description: string;
  taskIdToExecute: string;
  cronExpression: string;
}
