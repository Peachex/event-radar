import { Component, EventEmitter, Output } from '@angular/core';
import { SortingDirection } from '../../core/model/sorting-direction';
import { SortField } from '../../core/model/sort-field';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-event-sort-bar',
  imports: [CommonModule, FormsModule],
  templateUrl: './event-sort-bar.component.html',
  styleUrl: './event-sort-bar.component.css',
})
export class EventSortBarComponent {
  //fixme: There is an issue with pagination/sorting on the backend. As a result, sorting does not work correctly
  // with the search by fields feature.

  @Output() sortBarDataChange = new EventEmitter<SortField[]>();

  sortField: string = 'title';
  sortDirection: SortingDirection = SortingDirection.ASC;

  //todo: Replace hardcoded values with dynamically loaded ones.
  sortOptions = [
    { label: 'Title', value: 'title' },
    { label: 'Category', value: 'category' },
    { label: 'Price', value: 'price' },
    { label: 'Date', value: 'date' },
  ];

  directionOptions = [
    { label: 'Ascending', value: SortingDirection.ASC },
    { label: 'Descending', value: SortingDirection.DESC },
  ];

  applySort() {
    const sortData = this.createSortBarData();
    this.sortBarDataChange.emit(sortData);
  }

  private createSortBarData(): SortField[] {
    switch (this.sortField) {
      case 'price':
        return this.createPriceSortFields();
      case 'date':
        return this.createDateSortFields();
      default:
        return [{ field: this.sortField, direction: this.sortDirection }];
    }
  }

  private createPriceSortFields(): SortField[] {
    if (this.sortDirection === SortingDirection.ASC) {
      return [
        { field: 'price.minPrice', direction: this.sortDirection },
        { field: 'price.maxPrice', direction: this.sortDirection },
      ];
    } else {
      return [
        { field: 'price.maxPrice', direction: this.sortDirection },
        { field: 'price.minPrice', direction: this.sortDirection },
      ];
    }
  }

  private createDateSortFields(): SortField[] {
    if (this.sortDirection === SortingDirection.ASC) {
      return [
        { field: 'date.startDate', direction: this.sortDirection },
        { field: 'date.endDate', direction: this.sortDirection },
      ];
    } else {
      return [
        { field: 'date.endDate', direction: this.sortDirection },
        { field: 'date.startDate', direction: this.sortDirection },
      ];
    }
  }
}
