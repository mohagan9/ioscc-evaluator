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

        if (isMatch(nsPredicate, root))
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
            .filter { child -> isMatch(nsPredicate, child) }

        return matchingChildren
    }

    private fun isMatch(nsPredicate: String, xmlTag: XmlTag): Boolean {
        val splitByAnd = nsPredicate.split(" and ", ignoreCase = true)
        if (splitByAnd.size > 1) {
            for (andCondition in splitByAnd) {
                if (!xmlTag.attributes.any { attr ->
                        attr.name == getAttributeName(andCondition) &&
                        attr.value == getAttributeValue(andCondition)
                    }) return false
            }
            return true
        } else {
            val splitByOr = nsPredicate.split(" or ", ignoreCase = true)
            var isMatch = false
            for(orCondition in splitByOr) {
                if (xmlTag.attributes.any { attr ->
                        attr.name == getAttributeName(orCondition) &&
                        attr.value == getAttributeValue(orCondition)
                    }) isMatch = true
            }
            return isMatch
        }
    }
}
