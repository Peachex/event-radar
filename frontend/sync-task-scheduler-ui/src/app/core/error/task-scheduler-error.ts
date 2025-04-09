import { ExceptionResponse } from '../model/exception-response';

export class TaskSchedulerError extends Error {
  sourceError?: any;
  exceptionResponse?: ExceptionResponse;

  constructor(message: string, exceptionResponse?: ExceptionResponse, sourceError?: Error) {
    super(message);
    this.name = 'TaskSchedulerError';
    this.exceptionResponse = exceptionResponse;
    this.sourceError = sourceError;
  }
}
