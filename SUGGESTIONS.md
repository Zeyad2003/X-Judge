# SUGGESTIONS

- [P0] Decouple submission from fetching:
    - Use ObjectProvider for submission beans in ProblemServiceImp to avoid startup dependency on submission.
    - Guard submit-only paths; keep fetching paths free of submission calls.

- [P0] Disable submission layer during fetching phase:
    - Add features.submission.enabled=false and annotate submission-related beans (pools, strategies, configs) with @ConditionalOnProperty.
    - Keep this off until submission is ready.

- [P0] Global lazy initialization while fetching:
    - spring.main.lazy-initialization=true to prevent eager creation of heavy beans (Selenium, strategies).

- [P1] Harden Selenium setup:
    - Use WebDriverManager to provision drivers.
    - Headless mode in CI; avoid setting Chrome binary unless configured.
    - Create WebDrivers on-demand (no @PostConstruct driver creation).

- [P1] Prefer HTTP + HTML parsing for problem fetching:
    - Use WebClient/HttpClient + Jsoup instead of Selenium unless JS-rendered content or auth is strictly required.

- [P1] Add Resilience4j for external calls:
    - Retries with backoff, timeouts, circuit breakers on HTTP fetches and Selenium interactions.

- [P1] Normalize OnlineJudgeType parsing:
    - Convert incoming source to upper-case and validate.
    - Map aliases (e.g., cf, codeforces) if needed.

- [P2] Improve repository API:
    - Replace findByOnlineJudgeContaining with equality-based method for enums.
    - In filters, convert string to enum in service and use p.onlineJudge = :enum instead of CAST.

- [P2] Split modules or profiles:
    - Separate submission into its own module or Spring profile to physically isolate it from fetching.

- [P2] Background fetching:
    - Use @Async or a queue to scrape missing problems asynchronously and return a “fetching in progress” status.

- [P3] Caching:
    - Cache fetched problems in DB with lastUpdated and TTL; avoid repeated scraping.

- [P3] Observability:
    - Add structured logs and metrics (Micrometer) around scraping and submission flows.

- [P3] SSR best practices:
    - Sanitize problem HTML blocks before rendering.
    - Serve minified CSS/JS; use cache headers and ETags for static assets.
