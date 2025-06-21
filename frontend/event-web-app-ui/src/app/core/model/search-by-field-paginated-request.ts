import { SearchByFieldsRequest } from './search-by-field-request';
import { SortField } from './sort-field';

export interface SearchByFieldsPaginatedRequest {
  isCombinedMatch: boolean;
  fields: SearchByFieldsRequest;
  pageRequestDTO: {
    page: number;
    size: number;
    sortFields?: SortField[];
  };
}
