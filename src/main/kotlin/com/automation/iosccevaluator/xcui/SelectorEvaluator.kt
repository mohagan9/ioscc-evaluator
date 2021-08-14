package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.exceptions.InvalidClassChainExpressionException
import com.intellij.psi.xml.XmlTag

object SelectorEvaluator {
    fun select(filter: String, collection: List<XmlTag>, isPathDescendant: Boolean = true): List<XmlTag> {
        return when {
            filter.isEmpty() -> collection
            filter[0] == '$' -> selectAllByContainsPredicate(
                isPathDescendant, filter.trim('$'), collection
            )
            else -> try {
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
    private fun selectAllByContainsPredicate(
            isPathDescendant: Boolean,
            predicate: String,
            collection: List<XmlTag>
    ): List<XmlTag> {
        val matchingChildren = mutableListOf<XmlTag>()
        collection
            .forEach { parent ->
                val children = parent.children.filterIsInstance<XmlTag>()
                if (children.any {
                        NsPredicateEvaluator(it).findAllBy(predicate).isNotEmpty()
                }) matchingChildren += parent
                if (isPathDescendant)
                    matchingChildren += selectAllByContainsPredicate(isPathDescendant, predicate, children)
            }
        return matchingChildren
    }

    fun parseFilter(query: String): String {
        return query
            .substringBefore('/')
            .substringAfter('[', "")
            .substringBefore(']', "")
    }

    fun isMatch(filter: String, node: XmlTag): Boolean {
        return select(filter, listOf(node)).isNotEmpty()
    }
}
