package com.automation.iosccevaluator.xcui

import com.intellij.psi.xml.XmlTag

class NsPredicateEvaluator(private val root: XmlTag) {
    fun findBy(nsPredicate: String): Array<XmlTag> {
        return if (root.attributes
                .filter { attr -> attr.name == getAttributeName(nsPredicate) }
                .map { attr -> attr.value }
                .contains(getAttributeValue(nsPredicate)))
            arrayOf(root)
        else
            arrayOf()
    }

    private fun getAttributeName(nsPredicate: String): String {
        val parts = nsPredicate.split(" == ")
        return parts[0]
    }

    private fun getAttributeValue(nsPredicate: String): String {
        val parts = nsPredicate.split(" == ")
        return parts[1].replace("\"", "")
    }
}
