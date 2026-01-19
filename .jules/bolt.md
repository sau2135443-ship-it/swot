## 2024-10-18 - String Splitting Overhead
**Learning:** `String.split()` and list processing are significantly more expensive than string manipulation (substring/lastIndexOf) for simple prefix/suffix checks. In this case, avoiding `split` for TLD and Stoplist checks resulted in a ~15x speedup for valid domains.
**Action:** For hot paths checking prefixes/suffixes, avoid splitting strings into lists if possible. Operate on the string directly.
