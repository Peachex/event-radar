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
}
