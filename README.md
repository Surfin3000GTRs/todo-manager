# todo-manager

Spring Boot を使った TODO 管理アプリの学習用リポジトリです。

## 前提

- Java 21
- Gradle 9.x

## 起動方法

### Windows

```powershell
.\gradlew.bat bootRun
```

### Mac/Linux

```sh
./gradlew bootRun
```

## テスト

### Windows

```powershell
.\gradlew.bat test
```

### Mac/Linux

```sh
./gradlew.bat test
```

## 方針

- まずは Web / Thymeleaf / JSON の土台を整える
- CRUD 実装は後続 Issue で追加する
- パッケージは `controller`, `service`, `model`, `repository`, `cli` に分ける
