import { Component } from '@angular/core';
import { SchedulerHomeInfoComponent } from '../../feature/scheduler-home-info/scheduler-home-info.component';
import { TasksHomeInfoComponent } from '../../feature/tasks-home-info/tasks-home-info.component';

@Component({
  selector: 'app-home-page',
  imports: [SchedulerHomeInfoComponent, TasksHomeInfoComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
})
export class HomePageComponent {}
