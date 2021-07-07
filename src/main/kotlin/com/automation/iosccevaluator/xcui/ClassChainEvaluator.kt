package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
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

        matches += if (isDescendantChildQuery)
            findMatchingChildren(trimmedQuery, root)
        else
            findDirectMatchingChildren(trimmedQuery, root)

        if (isAttributeMatch("type == \"$trimmedQuery\"", root))
            matches += root

        return matches
    }

    private fun findDirectMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val matchingChildren = mutableListOf<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child: XmlTag ->
                if (isAttributeMatch("type == \"$query\"", child))
                    matchingChildren += child
                else
                    matchingChildren += findDirectMatchingChildren(query, child)
            }
        return matchingChildren
    }

    private fun findMatchingChildren(query: String, parent: XmlTag): List<XmlTag> {
        val matchingChildren = mutableListOf<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child: XmlTag ->
                matchingChildren += findMatchingChildren(query, child)
            }
        matchingChildren += parent.children
            .filterIsInstance<XmlTag>()
            .filter { child -> isAttributeMatch("type == \"$query\"", child) }
        return matchingChildren
    }
}
