import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../features/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../features/scheduler-search-bar/scheduler-search-bar.component';
import { TaskSchedule } from '../../core/model/task-schedule';

@Component({
  selector: 'app-scheduler-page',
  imports: [FormsModule, ScheduleTableComponent, SchedulerSearchBarComponent],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  searchQuery: string = ''; // Step 1: Initial query
  tasksSchedules: TaskSchedule[] = []; // Step 2: Store filtered results

  updateResults(results: TaskSchedule[]) {
    this.tasksSchedules = results; // Step 3: Store results from search bar
  }
}
