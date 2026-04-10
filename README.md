# AGMS System

AGMS (Automated Greenhouse Management System) is a Spring Boot microservices project with an API Gateway, service discovery, centralized config, and domain services for zones, sensors, automation, and crops.

## Architecture

Services in this repository:

- `config-server` (Spring Cloud Config Server)
- `eureka-server` (Service Discovery)
- `api-gateway` (Auth + routing)
- `zone-service` (Zone and threshold management)
- `sensor-service` (Telemetry polling and forwarding)
- `automation-service` (Rule evaluation and action logging)
- `crop-service` (Crop management)

External dependency:

- IoT platform API: `http://104.211.95.241:8080/api`

## Ports

- API Gateway: `8080`
- Zone Service: `8081`
- Sensor Service: `8082`
- Automation Service: `8083`
- Crop Service: `8084`
- Config Server: `8888`
- Eureka Server: `8761`

## Security Model

- Client authentication is performed at API Gateway via `POST /auth/login`.
- Gateway issues AGMS JWT (signed with `JWT_SECRET`).
- Protected routes require `Authorization: Bearer <token>`.
- Internal call chain forwards JWT across services:
  - sensor-service -> automation-service -> zone-service
- For scheduler-triggered sensor flows (no user context), sensor-service generates a short-lived internal token using `AGMS_INTERNAL_SUBJECT`.

## Prerequisites

- Java 17
- PowerShell (Windows)
- Internet access for Config Server Git backend and IoT API

## Environment Setup

Create a `.env` file in project root:

```env
JWT_SECRET=your-jwt-secret
IOT_USERNAME=your-iot-username
IOT_PASSWORD=your-iot-password
AGMS_INTERNAL_SUBJECT=sensor-service
```

Notes:

- `JWT_SECRET` must be consistent across gateway and backend services.
- `AGMS_INTERNAL_SUBJECT` is an internal identity label, not a password.

## Quick Start (Recommended)

Use the startup script to load `.env` and launch all services:

```powershell
Set-Location "e:\GitHub\agms-system"
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\start-agms.ps1
```

Optional arguments:

```powershell
.\start-agms.ps1 -DelaySeconds 10
.\start-agms.ps1 -EnvFile ".env.local"
```

What the script does:

1. Loads required environment variables from `.env`
2. Validates required keys exist
3. Starts all services in separate terminals in startup order

## Manual Run (Alternative)

If you prefer starting services manually, run in separate terminals:

1. `config-server`
2. `eureka-server`
3. `zone-service`
4. `automation-service`
5. `sensor-service`
6. `crop-service`
7. `api-gateway`

Each service can be started with:

```powershell
.\mvnw.cmd spring-boot:run
```

## API Routing Through Gateway

Gateway routes:

- `/api/zones/**` -> zone-service
- `/api/sensors/**` -> sensor-service
- `/api/automation/**` -> automation-service
- `/api/crops/**` -> crop-service

Public auth route:

- `POST /auth/login`

## Happy Path Verification

Goal: validate the full chain gateway -> sensor -> automation -> zone.

1. Login and obtain AGMS JWT
2. Call protected sensor endpoint with token
3. Wait at least 20 seconds for scheduler cycle
4. Check automation logs by zone

### Postman Collection

Use this collection for the full auth chain test:

- `AGMS-HappyPath-Auth-Postman-Collection.json`

Also available:

- `AGMS-Postman-Collection.json`

Collection variables:

- `base_url` (default `http://localhost:8080`)
- `iot_username`
- `iot_password`
- `zone_id` (numeric AGMS zone record id, for example `1`)

## How to Find zone_id

`zone_id` is the AGMS zone table id (not IoT device id).

- Call `GET /api/zones` through gateway with Bearer token
- Use the `id` of the zone you want to test

## Troubleshooting

- 401 on protected routes:
  - Verify Bearer token is present and valid
  - Ensure all services use same `JWT_SECRET`
  - Restart services after config changes

- No automation logs:
  - Ensure `zone_id` in sensor config exists in zone-service
  - Wait for scheduler cycle (`sensor-service` polls every 10 seconds)
  - Check sensor and automation logs for forwarding errors

- Config server startup issues:
  - Confirm internet access to config repository
  - Confirm Git URL is reachable

## Important Files

- `start-agms.ps1` (startup orchestration)
- `.env` (runtime secrets and auth variables)
- `AGMS-HappyPath-Auth-Postman-Collection.json` (happy-path tests)
- `AGMS-Postman-Collection.json` (general API tests)

## Development Notes

- Services use in-memory H2 databases by default.
- Most services are configured for local development defaults.
- Replace secrets and IoT credentials for production deployments.
