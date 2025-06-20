import { ExceptionResponse } from '../model/exception-response';

export class EventsFetchingError extends Error {
  sourceError?: any;
  exceptionResponse?: ExceptionResponse;

  constructor(
    message: string,
    exceptionResponse?: ExceptionResponse,
    sourceError?: Error
  ) {
    super(message);
    this.name = 'EventsFetchingError';
    this.exceptionResponse = exceptionResponse;
    this.sourceError = sourceError;
  }
}
