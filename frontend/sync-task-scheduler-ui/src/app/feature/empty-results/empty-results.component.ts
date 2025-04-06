import { Component, Input } from '@angular/core';
import { Task } from '../../core/model/task';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-results',
  imports: [CommonModule],
  templateUrl: './empty-results.component.html',
  styleUrl: './empty-results.component.css',
})
export class EmptyResultsComponent {
  @Input() tasks: Task[] = [];
  @Input() errorMessage: string | null = '';
  @Input() fetchForTableInitIsCompleted: boolean = false;

  hasNoResults(): boolean {
    return this.tasksAreEmpty() && this.errorMessageIsNull() && this.fetchForTableInitIsCompleted;
  }

  private tasksAreEmpty(): boolean {
    return this.tasks.length === 0;
  }

  private errorMessageIsNull(): boolean {
    return !this.errorMessage;
  }
}
