import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventData } from '../../core/model/event-data';

@Component({
  selector: 'app-empty-results',
  imports: [CommonModule],
  templateUrl: './empty-results.component.html',
  styleUrl: './empty-results.component.css',
})
export class EmptyResultsComponent {
  @Input() events: EventData[] = [];
  @Input() eventsRetrievingIsCompleted: boolean = false;
  @Input() errorMessage: string | null = '';

  hasNoResults(): boolean {
    return this.eventsAreEmpty() && this.errorMessageIsNull() && this.eventsRetrievingIsCompleted;
  }

  private eventsAreEmpty(): boolean {
    return this.events.length === 0;
  }

  private errorMessageIsNull(): boolean {
    return !this.errorMessage;
  }
}
