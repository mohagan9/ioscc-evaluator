package com.automation.iosccevaluator.xcui

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class NsPredicateEvaluatorTest {
    private lateinit var evaluator: NsPredicateEvaluator
    private val root: XmlTag = mockk()

    @BeforeEach
    fun setup() {
        evaluator = NsPredicateEvaluator(root)
        every { root.attributes } returns arrayOf()
        every { root.children } returns arrayOf()
    }

    private fun createXmlAttributeMock(name: String, value: String): XmlAttribute {
        val attr:XmlAttribute = mockk()
        every { attr.name } returns name
        every { attr.value } returns value
        return attr
    }

    private fun createXmlTagMock(attributes: Array<XmlAttribute>): XmlTag {
        val xmlTag: XmlTag = mockk()
        every { xmlTag.attributes } returns attributes
        return xmlTag
    }

    @Test
    fun findBy_givenRootWithMatchingAttributeNameValue_returnsRoot() {
        val attr = createXmlAttributeMock("type", "value")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(root), (evaluator.findAllBy("type == \"value\"")))
    }

    @Test
    fun findBy_givenRootWithNoMatchingAttributeValue_returnsEmpty() {
        val attr = createXmlAttributeMock("type", "no-match")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(), (evaluator.findAllBy("type == \"value\"")))
    }

    @Test
    fun findBy_givenRootWithNoMatchingAttributeName_returnsEmpty() {
        val attr = createXmlAttributeMock("no-match", "value")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(), (evaluator.findAllBy("type == \"value\"")))
    }

    @Test
    fun findBy_givenRootWithTwoPredicateMatchingNestedXmlTags_returnsBothXmlTags() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val children = arrayOf(
            createXmlTagMock(attributes),
            createXmlTagMock(attributes)
        )
        every { root.children } returns children

        assertEquals(children.toList(), (evaluator.findAllBy("type == \"value\"")))
    }

    @Test
    fun findBy_givenAllXmlTagsMatchPredicate_returnsAllXmlTags() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val children = arrayOf(
            createXmlTagMock(attributes),
            createXmlTagMock(attributes)
        )
        every { root.attributes } returns attributes
        every { root.children } returns children

        assertEquals(listOf(root, *children), (evaluator.findAllBy("type == \"value\"")))
    }
}