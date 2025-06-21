import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SearchBarData } from '../../core/model/search-bar-data';

@Component({
  selector: 'app-event-search-bar',
  imports: [FormsModule],
  templateUrl: './event-search-bar.component.html',
  styleUrl: './event-search-bar.component.css',
})
export class EventSearchBarComponent {
  @Output() searchBarDataChange = new EventEmitter<SearchBarData>();

  searchQuery: string = '';

  invokeSearch() {
    this.searchBarDataChange.emit(this.createSearchBarData(this.searchQuery, true));
  }

  invokeRetrievingAllEvents() {
    this.searchQuery = '';
    this.searchBarDataChange.emit(this.createSearchBarData(this.searchQuery, false));
  }

  createSearchBarData(searchQuery: string, searchWasTriggered: boolean): SearchBarData {
    const searchBarData: SearchBarData = {
      searchQuery: searchQuery,
      searchWasTriggered: searchWasTriggered,
    };
    return searchBarData;
  }
}
