import { Component, Input } from '@angular/core';
import { EventData } from '../../core/model/event-data';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-card',
  imports: [CommonModule],
  templateUrl: './event-card.component.html',
  styleUrl: './event-card.component.css',
})
export class EventCardComponent {
  @Input() event!: EventData;

  defaultImage =
    'https://cdn-cjhkj.nitrocdn.com/krXSsXVqwzhduXLVuGLToUwHLNnSxUxO/assets/images/optimized/rev-d98e8d7/spotme.com/wp-content/uploads/2020/07/Hero-1.jpg';

  onImageError(event: Event) {
    const element = event.target as HTMLImageElement;
    element.src = this.defaultImage;
  }
}
