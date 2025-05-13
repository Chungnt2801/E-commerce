# 🛒 E-commerce
## 🎯 Mục tiêu

Xây dựng một hệ thống thương mại điện tử theo kiến trúc microservices với các tính năng:

- Quản lý danh mục sản phẩm (Catalog Service)  
- Quản lý giỏ hàng (Cart Service)  
- Quản lý đơn hàng (Order Service)  
- Xác thực và phân quyền (Auth Service)  
- Tích hợp thanh toán (Payment Service)  
- Quản lý khuyến mãi (Promotion Service)  
- Quản lý kho hàng (Inventory Service)  
- Quản lý đánh giá và bình luận (Review & Comment Service)  
- Tối ưu hóa hiệu suất với Redis cache  
- Xử lý bất đồng bộ qua RabbitMQ  
- Triển khai CI/CD với Jenkins  
- Containerization với Docker và Kubernetes  
- Tài liệu API với Swagger (OpenAPI)

---

## ⚙️ Công nghệ sử dụng

- **Ngôn ngữ**: Java 21  
- **Backend Framework**: Spring Boot, Spring MVC  
- **ORM**: Hibernate, Spring Data JPA, Spring Data MongoDB  
- **Cơ sở dữ liệu**:
  - **PostgreSQL**: Auth, Cart, Order, Payment, Inventory Services  
  - **MongoDB**: Catalog, Promotion, Review & Comment Services  
- **Caching**: Redis  
- **Message Broker**: RabbitMQ  
- **API Gateway**: Spring Cloud Gateway  
- **Service Discovery**: Eureka  
- **Bảo mật & Xác thực**: Spring Security, JWT  
- **CI/CD**: Jenkins  
- **Containerization**: Docker + Kubernetes  
- **Tài liệu API**: Swagger (SpringDoc OpenAPI)  
- **Build Tool**: Gradle

---

## 🧱 Kiến trúc Microservices

### 🔗 API Gateway

- Là điểm vào duy nhất cho tất cả các yêu cầu HTTP.
- Xác thực JWT, định tuyến các request đến đúng microservice.
- Có thể cấu hình thêm rate limiting, logging, etc.

### 📌 Service Discovery - Eureka

- Quản lý danh sách các service đang hoạt động.
- Hỗ trợ load balancing khi tích hợp với Spring Cloud LoadBalancer hoặc Feign.

### 🧩 Các microservices

| Service Name         | Chức năng chính                                | Cơ sở dữ liệu |
|----------------------|------------------------------------------------|---------------|
| Auth Service         | Xác thực, đăng ký, JWT, phân quyền             | PostgreSQL    |
| Catalog Service      | Quản lý sản phẩm, danh mục                     | MongoDB       |
| Cart Service         | Quản lý giỏ hàng                               | PostgreSQL    |
| Order Service        | Quản lý đơn hàng, lịch sử đặt hàng             | PostgreSQL    |
| Payment Service      | Tích hợp thanh toán (VNPay, PayPal, etc.)      | PostgreSQL    |
| Promotion Service    | Quản lý khuyến mãi, mã giảm giá                | MongoDB       |
| Inventory Service    | Quản lý tồn kho, số lượng sản phẩm             | PostgreSQL    |
| Review & Comment     | Quản lý đánh giá và bình luận sản phẩm         | MongoDB       |

---

## 🔁 Giao tiếp giữa các services

### 1. **Đồng bộ (HTTP - REST)**

- Sử dụng Feign Client (Spring Cloud OpenFeign) hoặc RestTemplate.
- Ví dụ:
  - Cart Service gọi Catalog Service để lấy thông tin sản phẩm.
  - Order Service gọi Inventory và Payment để kiểm tra và đặt hàng.

### 2. **Bất đồng bộ (RabbitMQ)**

- Các sự kiện gửi/nhận qua các queue/topic RabbitMQ.
- Ví dụ:
  - Khi tạo đơn hàng thành công, gửi event `OrderCreated` → Inventory xử lý giảm tồn.
  - Khi có đánh giá mới, gửi event → Notification Service (nếu có).
  - Khi tạo khuyến mãi mới → các services khác cập nhật cache.

---

## 🚀 Redis Caching

- Tăng hiệu năng truy xuất dữ liệu "read-heavy".
- Sử dụng Redis cache với các annotation Spring như `@Cacheable`, `@CachePut`, `@CacheEvict`.
- Dữ liệu được cache:
  - Danh sách sản phẩm phổ biến
  - Khuyến mãi đang chạy
  - Đánh giá sản phẩm

---

## 🔧 CI/CD với Jenkins

### ✔️ Pipeline mẫu:

1. Clone source code  
2. Chạy `./gradlew build`  
3. Test tự động với JUnit  
4. Build Docker image  
5. Push Docker image lên Docker Hub  
6. Deploy lên Kubernetes Cluster

### 🧩 Tích hợp:

- Webhook GitHub/GitLab → Jenkins auto-trigger  
- Multi-branch pipeline  
- Quản lý secret với Jenkins Credential hoặc Vault  

---

## 🐳 Docker & ☸️ Kubernetes

### Docker

- Mỗi microservice là một Docker container độc lập.
- Dockerfile đơn giản (tối ưu image size bằng JDK slim hoặc GraalVM nếu cần).
- Sử dụng Docker Compose cho local development.

### Kubernetes

- Từng service deploy bằng `Deployment` + `Service`.
- Ingress Controller để expose các route.
- Dùng `ConfigMap` & `Secret` cho cấu hình.
- Áp dụng:
  - `Horizontal Pod Autoscaler` (HPA)
  - `Liveness` và `Readiness` probe
  - `Node Affinity`, `PodAntiAffinity`

---

## 📚 Swagger (OpenAPI)

- Tài liệu API tự động với SpringDoc OpenAPI tại `/swagger-ui.html`.
- Mỗi service có tài liệu riêng.
- Có thể tạo 1 Swagger Aggregator tại API Gateway:
  - Tổng hợp tất cả OpenAPI spec từ các services.
  - Giúp team Frontend hoặc QA dễ truy cập và test.

---

## 🔌 Khả năng mở rộng và bảo trì

### Mở rộng theo chiều ngang (Horizontal Scaling)

- Mỗi service có thể được scale độc lập theo nhu cầu:
  - Order Service nhiều traffic → scale lên 5 pods
  - Review Service ít hơn → chỉ cần 1 pod

### Phân tách theo domain (Domain-Driven Design)

- Áp dụng DDD để tổ chức các module rõ ràng:
  - Order Aggregate
  - Product Aggregate
  - Promotion Context
  - Auth & Identity

---

## 📈 Giám sát và Logging

- **Logging**: Spring Boot + Logback + ELK Stack (Elasticsearch, Logstash, Kibana)
- **Monitoring**: Prometheus + Grafana
- **Tracing**: Spring Cloud Sleuth + Zipkin (hoặc OpenTelemetry)

---

## 📎 Tài liệu bổ sung (Đề xuất)

- 🧪 Postman Collection cho API test  
- 📜 Sơ đồ kiến trúc hệ thống (vẽ bằng draw.io, Lucidchart, PlantUML, v.v.)  
- 🔐 Chính sách bảo mật JWT, Refresh Token  
- 📤 Thư mục `k8s/` chứa các file YAML cho từng service

---

> ✅ **Ghi chú**: Để triển khai thành công hệ thống này, cần CI/CD hoạt động ổn định, devops hiểu rõ cấu trúc, logging và monitoring đầy đủ từ đầu.

