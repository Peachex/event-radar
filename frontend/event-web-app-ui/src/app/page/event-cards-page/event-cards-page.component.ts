import { CommonModule } from '@angular/common';
import { Component, HostListener, OnInit } from '@angular/core';
import { EventCardComponent } from '../../feature/event-card/event-card.component';
import { EventData } from '../../core/model/event-data';
import { EventService } from '../../core/service/event-service';
import { PaginationComponent } from '../../feature/pagination/pagination.component';
import { EventsFetchingError } from '../../core/error/events-fetching-error';
import { ScrollToTopComponent } from '../../feature/scroll-to-top/scroll-to-top.component';

@Component({
  selector: 'app-event-cards-page',
  imports: [CommonModule, EventCardComponent, PaginationComponent, ScrollToTopComponent],
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
    if (this.searchWasTriggered) {
      //this.performSearch(page, pageSize, searchQuery);
      this.retrieveEvents(page, pageSize);
    } else {
      this.retrieveEvents(page, pageSize);
    }
  }

  retrieveEvents(page: number, size: number): void {
    this.eventService.retrieveEventsPaginated(page, size).subscribe({
      next: (response) => {
        this.events = response.page.content;
        this.totalPages = response.page.totalPages;
        this.pageNumber = response.page.number;
        //this.errorMessage = null;
        //this.fetchForTableInitIsCompleted = true;
      },
      error: (error: EventsFetchingError) => {
        console.error('Error fetching events:', error);
        //this.errorMessage = error.message;
        //this.fetchForTableInitIsCompleted = true;
      },
    });
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  showScrollButton = false;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.showScrollButton = window.pageYOffset > 300;
  }
}
