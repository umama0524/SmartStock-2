# SmartStock バックエンド セットアップ手順書

## 1. 必要な環境

- **Java**: JDK 17以上
- **Maven**: 3.8以上
- **MySQL**: 8.0以上（本番環境用）
- **IDE**: IntelliJ IDEA または VS Code（推奨）

---

## 2. プロジェクト構造

```
backend/
├── pom.xml                          # Maven設定
├── src/
│   ├── main/
│   │   ├── java/com/smartstock/
│   │   │   ├── SmartStockApplication.java    # エントリーポイント
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java       # Spring Security設定
│   │   │   │   ├── CorsConfig.java           # CORS設定
│   │   │   │   └── DataLoader.java           # 初期データ投入
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java       # 認証API
│   │   │   │   └── InventoryController.java  # 在庫API
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── LoginResponse.java
│   │   │   │   ├── UserDto.java
│   │   │   │   └── InventoryDto.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   ├── Inventory.java
│   │   │   │   ├── InventoryTransaction.java
│   │   │   │   ├── Supplier.java
│   │   │   │   ├── Customer.java
│   │   │   │   ├── PurchaseOrder.java
│   │   │   │   ├── PurchaseOrderItem.java
│   │   │   │   ├── SalesOrder.java
│   │   │   │   └── SalesOrderItem.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── InventoryRepository.java
│   │   │   │   ├── SupplierRepository.java
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── PurchaseOrderRepository.java
│   │   │   │   ├── SalesOrderRepository.java
│   │   │   │   └── InventoryTransactionRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtUtil.java              # JWT生成・検証
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── service/
│   │   │       ├── AuthService.java
│   │   │       └── InventoryService.java
│   │   └── resources/
│   │       └── application.yml               # アプリケーション設定
│   └── test/
└── README.md
```

---

## 3. セットアップ手順

### 3.1 開発環境（H2 Database）

H2はインメモリデータベースなので、追加のセットアップは不要です。

```bash
# プロジェクトディレクトリに移動
cd backend

# 依存関係のダウンロードとビルド
mvn clean install

# アプリケーション起動（開発モード）
mvn spring-boot:run
```

起動後:
- API: http://localhost:8080/api
- H2コンソール: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:smartstock`
  - User: `sa`
  - Password: （空欄）

### 3.2 本番環境（MySQL）

#### MySQLセットアップ

```sql
-- データベース作成
CREATE DATABASE smartstock CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ユーザー作成
CREATE USER 'smartstock_user'@'localhost' IDENTIFIED BY 'your_password_here';

-- 権限付与
GRANT ALL PRIVILEGES ON smartstock.* TO 'smartstock_user'@'localhost';
FLUSH PRIVILEGES;
```

#### application.yml修正

```yaml
# src/main/resources/application.yml
spring:
  profiles:
    active: prod  # devからprodに変更
```

#### 起動

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 4. API エンドポイント

### 4.1 認証 API

| メソッド | エンドポイント | 説明 |
|---------|---------------|------|
| POST | `/api/auth/login` | ログイン |
| POST | `/api/auth/logout` | ログアウト |

**ログインリクエスト例:**
```json
POST /api/auth/login
{
  "username": "admin",
  "password": "password"
}
```

**レスポンス:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "name": "管理者",
    "email": "admin@smartstock.com",
    "role": "admin"
  }
}
```

### 4.2 在庫 API

| メソッド | エンドポイント | 説明 |
|---------|---------------|------|
| GET | `/api/inventory` | 在庫一覧取得 |
| GET | `/api/inventory?search=keyword` | 在庫検索 |
| GET | `/api/inventory?status=critical` | ステータスでフィルタ |
| GET | `/api/inventory/{id}` | 在庫詳細取得 |
| POST | `/api/inventory/{id}/inbound` | 入庫登録 |
| POST | `/api/inventory/{id}/outbound` | 出庫登録 |
| POST | `/api/inventory/{id}/adjust` | 在庫調整 |

**入庫リクエスト例:**
```json
POST /api/inventory/1/inbound
Authorization: Bearer {token}
{
  "quantity": 100,
  "lotNumber": "LOT-2025-001",
  "note": "定期発注分"
}
```

---

## 5. テストアカウント

| ユーザー名 | パスワード | 役割 |
|-----------|-----------|------|
| admin | password | 管理者 |
| inventory | password | 在庫担当 |
| purchase | password | 発注担当 |
| sales | password | 販売担当 |

---

## 6. フロントエンドとの連携

### 6.1 axiosの設定（フロントエンド側）

```typescript
// src/api/axios.ts
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// リクエストインターセプター（トークン付与）
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// レスポンスインターセプター（エラーハンドリング）
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 6.2 認証ストアの更新（Zustand）

```typescript
// src/store/authStore.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import api from '../api/axios';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,

      login: async (username: string, password: string) => {
        const response = await api.post('/auth/login', { username, password });
        const { token, user } = response.data;
        
        localStorage.setItem('token', token);
        set({ user, token, isAuthenticated: true });
      },

      logout: () => {
        localStorage.removeItem('token');
        set({ user: null, token: null, isAuthenticated: false });
      },
    }),
    {
      name: 'auth-storage',
    }
  )
);
```

---

## 7. 今後の拡張（Phase 4.5以降）

### 実装予定のAPI

- `GET/POST /api/purchases` - 発注一覧・作成
- `GET/PUT /api/purchases/{id}` - 発注詳細・更新
- `POST /api/purchases/{id}/approve` - 発注承認
- `POST /api/purchases/{id}/receive` - 入荷登録
- `GET/POST /api/sales` - 販売一覧・作成
- `GET/PUT /api/sales/{id}` - 販売詳細・更新
- `POST /api/sales/{id}/ship` - 出荷登録
- `GET /api/dashboard/stats` - ダッシュボード統計

---

## 8. トラブルシューティング

### Q: CORSエラーが発生する

A: `CorsConfig.java` の `allowedOrigins` にフロントエンドのURLを追加してください。

### Q: 認証エラー（401）が発生する

A: 
1. トークンの有効期限を確認
2. `Authorization: Bearer {token}` ヘッダーが正しく設定されているか確認

### Q: H2コンソールにアクセスできない

A: `SecurityConfig.java` で `/h2-console/**` が `permitAll()` になっているか確認。

---

## 9. 参考リンク

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [JWT.io](https://jwt.io/)
