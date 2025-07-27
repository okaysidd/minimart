# minimart

A microservices-based e-commerce backend system for order processing, inventory management, payment, shipping, and notifications. Built with Java 21, Spring Boot, Kafka, PostgreSQL, MongoDB, Redis, and instrumented for observability with OpenTelemetry, Jaeger, Prometheus, and Grafana.

## Architecture

- **order-svc**: Handles order creation (`POST /orders`), persists orders, and initiates inventory checks.
- **inventory-svc**: Listens for order events, checks/reserves inventory, and notifies success/failure.
- **payment-svc**: Handles payment capture and refund based on inventory and shipping events.
- **shipping-svc**: Prepares shipments and updates shipping status.
- **notification-svc**: Enqueues notifications (success/failure) to Redis streams for further processing.

All services communicate asynchronously via Kafka.

## Tech Stack
- Java 21, Spring Boot 3.5.4
- Apache Kafka
- PostgreSQL (orders, payments)
- MongoDB (shipping)
- Redis (notifications)
- OpenTelemetry, Jaeger, Prometheus, Grafana
- Docker Compose for orchestration

## Running the Stack

1. **Clone the repository:**
   ```sh
   git clone <repo-url>
   cd minimart
   ```
2. **Start all services and dependencies:**
   ```sh
   docker-compose up --build
   ```
3. **Access dashboards:**
   - Jaeger UI: [http://localhost:16686](http://localhost:16686)
   - Prometheus: [http://localhost:9090](http://localhost:9090)
   - Grafana: [http://localhost:3000](http://localhost:3000)

## API Endpoints

### Order Service
- `POST /orders` — Create a new order
  - Example request body:
    ```json
    {
      "customerId": 123,
      "items": [
        { "sku": "SKU-42", "quantity": 1 }
      ]
    }
    ```

Other services communicate via Kafka and do not expose direct REST endpoints.

## Observability
- **Tracing:** OpenTelemetry Collector exports traces to Jaeger.
- **Metrics:** Exposed via Prometheus and visualized in Grafana.
- Configuration files: `otel-collector-config.yml`, `prometheus.yml`

## Load Testing
- The `k6-load-test/` directory is reserved for load testing scripts (currently empty).

## Development
- Each service is a standalone Spring Boot application with its own `pom.xml` and Dockerfile.
- To run a service locally (example for order-svc):
  ```sh
  cd services/order-svc
  ./mvnw spring-boot:run
  ```

## License
MIT (or specify your license here)