import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { EventCardComponent } from '../../feature/event-card/event-card.component';
import { EventData } from '../../core/model/event-data';
import { EventsHolder } from './events-holder';

@Component({
  selector: 'app-event-cards-page',
  imports: [CommonModule, EventCardComponent],
  templateUrl: './event-cards-page.component.html',
  styleUrl: './event-cards-page.component.css',
})
export class EventCardsPageComponent {
  events: EventData[] = EventsHolder.getEvents();
}
