import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { EventCardComponent } from '../../feature/event-card/event-card.component';
import { EventData } from '../../core/model/event-data';
import { EventsHolder } from './events-holder';
import { EventService } from '../../core/service/event-service';

@Component({
  selector: 'app-event-cards-page',
  imports: [CommonModule, EventCardComponent],
  templateUrl: './event-cards-page.component.html',
  styleUrl: './event-cards-page.component.css',
})
export class EventCardsPageComponent implements OnInit {
  // events: EventData[] = EventsHolder.getEvents();
  events: EventData[] = [];

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.eventService.retrieveEvents().subscribe({
      next: (retrievedEvents: EventData[]) => {
        console.log(retrievedEvents);
        this.events = retrievedEvents;
      },
    });
  }
}
