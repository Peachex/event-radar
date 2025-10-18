import { Component } from '@angular/core';
import { EventMapComponent } from '../../feature/event-map/event-map.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-map-page',
  imports: [CommonModule, EventMapComponent],
  templateUrl: './event-map-page.component.html',
  styleUrl: './event-map-page.component.css',
})
export class EventMapPageComponent {}
