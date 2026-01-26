## 2024-10-24 - String Optimization in Domain Checking
**Learning:** `StringBuilder.insert(0, ...)` is O(N^2) and significantly slower than iterating with `lastIndexOf` and `substring` for suffix checks. Also, delaying `split` operations until necessary avoids allocations in hot paths.
**Action:** Prefer direct string iteration/slicing for suffix/prefix checks over splitting or building modified strings, especially in loops or hot paths.
