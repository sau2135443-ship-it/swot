package swot

import java.io.File

fun isAcademic(email: String): Boolean {
    val domain = normalize(email)
    // Optimized path: check stoplist and TLDs using raw string to avoid list allocation
    if (isStoplisted(domain)) return false
    if (isUnderTLD(domain)) return true
    // Fallback: split and check file system
    return findSchoolNames(domain.split('.').reversed()).isNotEmpty()
}

fun findSchoolNames(emailOrDomain: String): List<String> {
    return findSchoolNames(domainParts(emailOrDomain))
}

fun isUnderTLD(parts: List<String>): Boolean {
    return checkSet(Resources.tlds, parts)
}

internal fun isUnderTLD(domain: String): Boolean {
    return checkSuffixes(Resources.tlds, domain)
}

fun isStoplisted(parts: List<String>): Boolean {
    return checkSet(Resources.stoplist, parts)
}

internal fun isStoplisted(domain: String): Boolean {
    return checkSuffixes(Resources.stoplist, domain)
}

private object Resources {
    val tlds = readList("/tlds.txt") ?: error("Cannot find /tlds.txt")
    val stoplist = readList("/stoplist.txt") ?: error("Cannot find /stoplist.txt")

    fun readList(resource: String) : Set<String>? {
        return File("lib/domains/$resource").takeIf { it.exists() }?.bufferedReader()?.lineSequence()?.toHashSet()
    }
}

private fun findSchoolNames(parts: List<String>): List<String> {
    val resourcePath = StringBuilder("")
    for (part in parts) {
        resourcePath.append('/').append(part)
        val school = Resources.readList("${resourcePath}.txt")
        if (school != null) {
            return school.toList()
        }
    }

    return arrayListOf()
}

private fun normalize(emailOrDomain: String): String {
    return emailOrDomain.trim().lowercase().substringAfter('@').substringAfter("://").substringBefore(':')
}

private fun domainParts(emailOrDomain: String): List<String> {
    return normalize(emailOrDomain).split('.').reversed()
}

internal fun checkSet(set: Set<String>, parts: List<String>): Boolean {
    val subj = StringBuilder()
    for (part in parts) {
        subj.insert(0, part)
        if (set.contains(subj.toString())) return true
        subj.insert(0 ,'.')
    }
    return false
}

// Efficiently check if any suffix of the domain exists in the set
// Iterates from shortest suffix (after last dot) to full domain
private fun checkSuffixes(set: Set<String>, domain: String): Boolean {
    var nextDot = domain.lastIndexOf('.')
    while (nextDot != -1) {
        if (set.contains(domain.substring(nextDot + 1))) return true
        nextDot = domain.lastIndexOf('.', nextDot - 1)
    }
    return set.contains(domain)
}
