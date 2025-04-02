import { Routes } from '@angular/router';
import { HomePageComponent } from './pages/home-page/home-page.component';
import { SchedulerPageComponent } from './pages/scheduler-page/scheduler-page.component';

export const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'home', component: HomePageComponent },
  { path: 'scheduler', component: SchedulerPageComponent },
];
