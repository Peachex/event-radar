export type TaskValidationErrors = {
  name?: string;
  cronExpression?: string;
  taskIdToExecute?: string;
};
