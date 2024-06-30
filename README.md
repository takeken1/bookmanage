# 書籍管理システム

このプロジェクトは、著者と書籍の管理を目的としたシンプルな書籍管理システムです。
書籍と著者のCRUD操作を行うことができます。

## 技術スタック
- 言語: Kotlin
- フレームワーク: Spring Boot
- データベース: PostgreSQL
- マイグレーション: Flyway
- ORM: jOOQ
- ビルドツール: Gradle
- テストフレームワーク: Kotest, MockK

## セットアップ

### 必要なツール
- JDK 21
- Gradle
- Docker (PostgreSQLの実行に使用)

### SDKMAN によるJDKインストール方法(Optional)
```sh
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
$ sdk version
$ sdk install java
Do you want java 21.0.3-tem to be set as default? (Y/n): y
$ java -version
```

### ビルド方法
プロジェクトのルートディレクトリで以下のコマンドを実行してください。

```sh
./gradlew clean build
```

### データベースのセットアップ
Dockerを使用してPostgreSQLをセットアップします。

```sh
docker-compose up -d
```

### データベースマイグレーション
Flywayを使用してデータベーススキーマをマイグレーションします。

```sh
./gradlew flywayMigrate
```

### JOOQの使用
データベース操作のためにJOOQを使用しています、

設定は、`build.gradle` ファイル内で行われています。

JOOQのコード生成タスクは、Flywayによるデータベースマイグレーション後に実行する必要があります。

これにより、最新のデータベーススキーマに基づいて、JOOQによるクラスが生成されます。

#### コード生成
JOOQのコード生成を実行するには、以下のコマンドを実行します。

コードは`build/generated-src/jooq/main`ディレクトリに生成されます。

```sh
./gradlew generateJooq
```

### アプリケーションの実行
ビルドが完了した後、以下のコマンドでアプリケーションを実行します。

```sh
./gradlew bootRun
```

## テスト
テストを実行します。

```sh
./gradlew test
```

## 使用方法
### エンドポイント

#### 著者関連のエンドポイント

- **著者を取得する**: `GET /api/authors/{id}`
	- 指定されたIDの著者情報を取得します。
- **著者一覧を取得する**: `GET /api/authors`
	- 登録されている全著者の一覧を取得します。
- **著者を作成する**: `POST /api/authors`
	- 新しい著者を作成します。著者名はリクエストボディに含めます。
- **著者を更新する**: `PUT /api/authors/{id}`
	- 指定されたIDの著者情報を更新します。更新情報はリクエストボディに含めます。
- **著者を削除する**: `DELETE /api/authors/{id}`
	- 指定されたIDの著者を削除します。

#### 書籍関連のエンドポイント

- **書籍を取得する**: `GET /api/books/{id}`
	- 指定されたIDの書籍情報を取得します。
- **書籍一覧を取得する**: `GET /api/books`
	- 登録されている全書籍の一覧を取得します。
- **著者に紐づく書籍一覧を取得する**: `GET /api/books/author/{authorId}`
	- 指定された著者IDに紐づく書籍の一覧を取得します。
- **書籍を作成する**: `POST /api/books`
	- 新しい書籍を作成します。書籍情報はリクエストボディに含めます。
- **書籍を更新する**: `PUT /api/books/{id}`
	- 指定されたIDの書籍情報を更新します。更新情報はリクエストボディに含めます。
- **書籍を削除する**: `DELETE /api/books/{id}`
	- 指定されたIDの書籍を削除します。
