import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-scheduler-search-bar',
  imports: [FormsModule],
  templateUrl: './scheduler-search-bar.component.html',
  styleUrl: './scheduler-search-bar.component.css',
})
export class SchedulerSearchBarComponent {
  searchQuery: string = '';
  @Output() searchQueryChange = new EventEmitter<string>();

  performSearch() {
    this.searchQueryChange.emit(this.searchQuery.trim());
  }
}
