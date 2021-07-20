package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlAttributeMock
import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlTagMock
import com.intellij.psi.xml.XmlTag
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ClassChainEvaluatorTest {
    private lateinit var evaluator: ClassChainEvaluator
    private val root: XmlTag = mockk()
    private val xcuiElementType = "XCUIElement"
    private val rootType = "ROOT_TYPE"

    @BeforeEach
    fun setup() {
        evaluator = ClassChainEvaluator(root)
        every { root.attributes } returns arrayOf(
            createXmlAttributeMock("type", rootType)
        )
        every { root.children } returns arrayOf()
    }

    @Test
    fun findAllBy_givenNullRoot_returnsEmptyList() {
        assertEquals(mutableListOf<XmlTag>(), ClassChainEvaluator(null).findAllBy(xcuiElementType))
    }

    @Test
    fun findAllBy_givenRootWithNoChildren_withMatchOnQuery_returnsRoot() {
        assertEquals(mutableListOf(root), evaluator.findAllBy(rootType))
    }

    @Test
    fun findAllBy_givenRootWithMatchingChildren_withMatchOnSelectAllDirectChildrenQuery_returnsOnlyChildren() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        every { root.children } returns arrayOf(child1, child2)
        assertEquals(listOf(child1, child2), evaluator.findAllBy(xcuiElementType))
    }

    @Test
    fun findAllBy_givenRootWithNestedChildren_withMatchOnSelectAllDirectChildrenQuery_returnsOnlyDirectChildren() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NO_MATCH")
        ))
        val nestedChild1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        every { child1.children } returns arrayOf(nestedChild1)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(listOf(child1, child2), evaluator.findAllBy(xcuiElementType))
    }

    @Test
    fun findAllBy_givenNsPredicateQuery_returnsPredicateMatch() {
        every { root.attributes } returns arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "blue")
        )
        assertEquals(mutableListOf(root), evaluator.findAllBy("\$color == \"blue\"\$"))
    }

    @Test
    fun findAllBy_givenRootWithMatchOnSelectAllChildrenQuery_returnsAllChildren() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NO_MATCH")
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(
            listOf(root, child1, nestedChild1a, nestedChild1b, child2, nestedChild2, nestedChild3),
            evaluator.findAllBy("**/$rootType")
        )
    }

    @Test
    fun findAllBy_givenRootWithChildren_whenMatchOnSelectNthDirectChildrenQuery_returnsOnlyMatchingChild() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(listOf(child1), evaluator.findAllBy("$xcuiElementType[1]"))
        assertEquals(listOf(child2), evaluator.findAllBy("$xcuiElementType[2]"))
        assertEquals(listOf(child3), evaluator.findAllBy("$xcuiElementType[3]"))
    }

    @Test
    fun findAllBy_givenRootWithMatchOnSelectNthChildrenQuery_returnsMatchingChildren() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NO_MATCH")
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(
            listOf(root, child1, nestedChild1a, nestedChild2, nestedChild3),
            evaluator.findAllBy("**/$rootType[1]")
        )
        assertEquals(
            listOf(nestedChild1b, child2), evaluator.findAllBy("**/$rootType[2]")
        )
    }
}
