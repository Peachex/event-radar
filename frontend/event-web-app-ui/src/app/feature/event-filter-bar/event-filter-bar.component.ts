import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FilterField } from '../../core/model/filter-field';

@Component({
  selector: 'app-event-filter-bar',
  imports: [CommonModule, FormsModule],
  templateUrl: './event-filter-bar.component.html',
  styleUrl: './event-filter-bar.component.css',
})
export class EventFilterBarComponent {
  //fixme: There is an issue with pagination/filtering on the backend. As a result, filtering does not work correctly
  // with the search by fields feature.

  @Output() filterBarDataChange = new EventEmitter<FilterField[]>();

  selectedCategory: string = '';
  selectedSourceType: string = '';

  //todo: Replace hardcoded values with dynamically loaded ones.
  categories = [
    { value: 'Активный отдых', label: 'Active Leisure' },
    { value: 'Кино', label: 'Cinema' },
    { value: 'Концерты', label: 'Concerts' },
    { value: 'Экскурсии', label: 'Excursions' },
    { value: 'Детская афиша', label: 'For Kids' },
    { value: 'К праздникам!', label: 'Holiday Events' },
    { value: 'Музеи и выставки', label: 'Museums & Exhibitions' },
    { value: 'Квесты', label: 'Quests' },
    { value: 'Рекомендуем', label: 'Recommended' },
    { value: 'Stand Up', label: 'Stand Up' },
    { value: 'Спектакли', label: 'Theatre' },
  ];

  sourceTypes = [
    { value: 'AFISHA_RELAX', label: 'Afisha Relax' },
    { value: 'BYCARD', label: 'Bycard' },
  ];

  applyFilters() {
    const fields: FilterField[] = [];
    if (this.selectedCategory) {
      fields.push({ name: 'category', value: this.selectedCategory });
    }
    if (this.selectedSourceType) {
      fields.push({ name: 'sourceType', value: this.selectedSourceType });
    }
    this.filterBarDataChange.emit(fields);
  }

  resetFilters() {
    this.selectedCategory = '';
    this.selectedSourceType = '';
    this.applyFilters();
  }
}
