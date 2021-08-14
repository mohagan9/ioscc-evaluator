package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
import com.automation.iosccevaluator.xcui.SelectorEvaluator.isMatch
import com.automation.iosccevaluator.xcui.SelectorEvaluator.parseFilter
import com.automation.iosccevaluator.xcui.SelectorEvaluator.select
import com.intellij.psi.xml.XmlTag
import com.intellij.util.containers.OrderedSet

class ClassChainEvaluator(private val root: XmlTag?) {
    fun findAllBy(query: String): OrderedSet<XmlTag> {
        if (query[0] == '$')
            return NsPredicateEvaluator(root).findAllBy(query.trim('$'))

        val matches = OrderedSet<XmlTag>()
        if (root == null)
            return matches

        val isDescendantChildQuery = query.startsWith("**/")
        val trimmedQuery = query.removePrefix("**/")
        val rootType = trimmedQuery.substringBefore('[').substringBefore('/')
        val rootFilter = parseFilter(trimmedQuery)
        val directChildQuery = trimmedQuery.substringAfter('/', "")

        if (directChildQuery.isEmpty() && isAttributeMatch("type == \"$rootType\"", root))
            matches += select(rootFilter, listOf(root))

        if (isDescendantChildQuery)
            matches += findMatchingChildren(trimmedQuery, root)
        else if (directChildQuery.isNotEmpty())
            matches += findDirectMatchingChildren(directChildQuery, root)

        return matches
    }

    private fun findDirectMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val type = query.substringBefore('[').substringBefore('/')
        val filter = parseFilter(query)
        val matchingChildren = mutableListOf<XmlTag>()
        val childQuery = query.substringAfter('/', "")
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child: XmlTag ->
                if (isAttributeMatch("type == \"$type\"", child)) {
                    if (childQuery.isEmpty())
                        matchingChildren += child
                    else if (childQuery.startsWith("**/"))
                        matchingChildren += findMatchingChildren(childQuery.removePrefix("**/"), child)
                    else if (isMatch(filter, child)) {
                        matchingChildren += findDirectMatchingChildren(childQuery, child)
                    }
                }
            }
        return if (childQuery.isEmpty())
            select(filter, matchingChildren, false)
        else matchingChildren
    }

    private fun findMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val type = query.substringBefore('[').substringBefore('/')
        val filter = parseFilter(query)
        val filteredChildren = select(filter, parent.children
            .filterIsInstance<XmlTag>()
            .filter { child -> isAttributeMatch("type == \"$type\"", child) }
        )
        val matchingChildren = mutableListOf<XmlTag>()
        val childQuery = query.substringAfter('/', "")
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child ->
                if (child in filteredChildren)
                    if (childQuery.isEmpty())
                        matchingChildren += child
                    else if (childQuery.startsWith("**/"))
                        matchingChildren += findMatchingChildren(childQuery.removePrefix("**/"), child)
                    else
                        matchingChildren += findDirectMatchingChildren(childQuery, parent)

                matchingChildren += findMatchingChildren(query, child)
            }
        return matchingChildren
    }
}
