import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-tasks-home-info',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './tasks-home-info.component.html',
  styleUrl: './tasks-home-info.component.css',
})
export class TasksHomeInfoComponent {}
