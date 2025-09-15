# Repository Guidelines

## Project Structure & Module Organization
- `pom.xml`: Maven build and dependency management (Java 21, Spring Boot 3.5.x).
- `src/main/java`: Application code (create packages under `dev.sobue.springaidemo`).
- `src/main/resources`: Config and assets, e.g., `application.yml`.
- `src/test/java`: Unit/integration tests (JUnit 5).
- `target/`: Build outputs (do not commit).

## Build, Test, and Development Commands
- Build jar: `mvn -q clean package`
- Run app (dev profile): `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
- Run tests: `mvn -q test`
- Run packaged jar: `java -jar target/spring-ai-demo-0.0.1-SNAPSHOT.jar`

The app includes Spring Shell; interactive mode is enabled in `application.yml`.

## Coding Style & Naming Conventions
- Language: Java 21. Indentation: 4 spaces; UTF-8; aim for ≤120 columns.
- Packages: `dev.sobue.springaidemo[.feature]`.
- Classes: `PascalCase`; methods/fields: `camelCase`; constants: `UPPER_SNAKE_CASE`.
- Prefer constructor injection; use Lombok (`@RequiredArgsConstructor`, `@Slf4j`) to reduce boilerplate.
- Keep controllers/services small and focused; avoid static state.

## Testing Guidelines
- Framework: JUnit Jupiter with Spring Boot Test.
- Place tests under `src/test/java` mirroring the main package.
- Naming: `ClassNameTests` (e.g., `ChatServiceTests`).
- Typical annotations: `@SpringBootTest` for integration, `@Test` for units.
- Run locally with `mvn test`; add tests for new behavior and bug fixes.

## Commit & Pull Request Guidelines
- Commits: imperative mood, concise subject; examples: `feat: add chat endpoint`, `fix: handle empty prompt`.
- Branches: `feature/...`, `fix/...`, `chore/...` as appropriate.
- PRs: clear description of changes, linked issues (e.g., `Closes #12`), steps to verify, and sample logs/output for CLI or HTTP.

## Security & Configuration Tips
- Do not commit real API keys. Default config targets local Ollama via `SPRING_AI_OPENAI_BASE_URL=http://localhost:11434`.
- Override via env vars: `SPRING_AI_OPENAI_API_KEY`, `SPRING_AI_OPENAI_BASE_URL`, or edit `src/main/resources/application.yml`.
- Use the `dev` profile for local work; keep secrets in your environment or a non-committed `application-local.yml`.

## Architecture Overview
- Spring Boot app with REST and Spring Shell interfaces.
- Uses Spring AI (OpenAI-compatible) with optional JDBC chat memory.
