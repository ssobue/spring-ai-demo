# 利用者ガイド（日本語）

## 概要

Spring Boot + Spring AI のデモアプリです。ローカルで稼働する Ollama に接続し、モデル `gpt-oss:20b` を使用します（`application.yml` 既定）。

## 前提条件

- macOS + Homebrew
- Java 21（`java -version`）
- Maven 3.9+（`mvn -v`）

## Ollama のインストール（Homebrew）

```bash
brew install ollama
# バックグラウンド起動（推奨）
brew services start ollama
# 代替: フォアグラウンド起動
# ollama serve
# 動作確認（タグ一覧）
curl -s http://localhost:11434/api/tags | jq . 2>/dev/null || curl -s http://localhost:11434/api/tags
```

## モデルの準備（gpt-oss:20b）

```bash
ollama pull gpt-oss:20b
# 簡易確認（終了は Ctrl+C）
ollama run gpt-oss:20b
```

## 設定の要点

- 既定の接続先: `http://localhost:11434`（`src/main/resources/application.yml`）
- 既定モデル: `gpt-oss:20b`
- `SPRING_AI_OPENAI_API_KEY` はローカル Ollama では実質未使用ですが、Spring AI の都合で値が必要な場合は `dummy` を設定してください。
- 環境変数で上書き可能:
```bash
export SPRING_AI_OPENAI_BASE_URL=http://localhost:11434
export SPRING_AI_OPENAI_API_KEY=dummy
```

## アプリの起動

開発プロファイル（`dev`）で起動します。Spring Shell の対話モードが有効です。

```bash
# ソースから実行
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# またはパッケージして実行
mvn -q clean package
java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar
```

起動後、プロンプトが表示されたら `help` で利用可能コマンドを確認できます。

## トラブルシューティング

- ポート確認: `lsof -i :11434`（Ollama が LISTEN しているか）
- モデル未取得: `ollama pull gpt-oss:20b` を再実行
- タイムアウト/接続失敗: `brew services restart ollama` または `ollama serve` を手動起動
- 設定反映: `application.yml` と環境変数の競合に注意（環境変数が優先）

## ライセンス/注意

本リポジトリには実利用の API キーを含めないでください。モデルと生成物のライセンス・利用規約は各提供元に従ってください。
