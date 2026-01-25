## 2024-10-18 - StringBuilder vs Substring for Suffix Checking
**Learning:** Checking domain suffixes by iteratively prepending to a `StringBuilder` (`insert(0)`) is O(N^2) and significantly slower than iterating from the end of the string and using `substring` (or a custom view).
**Action:** Use `lastIndexOf` and `substring` to check suffixes instead of constructing them with `StringBuilder.insert(0)`. Also, avoid splitting strings into lists if you only need to check suffixes.
