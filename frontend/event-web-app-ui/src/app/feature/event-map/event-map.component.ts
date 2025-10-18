import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import maplibregl, { Map, Marker, LngLatLike } from 'maplibre-gl';
import { EventService } from '../../core/service/event-service';
import { EventData } from '../../core/model/event-data';
import { EventsFetchingError } from '../../core/error/events-fetching-error';

@Component({
  selector: 'app-event-map',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './event-map.component.html',
  styleUrls: ['./event-map.component.css'],
})
export class EventMapComponent implements OnInit, AfterViewInit, OnDestroy {
  private map!: Map;
  events: EventData[] = [];

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  ngOnDestroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  private loadEvents(): void {
    this.eventService.retrieveEvents().subscribe({
      next: (response) => {
        this.events = response;
        this.addEventMarkers(); // Add markers after data loads
      },
      error: (error: EventsFetchingError) => {
        console.error('Error fetching events:', error);
      },
    });
  }

  private initMap(): void {
    this.map = new maplibregl.Map({
      container: 'map',
      style: '/assets/map-styles/osm-bright-gl-style/style.json',
      center: [27.567374, 53.893791],
      zoom: 16,
    });

    this.map.addControl(new maplibregl.NavigationControl(), 'top-right');

    // Custom "locate me" control
    const locateFn = () => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const coords: LngLatLike = [position.coords.longitude, position.coords.latitude];
          this.addUserMarker(coords);
          this.map.flyTo({ center: coords, zoom: 16 });
        },
        (err) => {
          console.warn('Geolocation error:', err);
        }
      );
    };

    this.map.addControl(new LocateControl(locateFn), 'top-right');

    // Automatically locate on load
    locateFn();
  }

  private addUserMarker(coords: LngLatLike) {
    const el = document.createElement('div');
    el.className = 'user-marker';
    el.title = 'Вы находитесь здесь';

    new maplibregl.Marker(el)
      .setLngLat(coords)
      .setPopup(new maplibregl.Popup({ offset: 25 }).setHTML(`<strong>${el.title}</strong>`))
      .addTo(this.map);
  }

  private addEventMarkers(): void {
    if (!this.map) return;

    this.events.forEach((event) => {
      if (event.location.latitude !== 0 && event.location.longitude !== 0) {
        const el = document.createElement('div');
        el.className = 'event-marker';
        el.innerHTML = `
          <div class="event-icon"></div>
          <div class="event-label">${event.title}</div>
        `;

        new maplibregl.Marker(el)
          .setLngLat([event.location.longitude, event.location.latitude])
          .setPopup(
            new maplibregl.Popup({ offset: 25 }).setHTML(
              `<strong>${event.title}</strong><br>${event.location.rawAddress}`
            )
          )
          .addTo(this.map);
      }
    });
  }
}

class LocateControl {
  private container!: HTMLElement;
  private map!: maplibregl.Map;

  constructor(private locateFn: () => void) {}

  onAdd(map: maplibregl.Map) {
    this.map = map;
    this.container = document.createElement('div');
    this.container.className = 'maplibregl-ctrl maplibregl-ctrl-group';

    const button = document.createElement('button');
    button.className = 'locate-button';
    button.type = 'button';
    button.title = 'Go to my location';
    button.innerHTML = '📍'; // You can use an icon here instead

    button.onclick = () => this.locateFn();

    this.container.appendChild(button);
    return this.container;
  }

  onRemove() {
    this.container.parentNode?.removeChild(this.container);
  }
}
