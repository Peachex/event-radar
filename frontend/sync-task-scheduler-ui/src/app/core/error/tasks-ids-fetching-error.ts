import { ExceptionResponse } from '../model/exception-response';

export class TasksIdsFetchingError extends Error {
  sourceError?: any;
  exceptionResponse?: ExceptionResponse;

  constructor(message: string, exceptionResponse?: ExceptionResponse, sourceError?: Error) {
    super(message);
    this.name = 'TasksIdsFetchingError';
    this.exceptionResponse = exceptionResponse;
    this.sourceError = sourceError;
  }
}
