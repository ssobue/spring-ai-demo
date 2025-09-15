# User Guide (English)

## Overview
This is a Spring Boot + Spring AI demo app. It connects to a local Ollama server and uses the `gpt-oss:20b` model (default in `application.yml`).

## Prerequisites
- macOS with Homebrew
- Java 21 (`java -version`)
- Maven 3.9+ (`mvn -v`)

## Install Ollama (Homebrew)
```bash
brew install ollama
# Start in background (recommended)
brew services start ollama
# Alternative: run in foreground
# ollama serve
# Sanity check (list tags)
curl -s http://localhost:11434/api/tags | jq . 2>/dev/null || curl -s http://localhost:11434/api/tags
```

## Prepare the model (gpt-oss:20b)
```bash
ollama pull gpt-oss:20b
# Quick check (Ctrl+C to quit)
ollama run gpt-oss:20b
```

## Configuration
- Default base URL: `http://localhost:11434` (see `src/main/resources/application.yml`).
- Default model: `gpt-oss:20b`.
- `SPRING_AI_OPENAI_API_KEY` is effectively unused with local Ollama, but Spring AI may require a value; set `dummy` if needed.
- Override via environment variables:
```bash
export SPRING_AI_OPENAI_BASE_URL=http://localhost:11434
export SPRING_AI_OPENAI_API_KEY=dummy
```

## Run the app
Runs with the `dev` profile. Spring Shell interactive mode is enabled.
```bash
# From sources
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or package and run
mvn -q clean package
java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar
```
After startup, type `help` at the prompt to see available commands.

## Troubleshooting
- Port check: `lsof -i :11434` (verify Ollama is LISTENing)
- Missing model: rerun `ollama pull gpt-oss:20b`
- Timeouts/connection errors: `brew services restart ollama` or run `ollama serve` manually
- Config precedence: environment variables override `application.yml`

## License/Notes
Do not commit real API keys. Models and generated content are subject to their respective licenses and terms.
