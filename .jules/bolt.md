## 2024-10-09 - String splitting vs Substrings
**Learning:** `StringBuilder.insert(0)` is an anti-pattern for building strings inside a loop (O(N^2)). For domain suffix checks, working directly on the normalized string with `lastIndexOf` and `substring` avoids expensive `split` allocations and array copying.
**Action:** When validating hierarchically (like domains), verify if validation can be done on the full string before splitting into parts.
