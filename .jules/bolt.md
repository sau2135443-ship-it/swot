## 2024-10-18 - StringBuilder.insert(0) Performance
**Learning:** `StringBuilder.insert(0)` is O(N) due to array shifting, making it O(N^2) in a loop. For reverse string construction, simple string concatenation (or `append` + `reverse`) can be significantly faster in Kotlin JVM, especially for small strings.
**Action:** Avoid `StringBuilder.insert(0)` in loops. Use string concatenation or build forward and reverse.
