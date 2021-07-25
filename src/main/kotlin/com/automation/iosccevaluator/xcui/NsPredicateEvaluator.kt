package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
import com.intellij.psi.xml.XmlTag
import com.intellij.util.containers.OrderedSet

class NsPredicateEvaluator(private val root: XmlTag?) {
    fun findAllBy(nsPredicate: String): OrderedSet<XmlTag> {
        val matches = OrderedSet<XmlTag>()
        if (root == null)
            return matches

        if (isOrMatch(nsPredicate, root))
            matches += root

        matches += findMatchingChildren(nsPredicate, root)
        return matches
    }

    private fun findMatchingChildren(nsPredicate: String, parent: XmlTag): OrderedSet<XmlTag> {
        val matchingChildren = OrderedSet<XmlTag>()
        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child ->
                if (isOrMatch(nsPredicate, child))
                    matchingChildren += child

                matchingChildren += findMatchingChildren(nsPredicate, child)
            }
        return matchingChildren
    }

    private fun isOrMatch(statement: String, xmlTag: XmlTag): Boolean {
        val splitByOr = statement.split(" or ", ignoreCase = true)
        var isMatch = false
        if (splitByOr.size <= 2) {
            for (orCondition in splitByOr)
                if (isAndMatch(orCondition, xmlTag)) isMatch = true
        } else for (orCondition in splitByOr)
                if (isOrMatch(orCondition, xmlTag)) isMatch = true

        return isMatch
    }

    private fun isAndMatch(statement: String, xmlTag: XmlTag): Boolean {
        val splitByAnd = statement.split(" and ", ignoreCase = true)
        if (splitByAnd.size <= 2) {
            for (andCondition in splitByAnd)
                if (!isAttributeMatch(andCondition, xmlTag)) return false
        } else for (andCondition in splitByAnd)
            if (!isAndMatch(andCondition, xmlTag)) return false

        return true
    }
}
