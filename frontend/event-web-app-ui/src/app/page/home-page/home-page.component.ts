import { Component } from '@angular/core';
import { EventRadarHomeInfoComponent } from '../../feature/event-radar-home-info/event-radar-home-info.component';

@Component({
  selector: 'app-home-page',
  imports: [EventRadarHomeInfoComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css',
})
export class HomePageComponent {}
