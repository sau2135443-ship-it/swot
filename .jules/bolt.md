## 2024-10-18 - String Manipulation vs List Splitting for Domain validation
**Learning:** For verifying if a domain belongs to a set of suffixes (like TLDs or Stoplist), iterating over string indices with `lastIndexOf` is significantly more performant than `split('.').reversed()` because it avoids List allocation and multiple String allocations.
**Action:** Prefer in-place string processing for high-frequency validation checks where structural parsing isn't strictly necessary.
