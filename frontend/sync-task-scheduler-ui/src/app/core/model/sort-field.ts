import { SortingDirection } from './sorting-direction';
import { Task } from './task';

export interface SortField {
  field: keyof Task;
  direction: SortingDirection;
}
