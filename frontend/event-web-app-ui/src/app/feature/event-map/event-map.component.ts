import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import maplibregl, { Map } from 'maplibre-gl';
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
        this.addEventMarkers();
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
      zoom: 14,
    });

    this.map.addControl(new maplibregl.NavigationControl(), 'top-right');

    const geolocate = new maplibregl.GeolocateControl({
      positionOptions: {
        enableHighAccuracy: true,
      },
      trackUserLocation: true,
      showUserLocation: true,
    });

    this.map.addControl(geolocate, 'top-right');

    // Wait for the map to finish loading, then trigger geolocation
    this.map.on('load', () => {
      geolocate.trigger();
    });
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

        const locationStr = event.location.rawAddress
          ? `${event.location.rawAddress}${event.location.name ? ` (${event.location.name})` : ''}<br>`
          : '';

        const popupHtml = `
          <strong>${event.title}</strong><br>
          ${event.category ? `${event.category}<br>` : ''}
          ${event.dateStr ? `${event.dateStr}<br>` : ''}
          ${event.priceStr ? `${event.priceStr}<br>` : ''}
          ${locationStr}
        `;

        new maplibregl.Marker(el)
          .setLngLat([event.location.longitude, event.location.latitude])
          .setPopup(new maplibregl.Popup({ offset: 25 }).setHTML(popupHtml))
          .addTo(this.map);
      }
    });
  }
}
