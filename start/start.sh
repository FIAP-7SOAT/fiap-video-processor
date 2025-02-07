#!/bin/bash

NETWORK_SHARED="fiap-shared-network"
NETWORK_NAME="fiap-db-network"
AUTH_SERVICE="fiap-auth-service-app"
MAILDEV_SERVICE="maildev"

# Check if networks exist, create if they don't
for NETWORK in "$NETWORK_SHARED" "$NETWORK_NAME"; do
    if ! docker network ls --format '{{.Name}}' | grep -q "^${NETWORK}\$"; then
        echo "Creating network ${NETWORK}..."
        docker network create ${NETWORK}
    else
        echo "Network ${NETWORK} already exists."
    fi
done

# Check if MailDev container is running
check_maildev() {
    if docker ps --format '{{.Names}}' | grep -q "^${MAILDEV_SERVICE}\$"; then
        echo "MailDev service is running."
        return 0
    else
        echo "MailDev service is not running. Please start fiap-auth-service first."
        return 1
    fi
}

# Check if fiap-auth-service is running
check_auth_service() {
    if docker ps --format '{{.Names}}' | grep -q "^${AUTH_SERVICE}\$"; then
        echo "Auth service is running."
        return 0
    else
        echo "Auth service is not running. Please start fiap-auth-service first."
        return 1
    fi
}

# Check for required services
if check_maildev && check_auth_service; then
    echo "Starting fiap-video-processor..."
    docker-compose up -d
else
    echo "Required services are not running. Please start fiap-auth-service first."
    exit 1
fi