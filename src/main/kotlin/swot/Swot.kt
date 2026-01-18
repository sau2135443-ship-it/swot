package swot

import java.io.File

fun isAcademic(email: String): Boolean {
    val parts = domainParts(email)
    return !isStoplisted(parts) && (isUnderTLD(parts) || findSchoolNames(parts).isNotEmpty())
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

private object Resources {
    private val NULL_MARKER = Any()
    private val cache = java.util.concurrent.ConcurrentHashMap<String, Any>()

    val tlds = readList("/tlds.txt") ?: error("Cannot find /tlds.txt")
    val stoplist = readList("/stoplist.txt") ?: error("Cannot find /stoplist.txt")

    fun readList(resource: String) : Set<String>? {
        val cached = cache[resource]
        if (cached != null) {
            @Suppress("UNCHECKED_CAST")
            return if (cached === NULL_MARKER) null else cached as Set<String>
        }

        val result = File("lib/domains/$resource").takeIf { it.exists() }?.bufferedReader()?.lineSequence()?.toHashSet()
        cache[resource] = result ?: NULL_MARKER
        return result
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
    return emailOrDomain.trim().lowercase().substringAfter('@').substringAfter("://").substringBefore(':').split('.').reversed()
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
