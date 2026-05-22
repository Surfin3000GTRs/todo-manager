# todo-manager

Spring Boot + Thymeleaf で作った学習用のシンプルなタスク管理アプリです。

## 必要要件
- Java 17+
- Maven 3.9+

## 起動方法（Web）
```bash
mvn spring-boot:run
```

起動後、`http://localhost:8080/tasks` にアクセスしてください。

## CLI の使い方（最小構成）
同じ `tasks.json` を使って CLI からも操作できます。

```bash
# 一覧
mvn spring-boot:run -Dspring-boot.run.arguments="cli list"

# 追加
mvn spring-boot:run -Dspring-boot.run.arguments="cli add '買い物' '牛乳を買う'"

# 更新
mvn spring-boot:run -Dspring-boot.run.arguments="cli update 1 '買い物(修正)' '牛乳と卵を買う' true"

# 削除
mvn spring-boot:run -Dspring-boot.run.arguments="cli delete 1"
```

## 実装方針
- DB は使わず `tasks.json` に永続化
- 起動時に `tasks.json` を読み込み、追加/更新/削除時に保存
- Web UI は Thymeleaf
- モデル / サービス / 永続化 / コントローラを分離
