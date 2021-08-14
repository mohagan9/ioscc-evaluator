package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.exceptions.InvalidClassChainExpressionException
import com.automation.iosccevaluator.xcui.SelectorEvaluator.isMatch
import com.automation.iosccevaluator.xcui.SelectorEvaluator.parseFilter
import com.automation.iosccevaluator.xcui.SelectorEvaluator.select
import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlAttributeMock
import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlTagMock
import com.intellij.psi.xml.XmlTag
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
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

    @Test
    fun parseFilter_givenAStringWithValidSelector_returnsTheFilter() {
        val filter = "FILTER"
        assertEquals(filter, parseFilter("ELEMENT[$filter]"))
    }

    @Test
    fun parseFilter_givenAStringWithInvalidSelector_returnsEmptyString() {
        val filter = "FILTER"
        assertEquals("", parseFilter("ELEMENT[$filter"))
        assertEquals("", parseFilter("ELEMENT$filter]"))
        assertEquals("", parseFilter(filter))
    }

    @Test
    fun parseFilter_givenAStringWithSlashBeforeBracket_returnsEmptyString() {
        val filter = "FILTER"
        assertEquals("", parseFilter("ROOT/ELEMENT[$filter]"))
    }

    @Test
    fun select_givenAPredicateFilter_returnsListOfMatchingParents() {
        val filter = "\$color == \"GREEN\"\$"
        val parent = mockk<XmlTag>()
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "GREEN")
        ))
        val child2 = mockk<XmlTag>()
        val nestedChild2a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "GREEN")
        ))
        every { parent.attributes } returns arrayOf()
        every { child2.attributes } returns arrayOf()
        every { child2.children } returns arrayOf(nestedChild2a, nestedChild2b)
        every { parent.children } returns arrayOf(child1, child2)

        assertEquals(listOf(parent, child2), select(filter, listOf(parent)))
    }

    @Test
    fun select_givenNoMatchOnAPredicateFilter_returnsAnEmptyList() {
        val filter = "\$color == \"BLUE\"\$"
        val parent = mockk<XmlTag>()
        val child = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "RED")
        ))
        every { parent.attributes } returns arrayOf()
        every { parent.children } returns arrayOf(child)

        assertEquals(listOf<XmlTag>(), select(filter, listOf(parent)))
    }

    @Test
    fun select_givenPredicateFilterWithNoMatchingType_returnsParentRegardless() {
        val type = "TYPE"
        val parent = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", type)
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NON_MATCHING_TYPE"),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NON_MATCHING_TYPE"),
            createXmlAttributeMock("color", "GREEN")
        ))
        every { parent.children } returns arrayOf(nestedChild1a, nestedChild1b)
        assertEquals(
            listOf(parent),
            select("\$color == \"GREEN\"\$", listOf(parent))
        )
    }

    @Test
    fun select_givenDirectPathPredicateFilterAndParentWithMatchingNestedChildren_onlyReturnsTheParent() {
        val nestedType = "NESTED_TYPE"
        val parent = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "ROOT")
        ))
        val child = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildi = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildii = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChildi3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChildi4a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChildi4b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "GREEN")
        ))
        every { parent.children } returns arrayOf(child)
        every { child.children } returns arrayOf(nestedChildi)
        every { nestedChildi.children } returns arrayOf(nestedChildii)
        every { nestedChildii.children } returns arrayOf(nestedChildi3)
        every { nestedChildi3.children } returns arrayOf(nestedChildi4a, nestedChildi4b)

        assertEquals(
            listOf(parent),
            select("\$color == \"GREEN\"\$", listOf(parent), false)
        )
    }

    @Test
    fun isMatch_givenNoMatch_returnsFalse() {
        val filter = "\$color == \"BLUE\"\$"
        val parent = mockk<XmlTag>()
        val child = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "RED")
        ))
        every { parent.attributes } returns arrayOf()
        every { parent.children } returns arrayOf(child)

        assertFalse(isMatch(filter, parent))
    }

    @Test
    fun isMatch_givenMatch_returnsTrue() {
        val filter = "\$color == \"BLUE\"\$"
        val parent = mockk<XmlTag>()
        val child = createXmlTagMock(arrayOf(
            createXmlAttributeMock("color", "BLUE")
        ))
        every { parent.attributes } returns arrayOf()
        every { parent.children } returns arrayOf(child)

        assertTrue(isMatch(filter, parent))
    }
}
