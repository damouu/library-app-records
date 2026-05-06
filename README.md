# Library App Records Service / 利用履歴サービス

Distributed Library System — Records Service

分散型図書館システム — 利用履歴サービス

---

## Overview / 概要

The Records Service is a core microservice within the distributed library platform.

It is responsible for consuming borrow and return events through Kafka, managing the history of library transactions,
and providing user-specific borrowing records to the frontend via a secured REST API.

The service operates asynchronously for event ingestion and acts as the source of truth for all historical library
transactions.

---

Records Service は分散型図書館システムにおける貸出および返却履歴を管理するサービスです。

Kafka から貸出・返却イベントを非同期に消費し、ユーザーごとの貸出履歴を REST API としてフロントエンドに提供します。

本サービスはイベント駆動で履歴を記録し、すべての図書館取引履歴の単一責任ソースとして機能します。

---

## Service Boundaries / サービス境界

### Provides

- Borrow and return event consumption
- User borrowing history management
- REST API for frontend consumption
- Secure API access via OAuth2
- Records persistence
- API documentation via OpenAPI

### Does Not Handle

- Member card generation
- Library catalog management
- Book inventory tracking
- Notification dispatching
- User authentication and credential management

---

## Badges

<!-- Code Quality & Tests -->
[![Tests](https://github.com/damouu/library-app-records/actions/workflows/run-tests.yml/badge.svg?branch=test)](https://github.com/damouu/library-app-records/actions/workflows/run-tests.yml)

[![Merge PR](https://github.com/damouu/library-app-records/actions/workflows/merge-pr.yml/badge.svg)](https://github.com/damouu/library-app-records/actions/workflows/merge-pr.yml)

[![Prepare](https://github.com/damouu/library-app-records/actions/workflows/prepare.yml/badge.svg)](https://github.com/damouu/library-app-records/actions/workflows/prepare.yml)

<!-- Coverage -->
[![Codecov](https://codecov.io/gh/damouu/library-app-records/branch/test/graph/badge.svg)](https://codecov.io/gh/damouu/library-app-records)

<!-- Git / Version -->
[![Git Tag](https://img.shields.io/github/v/tag/damouu/library-app-records?logo=github)](https://github.com/damouu/library-app-records/tags)

<!-- Observability -->
![Kafka](https://img.shields.io/badge/Kafka-integrated-orange)

![Prometheus](https://img.shields.io/badge/Prometheus-monitored-blue)

---

## Responsibilities / 責務

### English

- Consume borrow and return events
- Persist transaction records
- Provide user history via REST API
- Secure endpoints with OAuth2 Resource Server
- Generate interactive API documentation
- Handle asynchronous record processing

### 日本語

- 貸出・返却イベント処理
- 取引履歴の永続化
- REST API経由での履歴提供
- OAuth2によるエンドポイント保護
- APIドキュメントの生成
- 非同期履歴処理

---

## Technology Stack / 技術スタック

| Category    | Technology                               |
|-------------|------------------------------------------|
| Runtime     | Java 21                                  |
| Framework   | Spring Boot 2.7                          |
| Web / API   | Spring Web / Springdoc OpenAPI 1.7       |
| Security    | Spring Security (OAuth2 Resource Server) |
| Messaging   | Kafka                                    |
| Persistence | Spring Data JPA                          |
| Database    | PostgreSQL / H2                          |
| Validation  | Bean Validation                          |
| Monitoring  | Micrometer / Prometheus / Actuator       |
| Testing     | JUnit 5 / Mockito / JaCoCo / Instancio   |
| CI/CD       | GitHub Actions                           |

---

## Event Processing & APIs / イベント処理とAPI

Consumes Kafka borrow/return events and exposes secure REST endpoints.

Processes:

- Borrow and return events from the message broker
- Transaction persistence
- Client requests for borrowing history
- JWT validation for protected resources

---

## Example Event Payload

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "bookId": "123e4567-e89b-12d3-a456-426614174000",
  "action": "BORROW",
  "timestamp": "2026-05-06T10:00:00Z"
}

```

---

## Processing Pipeline / 処理パイプライン

Event Ingestion:

Borrow/Return Event

↓

Kafka Consumer

↓

Records Processing Layer

↓

Database Persistence

Data Retrieval:
Frontend Client Request (with JWT)

↓

REST API Controller (OAuth2 Secured)

↓

Database Query

↓

JSON Response

---

## Local Development / ローカル開発

### Requirements

- Java 9
- Maven
- Docker
- PostgreSQL
- Kafka

---

### Run

```bash
docker compose up --build
```

---

## Testing / テスト

```bash
./mvnw verify
```

Includes:

- Unit tests
- Integration tests
- Coverage verification

---

## Coverage Policy / カバレッジポリシー

JaCoCo quality gates:

- **Line coverage ≥ 80%**
- **Complexity missed count ≤ 3**

Excluded from coverage:

- DTO classes (/dto/)
- Model/Entity classes (/model/, /entity/)
- View classes (/view/)
- Configuration classes (/*Config.class)
- Bootstrap application class (DemoApplication.class)
- Generated sources (/generated/)

---

## Configuration / 設定

```env
SPRING_DATASOURCE_URL=
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
SPRING_KAFKA_BOOTSTRAP_SERVERS=
```

Environment-driven configuration.

---

## Monitoring / モニタリング

```text
/actuator/health
/actuator/prometheus
/actuator/metrics
```

---

## Build Quality / 品質保証

The build pipeline enforces:

- Automated test execution
- Coverage thresholds
- Pull request validation
- CI verification
- Workflow consistency checks

---

## Future Improvements / 今後の改善

- Reading history analytics and insights
- Exporting history to CSV/PDF
- Real-time indexing for fast search
- Advanced filtering and pagination for large histories

---

## License / ライセンス

MIT