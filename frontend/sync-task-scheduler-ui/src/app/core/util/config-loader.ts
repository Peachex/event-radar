import { environment } from '../../../environments/environment';

export class ConfigLoader {
  private static config: any = null;

  static get(): any {
    const isRuntime = environment.useRuntimeConfig;
    if (isRuntime && !(window as any).__env) {
      throw new Error('Runtime config not loaded yet!');
    }

    if (!this.config) {
      this.config = isRuntime ? (window as any).__env : environment;
    }

    return this.config;
  }
}
