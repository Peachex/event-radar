import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ScheduleTableComponent } from '../../features/schedule-table/schedule-table.component';

@Component({
  selector: 'app-scheduler-page',
  imports: [FormsModule, ScheduleTableComponent],
  templateUrl: './scheduler-page.component.html',
  styleUrl: './scheduler-page.component.css',
})
export class SchedulerPageComponent {
  searchQuery: string = '';
  filteredQuery: string = ''; // Holds the actual query when the search button is clicked

  performSearch() {
    this.filteredQuery = this.searchQuery.trim(); // Update query only when the button is clicked
  }
}
