import { Routes } from '@angular/router';
import { EventCardsPageComponent } from './page/event-cards-page/event-cards-page.component';
import { HomePageComponent } from './page/home-page/home-page.component';
import { EventMapPageComponent } from './page/event-map-page/event-map-page.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'events', component: EventCardsPageComponent },
  { path: 'map', component: EventMapPageComponent },
];
