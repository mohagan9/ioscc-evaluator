package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeName
import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeValue
import com.intellij.psi.xml.XmlTag

class NsPredicateEvaluator(private val root: XmlTag) {
    fun findAllBy(nsPredicate: String): List<XmlTag> {
        val matches: MutableList<XmlTag> = mutableListOf()
        matches += findMatchingChildren(nsPredicate, root)

        if (isMatch(nsPredicate, root))
            matches += root

        return matches
    }

    private fun findMatchingChildren(nsPredicate: String, parent: XmlTag): List<XmlTag> {
        val matchingChildren: MutableList<XmlTag> = mutableListOf()

        parent.children
            .map { child -> child as XmlTag }
            .forEach { child: XmlTag ->
                matchingChildren += findMatchingChildren(nsPredicate, child)
            }
        matchingChildren += parent.children
            .map { child -> child as XmlTag }
            .filter { child -> isMatch(nsPredicate, child) }

        return matchingChildren
    }

    private fun isMatch(nsPredicate: String, xmlTag: XmlTag): Boolean {
        return xmlTag.attributes.any { attr ->
                attr.name == getAttributeName(nsPredicate) &&
                attr.value == getAttributeValue(nsPredicate)
            }
    }
}
