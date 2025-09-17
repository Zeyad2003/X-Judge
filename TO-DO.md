# X-Judge Roadmap (Phase 1: Scraping Reliability)

This roadmap focuses on making Codeforces scraping correct, resilient, and observable. AtCoder is explicitly out of scope for now.

## Goals
- Deliver stable problem fetching from Codeforces even under intermittent CF outages or minor HTML changes.
- Provide clear operational behavior (logging/metrics), graceful fallbacks, and cache-first semantics.
- Prepare the codebase for standardized exception handling and future feature additions.

## Today’s Status (from code review)
- One-shot fetch: Scraper does a single Jsoup GET with a fixed 15s timeout; no retries/backoff.
- No alternate URLs: Uses only `https://codeforces.com/problemset/problem/{contestId}/{index}`.
- Minimal outage handling: Network/5xx leads to RuntimeException; no diagnostic, no backoff, no fallback.
- Locale: Sets `Accept-Language` but not locale cookie; CF can occasionally return non-English.
- Sample I/O parsing: Converts `<br>` to `\n` and strips tags; may lose exact spacing for some samples.
- Time/Memory parsing: Converts to ms/kb strings; unit normalization might be inconsistent with typical MB notation.
- Caching: If a problem exists in DB, it’s returned (good). If CF is down and the problem is new, request fails (no graceful fallback).
- No rate limiting or jitter: Risk of burst traffic and CF throttling.

## Deliverables (Phase 1)
1. Codeforces reliability improvements
    - Add exponential backoff + jitter retry for transient failures (timeouts, 5xx, DNS, connection reset).
    - Try alternative endpoints on failure:
        - `/problemset/problem/{contestId}/{index}`
        - `/contest/{contestId}/problem/{index}`
        - Try `https://` then fallback to `http://` if SSL layer causes issues.
    - Force English: Add `Cookie: X-User-Locale=en` (and keep `Accept-Language: en`).
    - Detect CF outage pages ("temporarily unavailable") and treat as retryable.
    - Improve logging with attempt counters, URL, status code, and terminal failure reason.

2. Graceful behavior when CF is down
    - If cached problem exists: serve it. Already implemented.
    - If not cached: after final retry, return a domain-specific error (to be mapped by controller later) stating the service is temporarily unavailable and suggest retry. For now, still throw but with actionable message.

3. Parsing improvements (scoped)
    - Keep sections as HTML (already done) but ensure selectors have sensible fallbacks.
    - Sample I/O: Preserve whitespace more faithfully, including multiple spaces and non-breaking spaces. Avoid over-stripping tags.
    - Normalize limits into consistent units (e.g., ms and MB) and keep the original raw string in metadata for reference.

4. Observability (scoped)
    - Structured logs for each fetch attempt (attempt, backoff, exception class, root cause).
    - Summaries on success (sections/properties/samples counts) already in service; keep that.

5. Configurability
    - Extract retry parameters to constants for now; next step expose via Spring config:
        - maxAttempts (default 5)
        - baseBackoffMs (default 500)
        - maxBackoffMs (default 4000)
        - timeoutMs (default 15000)

## Next Phases (after reliability)
- Centralized exception handling with mapped error responses (Spring @ControllerAdvice).
- Problem discovery/search endpoints (by tags, difficulty, source).
- Add missing endpoints to trigger scrape-by-code and search/browse stored problems.
- Documentation: API usage, examples, error semantics, and operational tips.
- Testing: unit tests for parsing utilities, integration tests with mocked HTML, and contract tests for controller/service.

## Concrete Tasks Checklist
- [ ] Add retry with exponential backoff + jitter and URL fallbacks in Codeforces scraper.
- [ ] Add CF locale cookie and improve outage detection.
- [ ] Improve failure messages for better UX and observability.
- [ ] Refine sample I/O extraction to preserve whitespace.
- [ ] Normalize time/memory units consistently; keep raw as metadata.
- [ ] Add configuration hooks for retry/timeouts (properties).
- [ ] Add basic unit tests for parsing helpers and retry/backoff behavior.
- [ ] Document behavior and failure modes in README/Swagger descriptions.

## Outage Handling Policy (draft)
- If CF returns 5xx, known outage text, TLS handshake failure, or DNS issues: treat as transient; retry with backoff and then abort with a 503-like domain error.
- If CF returns 404 for all endpoints: treat as permanent (likely invalid code) and return a 404-like domain error.
- If rate-limited or blocked (403/429): backoff with longer jitter, cap attempts, and return a 503-like domain error.

## Notes
- Official CF API doesn’t provide statements; scraping is unavoidable. Caching is the primary resilience layer. Keep DB canonical and avoid re-scraping existing rows.
- Respectful scraping: keep modest timeouts and backoffs, randomize user-agent, and avoid bursts.

