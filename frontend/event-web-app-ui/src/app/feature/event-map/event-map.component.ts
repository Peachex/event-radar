import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import maplibregl, { Map, Marker, IControl } from 'maplibre-gl';
import { EventService } from '../../core/service/event-service';
import { EventData } from '../../core/model/event-data';
import { FormsModule } from '@angular/forms';
import { EventsFetchingError } from '../../core/error/events-fetching-error';

@Component({
  selector: 'app-event-map',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './event-map.component.html',
  styleUrls: ['./event-map.component.css'],
})
export class EventMapComponent implements OnInit, AfterViewInit, OnDestroy {
  private map!: Map;
  private markers: Marker[] = [];

  events: EventData[] = [];
  filteredEvents: EventData[] = [];

  categories: string[] = [];
  selectedCategories: string[] = [];

  showFilterDialog = false;

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
        this.filteredEvents = [...response];

        this.categories = Array.from(
          new Set(
            this.events
              .filter((e) => e.location.latitude !== 0 && e.location.longitude !== 0)
              .map((e) => e.category?.trim())
              .filter((c) => c && c.length > 0)
          )
        );

        this.updateMarkers();
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

    // Add custom filter button
    this.map.addControl(new FilterControl(() => this.openFilterDialog()), 'top-right');

    this.map.on('load', () => {
      geolocate.trigger();
      this.updateMarkers(); // ensure markers load after map finished loading
    });
  }

  // -----------------------------
  //       FILTER LOGIC
  // -----------------------------
  openFilterDialog() {
    this.showFilterDialog = true;
  }

  closeFilterDialog() {
    this.showFilterDialog = false;
  }

  toggleCategory(category: string, checked: boolean) {
    if (checked) {
      if (!this.selectedCategories.includes(category)) {
        this.selectedCategories.push(category);
      }
    } else {
      this.selectedCategories = this.selectedCategories.filter((c) => c !== category);
    }
  }

  applyFilters() {
    if (this.selectedCategories.length === 0) {
      this.filteredEvents = [...this.events];
    } else {
      this.filteredEvents = this.events.filter((e) => this.selectedCategories.includes(e.category));
    }

    this.updateMarkers();
    this.showFilterDialog = false; // Close dialog ✔ FIXED
  }

  clearFilters() {
    this.selectedCategories = [];
    this.filteredEvents = [...this.events];
    this.updateMarkers();
  }

  // -----------------------------
  //       MARKERS UPDATE
  // -----------------------------
  private updateMarkers(): void {
    if (!this.map) return;

    // remove old markers
    this.markers.forEach((m) => m.remove());
    this.markers = [];

    // add filtered markers
    this.filteredEvents.forEach((event) => {
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
          <a href="${event.eventLink}" target="_blank" class="btn btn-sm btn-outline-primary mt-2">Подробнее</a>
        `;

        const marker = new maplibregl.Marker(el)
          .setLngLat([event.location.longitude, event.location.latitude])
          .setPopup(new maplibregl.Popup({ offset: 25 }).setHTML(popupHtml))
          .addTo(this.map);

        this.markers.push(marker);
      }
    });
  }
}

// --------------------------------------------------
// Custom MapLibre Filter Button
// --------------------------------------------------
class FilterControl implements IControl {
  private container: HTMLElement;

  constructor(private onClick: () => void) {
    this.container = document.createElement('div');
    this.container.className = 'maplibregl-ctrl maplibregl-ctrl-group';

    const button = document.createElement('button');
    button.type = 'button';
    button.title = 'Фильтр';
    button.innerHTML = '🔍';
    button.onclick = () => this.onClick();

    this.container.appendChild(button);
  }

  onAdd(): HTMLElement {
    return this.container;
  }

  onRemove(): void {
    this.container.remove();
  }
}
