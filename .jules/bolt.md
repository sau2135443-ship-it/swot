## 2024-05-23 - Avoid Premature splitting in Domain Validation
**Learning:** In `isAcademic`, we were eagerly splitting the domain into parts (`split('.').reversed()`) before checking if it was stoplisted or a known TLD. This caused unnecessary List and String allocations for every check.
**Action:** Normalize the string first, then perform stoplist and TLD checks on the string directly using suffix matching (`lastIndexOf`). Only split if necessary (for `findSchoolNames`).
