import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css',
})
export class PaginationComponent {
  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Input() pageSize = 1;
  @Input() pageSizes = [1, 5, 10, 20];

  @Output() pageChange = new EventEmitter<number>();
  @Output() pageSizeChange = new EventEmitter<number>();

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.pageChange.emit(page);
    }
  }

  onPageSizeChange(event: Event) {
    const newSize = parseInt((event.target as HTMLSelectElement).value, 10);
    this.pageSizeChange.emit(newSize);
  }

  getVisiblePages(): (number | string)[] {
    const pages: (number | string)[] = [];
    const total = this.totalPages;
    const current = this.currentPage;
    const maxMiddlePages = 5;

    if (total <= maxMiddlePages + 2) {
      // Small number of pages — show all
      return Array.from({ length: total }, (_, i) => i);
    }

    const showLeft = Math.max(current - 1, 1);
    const showRight = Math.min(current + 1, total - 2);

    pages.push(0); // First page

    if (showLeft > 1) {
      if (showLeft > 2) {
        pages.push('...');
      } else {
        pages.push(1);
      }
    }

    for (let i = showLeft; i <= showRight; i++) {
      pages.push(i);
    }

    if (showRight < total - 2) {
      if (showRight < total - 3) {
        pages.push('...');
      } else {
        pages.push(total - 2);
      }
    }

    pages.push(total - 1); // Last page

    return pages;
  }

  isNumber(value: any): value is number {
    return typeof value === 'number';
  }
}
