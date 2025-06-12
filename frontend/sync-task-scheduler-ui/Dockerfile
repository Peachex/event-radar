FROM nginx:alpine

# Clean Nginx default
RUN rm -rf /usr/share/nginx/html/*

# Copy Angular build output (adjust if needed)
COPY dist/sync-task-scheduler-ui/browser /usr/share/nginx/html/

# Copy config template and entrypoint
COPY public/assets/config.template.js /usr/share/nginx/html/public/assets/config.template.js
COPY entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

# Custom nginx conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
