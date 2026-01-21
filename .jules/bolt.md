## 2026-01-21 - StringBuilder.insert(0) Performance
**Learning:** `StringBuilder.insert(0, str)` is significantly slower (~30-50%) than simple string concatenation (`str + current`) when building strings in reverse order (prepending), because `insert(0)` requires shifting the entire existing character array twice per iteration (once for content, once for separator). Concatenation only copies data once into a new buffer.
**Action:** Use string concatenation for simple prepend operations or reconstruct the string logic to append instead.

## 2026-01-21 - Resource Leaks in Kotlin Streams
**Learning:** `bufferedReader().lineSequence()` does NOT close the underlying reader when the sequence is exhausted. This leads to file handle leaks in hot paths (like checking thousands of domains).
**Action:** Always use `File.useLines { ... }` which guarantees closure.
