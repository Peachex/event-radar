import { Injectable } from '@angular/core';
import { EventWebAppClient } from '../client/event-web-app-client';
import { Observable } from 'rxjs';
import { EventData } from '../model/event-data';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  constructor(private eventClient: EventWebAppClient) {}

  retrieveEvents(): Observable<EventData[]> {
    return this.eventClient.retrieveAllEvents();
  }
}
