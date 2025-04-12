import { HttpErrorResponse } from '@angular/common/http';
import { ExceptionResponse } from '../model/exception-response';
import { throwError } from 'rxjs';
import { TaskSchedulerError } from '../error/task-scheduler-error';

export class ErrorUtil {
  static createErrorMessageBasedOnErrorStatus(status: number): string {
    let errorMessage;
    switch (status) {
      case 400:
        errorMessage = 'Invalid request. Please check your input.';
        break;
      case 404:
        errorMessage = 'Task not found.';
        break;
      case 500:
        errorMessage = 'Internal server error. Try again later.';
        break;
      default:
        errorMessage = `Unexpected error: ${status}`;
    }
    return errorMessage;
  }

  static handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Client error: ${error.error.message}`;
    } else {
      errorMessage = ErrorUtil.createErrorMessageBasedOnErrorStatus(error.status);
    }

    let exceptionResponse = error.error as ExceptionResponse;
    console.error(exceptionResponse);

    return throwError(() => new TaskSchedulerError(errorMessage, error.error));
  }
}
