# ExpenseTracker

A desktop personal finance tracker built with Java Swing. Users can add incomes/expenses, categorize transactions, view history, generate reports, manage budgets, import/export CSV, toggle themes, and visualize spending.

## Project Flow (High-Level)
1. User launches the app → `App` sets the Look & Feel and shows `MainFrame`.
2. `Database` ensures the SQL Server database and tables exist, then returns a connection.
3. UI tabs perform actions through DAOs and services:
   - Add → `TransactionDao.insert`
   - History → `TransactionDao.listAll` (+ edit/delete/export)
   - Reports → `ReportService` totals (daily/monthly/yearly)
   - Dashboard → `ReportService.monthlySpendingByCategory` → XChart pie
   - Budgets → `BudgetDao.upsert/find/list`
4. Results render in Swing components; theme and UX enhancements via FlatLaf.

## Architecture
- UI (Swing, tabs)
  - `MainFrame` (menu, tabs, header)
  - `ui/AddPanel`, `ui/HistoryPanel`, `ui/ReportsPanel`, `ui/DashboardPanel`, `ui/BudgetsPanel`, `ui/TransactionDialog`
- Services
  - `service/ReportService` (aggregations and chart data)
  - `service/SeedService` (optional seeding on first run)
- Data Access
  - `dao/TransactionDao`, `dao/CategoryDao`, `dao/BudgetDao`
- Persistence
  - `db/Database` (SQL Server connection + schema init)
- Models
  - `model/Transaction`, `model/Category`, `model/Budget`
- Utilities
  - `util/CsvExporter`

Separation of concerns: UI never accesses JDBC directly; it calls DAOs/services, which use `Database`.

## Tech Stack
- Language: Java 17
- UI: Swing + FlatLaf (IntelliJ themes, extras)
- Charts: XChart
- Database: Microsoft SQL Server via JDBC (mssql-jdbc)
- Build: Maven (fat jar via `maven-assembly-plugin`)

## Features
- Add income/expense with category, date, currency, note
- Editable categories (type to add new); category management via data model
- History: search, sort, refresh, edit, delete, export CSV
- Reports: daily, monthly, yearly totals; refreshable
- Dashboard: monthly category spending pie chart
- Budgets: set monthly limits per category
- Import CSV (menu) and export CSV (history)
- Themes: light/dark toggle; IntelliJ Arc Orange by default

## Database
- Engine: SQL Server (local)
- Connection: `jdbc:sqlserver://localhost:1433;databaseName=ExpenseTracker;encrypt=false`
- Credentials: user `khan`, password `khan321`

Tables (created if missing):
- `categories(id INT IDENTITY PK, name NVARCHAR(100) UNIQUE)`
- `transactions(id INT IDENTITY PK, date DATE, type NVARCHAR(10) CHECK ('Income'/'Expense'), category NVARCHAR(100), amount DECIMAL(18,2), note NVARCHAR(4000), currency NVARCHAR(10) DEFAULT 'USD')`
- `budgets(category NVARCHAR(100) PK, monthly_limit DECIMAL(18,2))`

Indexes (recommended for performance):
- `CREATE INDEX IX_transactions_date ON transactions(date)`
- `CREATE INDEX IX_transactions_category ON transactions(category)`
- `CREATE INDEX IX_transactions_type ON transactions(type)`

## Build & Run
1. Ensure SQL Server is running locally and the login exists:
   - user: `khan`
   - password: `khan321`
   - port: 1433 (default)
2. Build fat jar:
```bash
mvn -DskipTests package assembly:single
```
3. Run:
```bash
java -jar target/ExpenseTracker-1.0.0-jar-with-dependencies.jar
```

If SQL Server is on a different host/instance/port, update `SERVER_URL`, `DATABASE_NAME`, `USER`, `PASSWORD` in `db/Database.java`.

## UI Overview
- Menu → File: Import CSV…, Export CSV…, Exit; View: Toggle Dark Mode; Help: About
- Tabs:
  - Dashboard: monthly spending pie
  - Add: form with amount, type, category (editable), date, currency, note
  - History: table with search, sort, edit/delete/export
  - Reports: daily/monthly/yearly totals
  - Budgets: grid to manage per-category monthly limits

## CSV Format
- Header: `id,date,type,category,amount,currency,note`
- Import ignores `id` and creates new records
- Export writes all current transactions

## Project Layout
- `src/main/java/com/expensetracker/` root
  - `App.java`, `MainFrame.java`
  - `db/`, `dao/`, `model/`, `service/`, `ui/`, `util/`
- `pom.xml` (deps: mssql-jdbc, flatlaf, xchart, assembly plugin)

## Extensibility & Roadmap
- Performance: server-side pagination, parameterized quick filters, additional indexes
- Analytics: 12‑month trends, category bar charts, budget progress bars by month
- Data safety: backup/restore, export/import app bundle
- Internationalization: locale-specific formats, multi-currency conversion in reports
- Recurring transactions: schedule + generator service

## Security Notes
- Credentials are currently configured in code for simplicity; move to environment variables or config file for production.
- Validate and sanitize imported CSV data.

## Troubleshooting
- SQL Server connection errors: verify server is running, port 1433 open, login exists, TCP enabled.
- Missing charts: ensure `xchart` is in the fat jar (use `assembly:single`).
- UI glitches: try toggling theme in View menu; ensure Java 17 is used.
