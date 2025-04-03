import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../features/schedule-table/schedule-table.component';
import { SchedulerSearchBarComponent } from '../../features/scheduler-search-bar/scheduler-search-bar.component';

@Component({
  selector: 'app-scheduler-page',
  imports: [FormsModule, ScheduleTableComponent, SchedulerSearchBarComponent],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  filteredQuery: string = ''; // Define the property

  updateSearchQuery(query: string) {
    this.filteredQuery = query;
  }
}
