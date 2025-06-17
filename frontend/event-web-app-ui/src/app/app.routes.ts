import { Routes } from '@angular/router';
import { EventCardsPageComponent } from './page/event-cards-page/event-cards-page.component';

export const routes: Routes = [
  //todo: Create HomePageComponent and route the user to this page by default.
  //{ path: '', component: HomePageComponent }
  { path: 'events', component: EventCardsPageComponent },
];
