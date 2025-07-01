import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { environment } from './environments/environment';

function loadConfig(): Promise<void> {
  return fetch('/assets/config.json')
    .then((res) => res.json())
    .then((config) => {
      (window as any).__env = config;
    });
}

const shouldUseRuntimeConfig = environment.production && environment.useRuntimeConfig;

(shouldUseRuntimeConfig ? loadConfig() : Promise.resolve()).then(() => {
  bootstrapApplication(AppComponent, appConfig).catch((err) => console.error(err));
});
