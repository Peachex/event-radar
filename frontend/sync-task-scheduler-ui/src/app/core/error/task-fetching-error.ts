import { ExceptionResponse } from '../model/exception-response';

export class TaskFetchingError extends Error {
  sourceError?: any;
  exceptionResponse?: ExceptionResponse;

  constructor(message: string, exceptionResponse?: ExceptionResponse, sourceError?: Error) {
    super(message);
    this.name = 'TaskFetchingError';
    this.exceptionResponse = exceptionResponse;
    this.sourceError = sourceError;
  }
}
