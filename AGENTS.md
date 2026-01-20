# Repository Guidelines

## Project Structure & Module Organization
- `src/app/` contains the entry point (`App.java`) and demo data loader.
- `src/domain/` holds core business logic: `model/`, `policy/`, and `service/`.
- `src/presentation/` contains the console menu and UI flow.
- `src/infrastructure/csv/` provides CSV parsing factories.
- `src/resources/data/` stores demo CSV files for books, members, etc.
- `test/` includes lightweight test classes with `main` methods.

## Build, Test, and Development Commands
- Compile the app:
  `javac -d out $(find src -name "*.java")`
- Run the console app:
  `java -cp out app.App`
- Compile sources + tests:
  `javac -d out $(find src test -name "*.java")`
- Run an individual test class:
  `java -cp out LoanTest`

## Coding Style & Naming Conventions
- Indentation: 4 spaces; braces on the same line as declarations.
- Classes and enums: `PascalCase` (e.g., `StandardFinePolicy`).
- Methods and fields: `camelCase` (e.g., `calculateFine`).
- Packages are lowercase and layered by responsibility (`domain`, `presentation`, `infrastructure`).
- No formatter or linter is configured; follow existing file style.

## Testing Guidelines
- Tests are plain Java classes in `test/` with a `main` entry point.
- Name tests `*Test.java` and print clear PASS/FAIL messages.
- Focus coverage on business rules (loan status, fine policy, validation rules).

## Commit & Pull Request Guidelines
- Commit messages are short, imperative statements (e.g., "Fix error in Loan").
- Keep commits scoped to a single change when possible.
- PRs should include: a concise summary, how to run (commands), and any data changes.
- Screenshots are not required for console output unless illustrating a UI change.

## Data & Configuration Notes
- Demo data lives in `src/resources/data/`; keep CSV headers stable.
- If you add new fields, update CSV factories and demo loader together.
