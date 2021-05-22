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
        every { xmlTag.children } returns arrayOf()
        return xmlTag
    }

    @Test
    fun findAllBy_givenRootWithMatchingAttributeNameValue_returnsRoot() {
        val attr = createXmlAttributeMock("type", "value")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(root), evaluator.findAllBy("type == \"value\""))
    }

    @Test
    fun findAllBy_givenRootWithNoMatchingAttributeValue_returnsEmpty() {
        val attr = createXmlAttributeMock("type", "no-match")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(), evaluator.findAllBy("type == \"value\""))
    }

    @Test
    fun findAllBy_givenRootWithNoMatchingAttributeName_returnsEmpty() {
        val attr = createXmlAttributeMock("no-match", "value")
        every { root.attributes } returns arrayOf(attr)

        assertEquals(listOf(), evaluator.findAllBy("type == \"value\""))
    }

    @Test
    fun findAllBy_givenRootWithTwoPredicateMatchingNestedXmlTags_returnsBothXmlTags() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val child: XmlTag = createXmlTagMock(attributes)
        val children = arrayOf(child, child)
        every { root.children } returns children

        assertEquals(children.toList(), evaluator.findAllBy("type == \"value\""))
    }

    @Test
    fun findAllBy_givenAllXmlTagsMatchPredicate_returnsAllXmlTags() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val child: XmlTag = createXmlTagMock(attributes)
        val children = arrayOf(child, child)
        every { root.attributes } returns attributes
        every { root.children } returns children

        assertEquals(listOf(*children, root), evaluator.findAllBy("type == \"value\""))
    }
    
    @Test
    fun findAllBy_givenSinglePredicateMatchInNestedChildren_returnsOneXmlTag() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val childA = createXmlTagMock(arrayOf())
        val childB = createXmlTagMock(arrayOf())
        val leaf = createXmlTagMock(arrayOf())
        val matchingElement = createXmlTagMock(attributes)
        every { childA.children } returns arrayOf(leaf, leaf)
        every { childB.children } returns arrayOf(leaf, matchingElement)
        every { root.children } returns arrayOf(childA, childB)

        assertEquals(listOf(matchingElement), evaluator.findAllBy("type == \"value\""))
    }

    @Test
    fun findAllBy_givenAllXmlTagsMatchPredicateForDeeplyNestedTree_returnsAllXmlTags() {
        val attributes = arrayOf(createXmlAttributeMock("type", "value"))
        val childLevel1 = createXmlTagMock(attributes)
        val childLevel2 = createXmlTagMock(attributes)
        every { root.attributes } returns attributes
        every { root.children } returns arrayOf(childLevel1, childLevel1, childLevel1)
        every { childLevel1.children } returns arrayOf(childLevel2, childLevel2, childLevel2, childLevel2)

        assertEquals(
            listOf(
                *childLevel1.children, *childLevel1.children, *childLevel1.children,
                *root.children, root
            ),
            evaluator.findAllBy("type == \"value\"")
        )
    }

    @Test
    fun findAllBy_givenPredicateWithStringValueContainingPredicate_returnsCorrectXmlTags() {
        val child: XmlTag = createXmlTagMock(
            arrayOf(createXmlAttributeMock("type", "name == \"test\""))
        )
        every { root.attributes } returns arrayOf(createXmlAttributeMock("name", "test"))
        every { root.children } returns arrayOf(child)

        assertEquals(listOf(child), evaluator.findAllBy("type == \"name == \"test\"\""))
    }

    @Test
    fun findAllBy_givenPredicateWithAllAndConditionsMatching_returnsMatchingXmlTag() {
        every { root.attributes } returns arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        )

        assertEquals(listOf(root), evaluator.findAllBy("type == \"value\" and name == \"test\" AND color == \"blue\""))
    }

    @Test
    fun findAllBy_givenPredicateWithSingleAndConditionNotMatching_returnsEmpty() {
        every { root.attributes } returns arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        )

        assertEquals(listOf(), evaluator.findAllBy("type == \"value\" and name == \"test\" AND color == \"green\""))
    }

    @Test
    fun findAllBy_givenPredicateWithSingleOrConditionMatching_returnsMatchingXmlTag() {
        every { root.attributes } returns arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        )

        assertEquals(listOf(root), evaluator.findAllBy("type == \"string\" OR name == \"bob\" or color == \"blue\""))
    }
}