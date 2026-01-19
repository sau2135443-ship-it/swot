package swot

import java.io.File

fun isAcademic(email: String): Boolean {
    val domain = normalizeDomain(email)
    if (checkSet(Resources.stoplist, domain)) return false
    if (checkSet(Resources.tlds, domain)) return true

    val parts = domain.split('.').reversed()
    return findSchoolNames(parts).isNotEmpty()
}

fun findSchoolNames(emailOrDomain: String): List<String> {
    return findSchoolNames(domainParts(emailOrDomain))
}

private fun normalizeDomain(emailOrDomain: String): String {
    return emailOrDomain.trim().lowercase().substringAfter('@').substringAfter("://").substringBefore(':')
}

fun isUnderTLD(parts: List<String>): Boolean {
    return checkSet(Resources.tlds, parts)
}

fun isStoplisted(parts: List<String>): Boolean {
    return checkSet(Resources.stoplist, parts)
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

private fun domainParts(emailOrDomain: String): List<String> {
    return normalizeDomain(emailOrDomain).split('.').reversed()
}

internal fun checkSet(set: Set<String>, domain: String): Boolean {
    var searchEnd = domain.length
    while (true) {
        val lastDot = domain.lastIndexOf('.', searchEnd - 1)
        val suffix = if (lastDot == -1) domain else domain.substring(lastDot + 1)
        if (set.contains(suffix)) return true
        if (lastDot == -1) break
        searchEnd = lastDot
    }
    return false
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
