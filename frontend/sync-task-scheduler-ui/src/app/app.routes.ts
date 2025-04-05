import { Routes } from '@angular/router';
import { HomePageComponent } from './page/home-page/home-page.component';
import { SchedulerPageComponent } from './page/scheduler-page/scheduler-page.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'scheduler', component: SchedulerPageComponent },
];
