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

@Component({
  selector: 'app-event-cards-page',
  imports: [
    CommonModule,
    EventCardComponent,
    PaginationComponent,
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

  invokeRetrievingEventsLogic(page: number, pageSize: number, searchQuery: string) {
    this.resetEventsAndFlags();
    if (this.searchWasTriggered) {
      //this.performSearch(page, pageSize, searchQuery);
      this.retrieveEvents(page, pageSize);
    } else {
      this.retrieveEvents(page, pageSize);
    }
  }

  resetEventsAndFlags(): void {
    this.eventsRetrievingIsCompleted = false;
    this.events = [];
  }

  retrieveEvents(page: number, size: number): void {
    this.eventService.retrieveEventsPaginated(page, size).subscribe({
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
}
