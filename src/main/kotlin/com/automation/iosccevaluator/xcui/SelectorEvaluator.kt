package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.exceptions.InvalidClassChainExpressionException
import com.intellij.psi.xml.XmlTag

object SelectorEvaluator {
    fun select(filter: String, collection: List<XmlTag>): List<XmlTag> {
        return if (filter.isEmpty())
            collection
        else try {
            val index = filter.toInt() - 1
            if (index in collection.indices)
                listOf(collection[index])
            else
                listOf()
        } catch (e: NumberFormatException) {
            throw InvalidClassChainExpressionException("'$filter' is not a valid selector expression.")
        }
    }
}
