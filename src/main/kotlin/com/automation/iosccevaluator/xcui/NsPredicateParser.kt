package com.automation.iosccevaluator.xcui

object NsPredicateParser {
    fun getAttributeName(nsPredicate: String): String {
        val parts = nsPredicate.split(" == ")
        return parts[0]
    }

    fun getAttributeValue(nsPredicate: String): String {
        val parts = nsPredicate.split(" == ")
        return parts[1].replace("\"", "")
    }
}
