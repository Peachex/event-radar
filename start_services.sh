#!/bin/bash

# Function to check if docker-compose file exists
function check_docker_compose() {
    if [ ! -f "$1/docker-compose.yml" ]; then
        echo "Error: docker-compose.yml not found in $1"
        exit 1
    fi
}

# Function to wait for a service to be up
function wait_for_service() {
    service_name=$1
    echo "Waiting for $service_name to be up and running..."
    echo ""  # Empty line for separation

    # Check the status using docker-compose ps
    while true; do
        # Check if all containers are running
        docker-compose -f "$service_name/docker-compose.yml" ps -q | xargs docker inspect -f '{{.State.Running}}' | grep -q "true" && break
        echo "$service_name is not yet running. Retrying..."
        sleep 5
    done

    echo "$service_name is now running!"
    echo ""  # Empty line for separation
}

# Function to display ASCII art of the module
function display_ascii_art() {
    module_name=$1
    case $module_name in
        "event-persistor")
            cat <<'EOF'
                      _                              _     _             
                     | |                            (_)   | |            
  _____   _____ _ __ | |_ ______ _ __   ___ _ __ ___ _ ___| |_ ___  _ __ 
 / _ \ \ / / _ \ '_ \| __|______| '_ \ / _ \ '__/ __| / __| __/ _ \| '__|
|  __/\ V /  __/ | | | |_       | |_) |  __/ |  \__ \ \__ \ || (_) | |   
 \___| \_/ \___|_| |_|\__|      | .__/ \___|_|  |___/_|___/\__\___/|_|   
                                | |                                      
                                |_|                                      
EOF
            ;;
        "sync-task-scheduler")
            cat <<'EOF'
                              _            _                    _              _       _           
                             | |          | |                  | |            | |     | |          
  ___ _   _ _ __   ___ ______| |_ __ _ ___| | ________ ___  ___| |__   ___  __| |_   _| | ___ _ __ 
 / __| | | | '_ \ / __|______| __/ _ \ __| |/ /______/ __|/ __| '_ \ / _ \/ _` | | | | |/ _ \ '__|
 \__ \ |_| | | | | (__       | || (_| \__ \   <       \__ \ (__| | | |  __/ (_| | |_| | |  __/ |   
 |___/\__, |_| |_|\___|       \__\__,_|___/_|\_\      |___/\___|_| |_|\___|\__,_|\__,_|_|\___|_|   
       __/ |                                                                                       
      |___/                                                                                         
EOF
            ;;
        "event-manager")
            cat <<'EOF'
                       _                                                      
                      | |                                                     
   _____   _____ _ __ | |_ ______ _ __ ___   __ _ _ __   __ _  __ _  ___ _ __ 
  / _ \ \ / / _ \ '_ \| __|______| '_ ` _ \ / _` | '_ \ / _` |/ _` |/ _ \ '__|
 |  __/\ V /  __/ | | | |_       | | | | | | (_| | | | | (_| | (_| |  __/ |   
  \___| \_/ \___|_| |_|\__|      |_| |_| |_|\__,_|_| |_|\__,_|\__, |\___|_|   
                                                               __/ |          
                                                              |___/            
EOF
            ;;
        "event-web-app")
            cat <<'EOF'
                       _                       _                            
                      | |                     | |                           
   _____   _____ _ __ | |_ ________      _____| |__ ______ __ _ _ __  _ __  
  / _ \ \ / / _ \ '_ \| __|______\ \ /\ / / _ \ '_ \______/ _` | '_ \| '_ \ 
 |  __/\ V /  __/ | | | |_        \ V  V /  __/ |_) |    | (_| | |_) | |_) |
  \___| \_/ \___|_| |_|\__|        \_/\_/ \___|_.__/      \__,_| .__/| .__/ 
                                                               | |   | |    
                                                               |_|   |_|    
EOF
            ;;
        *)
            echo "No ASCII art found for $module_name"
            ;;
    esac
    echo ""  # Empty line for separation
}

# List of directories in the order in which you want to start the services
services=("event-persistor" "sync-task-scheduler" "event-manager" "event-web-app")

# Iterate over the services and bring them up
for service in "${services[@]}"; do
    display_ascii_art "$service"  # Display the ASCII art of the module

    echo "Starting service: $service"
    echo ""  # Empty line for separation
    
    # Check if the docker-compose file exists in the service directory
    check_docker_compose "$service"

    # Navigate to the service's directory and run docker-compose up -d
    cd "$service" || exit
    docker-compose up -d
    cd - || exit

    # Wait for the service to be fully up before continuing to the next one
    wait_for_service "$service"

    echo "$service started and ready."
    echo ""  # Empty line for separation
done

echo "All services started in order."
echo ""  # Empty line for final separation
