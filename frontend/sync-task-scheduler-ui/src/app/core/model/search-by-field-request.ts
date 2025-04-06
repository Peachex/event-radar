import { Task } from './task';

export type SearchByFieldsRequest = Partial<Record<keyof Task, string | number>>;
