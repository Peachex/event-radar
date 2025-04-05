import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../feature/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../feature/scheduler-search-bar/scheduler-search-bar.component';
import { Task } from '../../core/model/task';

@Component({
  selector: 'app-scheduler-page',
  imports: [FormsModule, ScheduleTableComponent, SchedulerSearchBarComponent],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  searchQuery: string = '';
  tasks: Task[] = [];
  errorMessage: string | null = '';

  fetchResultsFromSearchBarComponent(foundTasks: Task[]) {
    this.tasks = foundTasks;
  }

  onErrorMessageUpdate(errorMessage: string | null) {
    this.errorMessage = errorMessage;
  }
}
