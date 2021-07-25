package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
import com.automation.iosccevaluator.xcui.SelectorEvaluator.parseFilter
import com.automation.iosccevaluator.xcui.SelectorEvaluator.select
import com.intellij.psi.xml.XmlTag
import com.intellij.util.containers.OrderedSet

class ClassChainEvaluator(private val root: XmlTag?) {
    private val descendantSelector = "**/"

    fun findAllBy(query: String): OrderedSet<XmlTag> {
        if (query[0] == '$')
            return NsPredicateEvaluator(root).findAllBy(query.trim('$'))

        val matches = OrderedSet<XmlTag>()
        if (root == null)
            return matches

        val isDescendantChildQuery = query.subSequence(0, descendantSelector.length) == descendantSelector
        val trimmedQuery = query.removePrefix(descendantSelector)
        val type = trimmedQuery.substringBefore('[')
        val filter = parseFilter(trimmedQuery)

        if (isAttributeMatch("type == \"$type\"", root))
            matches += select(filter, listOf(root))

        matches += if (isDescendantChildQuery)
            findMatchingChildren(trimmedQuery, root)
        else
            findDirectMatchingChildren(trimmedQuery, root)

        return matches
    }

    private fun findDirectMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val type = query.substringBefore('[')
        val filter = parseFilter(query)
        val matchingChildren = mutableListOf<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child: XmlTag ->
                if (isAttributeMatch("type == \"$type\"", child))
                    matchingChildren += child
            }
        return select(filter, matchingChildren)
    }

    private fun findMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val type = query.substringBefore('[')
        val filter = parseFilter(query)
        val filteredChildren = select(filter, parent.children
            .filterIsInstance<XmlTag>()
            .filter { child -> isAttributeMatch("type == \"$type\"", child) }
        )
        val matchingChildren = mutableListOf<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child ->
                if (child in filteredChildren)
                    matchingChildren += child
                matchingChildren += findMatchingChildren(query, child)
            }
        return matchingChildren
    }
}
