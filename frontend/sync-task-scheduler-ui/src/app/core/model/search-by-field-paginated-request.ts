import { SearchByFieldsRequest } from './search-by-field-request';

export interface SearchByFieldsPaginatedRequest {
  isCombinedMatch: boolean;
  fields: SearchByFieldsRequest;
  pageRequestDTO: {
    page: number;
    size: number;
  };
}
