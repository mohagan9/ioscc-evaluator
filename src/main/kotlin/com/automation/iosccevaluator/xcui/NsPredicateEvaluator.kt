package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeName
import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeValue
import com.intellij.psi.xml.XmlTag

class NsPredicateEvaluator(private val root: XmlTag?) {
    fun findAllBy(nsPredicate: String): List<XmlTag> {
        if (root == null)
            return mutableListOf()

        val matches: MutableList<XmlTag> = mutableListOf()
        matches += findMatchingChildren(nsPredicate, root)

        if (isOrMatch(nsPredicate, root))
            matches += root

        return matches
    }

    private fun findMatchingChildren(nsPredicate: String, parent: XmlTag): List<XmlTag> {
        val matchingChildren: MutableList<XmlTag> = mutableListOf()

        parent.children
            .filterIsInstance<XmlTag>()
            .forEach { child: XmlTag ->
                matchingChildren += findMatchingChildren(nsPredicate, child)
            }
        matchingChildren += parent.children
            .filterIsInstance<XmlTag>()
            .filter { child -> isOrMatch(nsPredicate, child) }

        return matchingChildren
    }

    private fun isOrMatch(statement: String, xmlTag: XmlTag): Boolean {
        val splitByOr = statement.split(" or ", ignoreCase = true)
        var isMatch = false
        if (splitByOr.size < 3) {
            for (orCondition in splitByOr)
                if (isAndMatch(orCondition, xmlTag)) isMatch = true

        } else for (orCondition in splitByOr)
                if (isOrMatch(orCondition, xmlTag)) isMatch = true

        return isMatch
    }

    private fun isAndMatch(statement: String, xmlTag: XmlTag): Boolean {
        val splitByAnd = statement.split(" and ", ignoreCase = true)
        if (splitByAnd.size < 3) {
            for (andCondition in splitByAnd)
                if (!isAttributeMatch(andCondition, xmlTag)) return false

            return true
        }
        for (andCondition in splitByAnd)
         if (!isAndMatch(andCondition, xmlTag)) return false

        return true
    }

    private fun isAttributeMatch(condition: String, xmlTag: XmlTag): Boolean {
        return xmlTag.attributes.any { attr ->
            attr.name == getAttributeName(condition) &&
            attr.value == getAttributeValue(condition)
        }
    }
}
