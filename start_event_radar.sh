#!/bin/bash

set -e  # Exit immediately if a command exits with a non-zero status

# Function to run start_services.sh in a given directory
run_start_services() {
    local dir=$1

    if [ -f "$dir/start_services.sh" ]; then
        echo "Launching services in $dir..."
        echo ""  # Separation line
        (
            cd "$dir" || exit 1
            chmod +x start_services.sh
            ./start_services.sh
        )
        echo "Services in $dir started successfully."
        echo ""
    else
        echo "Error: start_services.sh not found in $dir"
        exit 1
    fi
}

# Run backend/start_services.sh
run_start_services "backend"

# Run frontend/start_services.sh
run_start_services "frontend"

echo "✅ All services (backend and frontend) started successfully."
