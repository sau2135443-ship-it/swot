package swot

import java.io.File

fun isAcademic(email: String): Boolean {
    val domain = normalizeDomain(email)
    if (isStoplisted(domain)) return false
    if (isUnderTLD(domain)) return true

    val parts = domain.split('.').reversed()
    return findSchoolNames(parts).isNotEmpty()
}

fun findSchoolNames(emailOrDomain: String): List<String> {
    return findSchoolNames(domainParts(emailOrDomain))
}

fun isUnderTLD(parts: List<String>): Boolean {
    return checkSet(Resources.tlds, parts)
}

fun isStoplisted(parts: List<String>): Boolean {
    return checkSet(Resources.stoplist, parts)
}

internal fun isUnderTLD(domain: String): Boolean {
    return checkSet(Resources.tlds, domain)
}

internal fun isStoplisted(domain: String): Boolean {
    return checkSet(Resources.stoplist, domain)
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

private fun normalizeDomain(emailOrDomain: String): String {
    return emailOrDomain.trim().lowercase().substringAfter('@').substringAfter("://").substringBefore(':')
}

private fun domainParts(emailOrDomain: String): List<String> {
    return normalizeDomain(emailOrDomain).split('.').reversed()
}

internal fun checkSet(set: Set<String>, parts: List<String>): Boolean {
    val domain = parts.reversed().joinToString(".")
    return checkSet(set, domain)
}

internal fun checkSet(set: Set<String>, domain: String): Boolean {
    var nextDot = domain.lastIndexOf('.')
    while (nextDot != -1) {
        if (set.contains(domain.substring(nextDot + 1))) return true
        nextDot = domain.lastIndexOf('.', nextDot - 1)
    }
    if (set.contains(domain)) return true
    return false
}
