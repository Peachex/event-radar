export interface ExceptionResponse {
  timestamp: number[];
  status: number;
  exceptionMessage: string;
  exceptionClass: string;
  rootStackTrace: string;
}
