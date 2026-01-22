package swot

import java.io.File

fun isAcademic(email: String): Boolean {
    val domain = normalize(email)
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

private fun isUnderTLD(domain: String): Boolean {
    return checkSet(Resources.tlds, domain)
}

fun isStoplisted(parts: List<String>): Boolean {
    return checkSet(Resources.stoplist, parts)
}

private fun isStoplisted(domain: String): Boolean {
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

internal fun checkSet(set: Set<String>, domain: String): Boolean {
    var index = domain.lastIndexOf('.')
    while (index != -1) {
        if (set.contains(domain.substring(index + 1))) return true
        index = domain.lastIndexOf('.', index - 1)
    }
    return set.contains(domain)
}
