package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeName
import com.automation.iosccevaluator.xcui.NsPredicateParser.getAttributeValue
import com.intellij.psi.xml.XmlTag

class NsPredicateEvaluator(private val root: XmlTag) {
    fun findAllBy(nsPredicate: String): List<XmlTag> {
        val matches: MutableList<XmlTag> = mutableListOf()

        if (isMatch(nsPredicate, root))
            matches += root

        if (root.children.isNotEmpty())
            matches += root.children
                .map { child -> child as XmlTag }
                .filter { child -> isMatch(nsPredicate, child) }

        return matches
    }

    private fun isMatch(nsPredicate: String, xmlTag: XmlTag): Boolean {
        return xmlTag.attributes.any { attr ->
                attr.name == getAttributeName(nsPredicate) &&
                attr.value == getAttributeValue(nsPredicate)
            }
    }
}
