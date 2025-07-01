import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { EventCardComponent } from '../../feature/event-card/event-card.component';
import { EventData } from '../../core/model/event-data';
import { EventService } from '../../core/service/event-service';
import { PaginationComponent } from '../../feature/pagination/pagination.component';
import { EventsFetchingError } from '../../core/error/events-fetching-error';
import { ScrollToTopComponent } from '../../feature/scroll-to-top/scroll-to-top.component';
import { InProgressProcessSpinnerComponent } from '../../feature/in-progress-process-spinner/in-progress-process-spinner.component';
import { EmptyResultsComponent } from '../../feature/empty-results/empty-results.component';
import { ErrorMessageComponent } from '../../feature/error-message/error-message.component';
import { EventSearchBarComponent } from '../../feature/event-search-bar/event-search-bar.component';
import { SearchBarData } from '../../core/model/search-bar-data';
import { EventSortBarComponent } from '../../feature/event-sort-bar/event-sort-bar.component';
import { SortField } from '../../core/model/sort-field';
import { SortingDirection } from '../../core/model/sorting-direction';
import { EventFilterBarComponent } from '../../feature/event-filter-bar/event-filter-bar.component';
import { FilterField } from '../../core/model/filter-field';

@Component({
  selector: 'app-event-cards-page',
  imports: [
    CommonModule,
    EventCardComponent,
    PaginationComponent,
    EventSearchBarComponent,
    EventSortBarComponent,
    EventFilterBarComponent,
    ScrollToTopComponent,
    InProgressProcessSpinnerComponent,
    ErrorMessageComponent,
    EmptyResultsComponent,
  ],
  templateUrl: './event-cards-page.component.html',
  styleUrl: './event-cards-page.component.css',
})
export class EventCardsPageComponent implements OnInit {
  events: EventData[] = [];

  pageSize: number = 4;
  pageNumber: number = 0;
  totalPages: number = 0;

  searchQuery: string = '';
  searchWasTriggered: boolean = false;

  sortFields: SortField[] = [{ field: 'title', direction: SortingDirection.ASC }];
  filterFields: FilterField[] = [];

  eventsRetrievingIsCompleted: boolean = false;

  errorMessage: string | null = '';

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.retrieveEvents(this.pageNumber, this.pageSize);
  }

  onPageSizeChange(newSize: number) {
    this.pageSize = newSize;
    this.pageNumber = 0;
    this.invokeRetrievingEventsLogic(this.pageNumber, newSize, this.searchQuery);
  }

  goToPage(newNumber: number) {
    this.pageNumber = newNumber;
    this.invokeRetrievingEventsLogic(this.pageNumber, this.pageSize, this.searchQuery);
  }

  invokeRetrievingEventsLogic(page: number, size: number, searchQuery: string) {
    this.resetEventsAndFlags();
    if (this.searchWasTriggered) {
      this.findEventsBySearchQuery(searchQuery, page, size);
    } else {
      this.retrieveEvents(page, size);
    }
  }

  resetEventsAndFlags(): void {
    this.eventsRetrievingIsCompleted = false;
    this.events = [];
  }

  retrieveEvents(page: number, size: number): void {
    this.eventService.retrieveEventsPaginated(page, size, this.sortFields).subscribe({
      next: (response) => {
        this.events = response.page.content;
        this.totalPages = response.page.totalPages;
        this.pageNumber = response.page.number;
        this.errorMessage = null;
        this.eventsRetrievingIsCompleted = true;
      },
      error: (error: EventsFetchingError) => {
        console.error('Error fetching events:', error);
        this.errorMessage = error.message;
        this.eventsRetrievingIsCompleted = true;
      },
    });
  }

  findEventsBySearchQuery(searchQuery: string, page: number, size: number): void {
    this.eventService
      .findEventsBySearchQueryPaginated(searchQuery, page, size, this.sortFields, this.filterFields)
      .subscribe({
        next: (response) => {
          this.events = response.page.content;
          this.totalPages = response.page.totalPages;
          this.pageNumber = response.page.number;
          this.errorMessage = null;
          this.eventsRetrievingIsCompleted = true;
        },
        error: (error: EventsFetchingError) => {
          console.error('Error fetching events:', error);
          this.errorMessage = error.message;
          this.eventsRetrievingIsCompleted = true;
        },
      });
  }

  onSearchBarDataChange(searchBarData: SearchBarData) {
    this.pageNumber = 0;
    this.searchQuery = searchBarData.searchQuery;
    this.searchWasTriggered = searchBarData.searchWasTriggered;
    this.invokeRetrievingEventsLogic(this.pageNumber, this.pageSize, this.searchQuery);
  }

  onSortBarDataChange(sortFields: SortField[]) {
    this.sortFields = sortFields;
    this.invokeRetrievingEventsLogic(this.pageNumber, this.pageSize, this.searchQuery);
  }

  onFilterBarDataChange(filterFields: FilterField[]) {
    this.filterFields = filterFields;
    this.searchWasTriggered = Object.keys(filterFields).length > 0 || !!this.searchQuery;
    this.invokeRetrievingEventsLogic(this.pageNumber, this.pageSize, this.searchQuery);
  }
}
