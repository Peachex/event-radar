export class TaskFetchingError extends Error {
  sourceError?: any;

  constructor(message: string, sourceError?: Error) {
    super(message);
    this.name = 'TaskErTaskFetchingError';
    this.sourceError = sourceError;
  }
}
