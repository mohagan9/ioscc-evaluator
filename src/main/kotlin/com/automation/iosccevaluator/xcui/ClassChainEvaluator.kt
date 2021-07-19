package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
import com.automation.iosccevaluator.xcui.SelectorEvaluator.select
import com.intellij.psi.xml.XmlTag

class ClassChainEvaluator(private val root: XmlTag?) {
    private val descendantSelector = "**/"

    fun findAllBy(query: String): List<XmlTag> {
        if (query[0] == '$')
            return NsPredicateEvaluator(root).findAllBy(query.trim('$'))

        val matches = mutableListOf<XmlTag>()
        if (root == null)
            return matches

        val isDescendantChildQuery = query.subSequence(0, descendantSelector.length) == descendantSelector
        val trimmedQuery = query.removePrefix(descendantSelector)

        if (isAttributeMatch("type == \"$trimmedQuery\"", root))
            matches += root

        matches += if (isDescendantChildQuery)
            findMatchingChildren(trimmedQuery, root)
        else
            findDirectMatchingChildren(trimmedQuery, root)

        return matches
    }

    private fun findDirectMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val type = query.substringBefore('[')
        val filter = query.substringAfter('[', "").substringBefore(']', "")
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
        val matchingChildren = mutableListOf<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child ->
                if (isAttributeMatch("type == \"$query\"", child))
                    matchingChildren += child
                matchingChildren += findMatchingChildren(query, child)
            }
        return matchingChildren
    }
}
