package com.automation.iosccevaluator.xcui

object NsPredicateParser {
    private const val EQUALS_DELIMITER = " == \""

    fun getAttributeName(nsPredicate: String): String {
        return nsPredicate.split(EQUALS_DELIMITER)[0]
    }

    fun getAttributeValue(nsPredicate: String): String {
        return nsPredicate
            .split(EQUALS_DELIMITER, ignoreCase = false, limit = 2)[1]
            .dropLast(1)
    }
}
