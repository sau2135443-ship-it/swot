## 2024-10-24 - Domain Validation String Allocation
**Learning:** `StringBuilder.insert(0, ...)` inside a loop is O(N^2) and costly for string construction. In high-frequency domain validation, `split` and List creation dominate CPU usage when disk I/O is cached.
**Action:** Use `lastIndexOf` and `substring` (or `regionMatches` if possible) to validate suffixes on the raw string, avoiding intermediate object allocations entirely.
