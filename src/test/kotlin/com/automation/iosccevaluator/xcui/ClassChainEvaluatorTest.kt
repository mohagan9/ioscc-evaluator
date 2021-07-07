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
        assertEquals(listOf(child1, child2, nestedChild3), evaluator.findAllBy(xcuiElementType))
    }

    @Test
    fun findAllBy_givenRootWithNestedChildren_whenMatchOnSelectAllDirectChildrenQuery_returnsOnlyNestedChildren() {
        val childType = "CHILD_TYPE"
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val nestedChild1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", childType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", childType)
        ))
        every { child1.children } returns arrayOf(nestedChild1)
        every { child2.children } returns arrayOf(nestedChild2)
        every { root.children } returns arrayOf(child1, child2)
        assertEquals(listOf(nestedChild1, nestedChild2), evaluator.findAllBy(childType))
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
        val nestedChild1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType)
        ))
        every { child1.children } returns arrayOf(nestedChild1)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(
            listOf(nestedChild1, nestedChild2, nestedChild3, child1, child2, root),
            evaluator.findAllBy("**/$rootType")
        )
    }
}
