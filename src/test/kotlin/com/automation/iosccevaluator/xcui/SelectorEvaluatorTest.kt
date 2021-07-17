package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.exceptions.InvalidClassChainExpressionException
import com.automation.iosccevaluator.xcui.SelectorEvaluator.select
import com.intellij.psi.xml.XmlTag
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class SelectorEvaluatorTest {
    @Test
    fun select_givenAnIndexFilter_returnsSingletonListOfMatch() {
        val tag1: XmlTag = mockk()
        val tag2: XmlTag = mockk()
        val tag3: XmlTag = mockk()
        val collection = listOf(tag1, tag2, tag3)

        assertEquals(listOf(tag1), select("1", collection))
        assertEquals(listOf(tag2), select("2", collection))
        assertEquals(listOf(tag3), select("3", collection))
    }

    @Test
    fun select_givenAnOutOfBoundsIndexFilter_returnsEmptyList() {
        val collection = listOf(mockk<XmlTag>())

        assertEquals(listOf<XmlTag>(), select("0", collection))
        assertEquals(listOf<XmlTag>(), select("2", collection))
    }

    @Test
    fun select_givenAnInvalidNumericIndexFilter_throwsInvalidClassChainExpression() {
        assertThrows<InvalidClassChainExpressionException>("'a' is not a valid selector expression.") {
            select("a", listOf(mockk()))
        }
    }

    @Test
    fun select_givenAnEmptyFilter_returnsTheCollection() {
        val collection = listOf(mockk<XmlTag>())
        assertEquals(collection, select("", collection))
    }
}
