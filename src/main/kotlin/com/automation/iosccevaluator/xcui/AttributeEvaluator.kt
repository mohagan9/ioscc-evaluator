package com.automation.iosccevaluator.xcui

import com.intellij.psi.xml.XmlTag

object AttributeEvaluator {
    private const val EQUALS_DELIMITER = " == \""
    private const val NOT_EQUALS_DELIMITER = " != \""

    fun isAttributeMatch(condition: String, xmlTag: XmlTag): Boolean {
        if (condition.contains(EQUALS_DELIMITER))
            return xmlTag.attributes.any { attr ->
                attr.name == getAttributeName(condition, EQUALS_DELIMITER) &&
                attr.value == getAttributeValue(condition, EQUALS_DELIMITER)
            }

        return xmlTag.attributes.any { attr ->
            attr.name == getAttributeName(condition, NOT_EQUALS_DELIMITER) &&
            attr.value != getAttributeValue(condition, NOT_EQUALS_DELIMITER)
        }
    }

    private fun getAttributeName(condition: String, delimiter: String): String {
        return condition.split(delimiter)[0]
    }

    private fun getAttributeValue(condition: String, delimiter: String): String {
        return condition
            .split(delimiter, ignoreCase = false, limit = 2)[1]
            .dropLast(1)
    }
}
