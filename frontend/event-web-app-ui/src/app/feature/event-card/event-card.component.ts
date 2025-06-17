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

  imageSrc: string = '';
  private loadTimeout?: any;

  ngOnChanges() {
    this.loadImage(this.event.imageLink ?? null);
  }

  loadImage(url: string | null) {
    this.imageSrc = url || this.defaultImage;

    // Clear any existing timeout
    if (this.loadTimeout) {
      clearTimeout(this.loadTimeout);
    }

    // Start 1 second timer to fallback to default image if not loaded
    this.loadTimeout = setTimeout(() => {
      if (this.imageSrc !== this.defaultImage) {
        this.imageSrc = this.defaultImage;
      }
    }, 1000);
  }

  onImageLoad() {
    // Image loaded successfully, clear timeout to prevent fallback
    if (this.loadTimeout) {
      clearTimeout(this.loadTimeout);
      this.loadTimeout = undefined;
    }
  }

  onImageError(event: Event) {
    // Immediately fallback on error
    const element = event.target as HTMLImageElement;
    element.src = this.defaultImage;

    if (this.loadTimeout) {
      clearTimeout(this.loadTimeout);
      this.loadTimeout = undefined;
    }
  }
}
