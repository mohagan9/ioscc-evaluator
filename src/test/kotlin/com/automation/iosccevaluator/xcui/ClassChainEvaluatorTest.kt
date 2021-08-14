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
    fun findAllBy_givenRootWithMatchingChildren_withMatchOnDirectPathQuery_returnsOnlyMatchingChildren() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        every { root.children } returns arrayOf(child1, child2)
        assertEquals(listOf(child1, child2), evaluator.findAllBy("$rootType/$xcuiElementType"))
    }

    @Test
    fun findAllBy_givenRootWithNestedChildren_withMatchOnDirectPathQuery_returnsOnlyDirectChildren() {
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
        assertEquals(listOf(child1, child2), evaluator.findAllBy("$rootType/$xcuiElementType"))
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
        assertEquals(listOf(child1), evaluator.findAllBy("$rootType/$xcuiElementType[1]"))
        assertEquals(listOf(child2), evaluator.findAllBy("$rootType/$xcuiElementType[2]"))
        assertEquals(listOf(child3), evaluator.findAllBy("$rootType/$xcuiElementType[3]"))
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

    @Test
    fun findAllBy_givenMatchOnSelectAllDirectChildrenByPredicateQuery_returnsMatchingParent() {
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
            createXmlAttributeMock("type", "NO_MATCH"),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "GREEN")
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "RED")
        ))
        every { child1.children } returns arrayOf(nestedChild1)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(listOf(child1), evaluator.findAllBy("$rootType/$xcuiElementType[\$color == \"RED\"\$]"))
    }

    @Test
    fun findAllBy_givenRootWithMatchOnSelectAllChildrenByPredicateQuery_returnsAllMatchingParents() {
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "RED")
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "GREEN")
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "NO_MATCH"),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", rootType),
            createXmlAttributeMock("color", "GREEN")
        ))
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)
        assertEquals(
            listOf(root, child1, child2),
            evaluator.findAllBy("**/$rootType[\$color == \"RED\"\$]")
        )
    }

    @Test
    fun findAllBy_givenMatchOnDeepDirectChildPathQuery_returnsMatchingChild() {
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
            createXmlAttributeMock("type", "NESTED_TYPE")
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

        assertEquals(listOf(nestedChild1), evaluator.findAllBy("$rootType/$xcuiElementType/NESTED_TYPE"))
    }

    @Test
    fun findAllBy_givenMatchOnDeepDirectChildPathWithPredicateQuery_returnsMatchingChildren() {
        val nestedType = "NESTED_TYPE"
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "RED")
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "GREEN")
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3)
        every { root.children } returns arrayOf(child1, child2, child3)

        assertEquals(
            listOf(nestedChild1a, nestedChild1b),
            evaluator.findAllBy("$rootType[\$color == \"RED\"\$]/$xcuiElementType[\$color == \"BLUE\"\$]/$nestedType")
        )
    }

    @Test
    fun findAllBy_givenMatchOnDeepDescendantChildPathWithPredicateQuery_returnsMatchingChildren() {
        val nestedType = "NESTED_TYPE"
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "RED")
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "GREEN")
        ))
        val child3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild1ai = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild3a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild3b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        every { nestedChild1a.children } returns arrayOf(nestedChild1ai)
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { child3.children } returns arrayOf(nestedChild3a, nestedChild3b)
        every { root.children } returns arrayOf(child1, child2, child3)

        assertEquals(
            listOf(nestedChild1a, nestedChild1ai, nestedChild1b, nestedChild3a, nestedChild3b),
            evaluator.findAllBy("**/$xcuiElementType[\$color == \"BLUE\"\$]/**/$nestedType")
        )
    }

    @Test
    fun findAllBy_givenMatchOnPathQueryWithPredicatesAfterSlashes_returnsMatchingChildren() {
        val nestedType = "NESTED_TYPE"
        val child1 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val child2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val nestedChild1a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild1b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild1ai = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild1aii = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild1ai3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild2i = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChild2ii = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "BLUE")
        ))
        val nestedChild2i3 = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2i4a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "RED")
        ))
        val nestedChild2i4b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType),
            createXmlAttributeMock("color", "GREEN")
        ))
        every { nestedChild1a.children } returns arrayOf(nestedChild1ai)
        every { nestedChild1ai.children } returns arrayOf(nestedChild1aii)
        every { nestedChild1aii.children } returns arrayOf(nestedChild1ai3)
        every { child1.children } returns arrayOf(nestedChild1a, nestedChild1b)
        every { child2.children } returns arrayOf(nestedChild2)
        every { nestedChild2.children } returns arrayOf(nestedChild2i)
        every { nestedChild2i.children } returns arrayOf(nestedChild2ii)
        every { nestedChild2ii.children } returns arrayOf(nestedChild2i3)
        every { nestedChild2i3.children } returns arrayOf(nestedChild2i4a, nestedChild2i4b)
        every { root.children } returns arrayOf(child1, child2)

        assertEquals(
            listOf(nestedChild1ai, nestedChild1aii, nestedChild2i, nestedChild2ii, nestedChild2i3),
            evaluator.findAllBy(
                "$rootType/$xcuiElementType/$nestedType[\$color == \"BLUE\"\$]/**/$nestedType[\$color == \"RED\"\$]"
            )
        )
    }

    @Test
    fun findAllBy_givenADescendantPathQuery_returnsAllMatches() {
        val nestedType = "NESTED_TYPE"
        val child = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", xcuiElementType)
        ))
        val nestedChild = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildi = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildii = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildi3a = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        val nestedChildi3b = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", nestedType)
        ))
        every { child.children } returns arrayOf(nestedChild)
        every { nestedChild.children } returns arrayOf(nestedChildi)
        every { nestedChildi.children } returns arrayOf(nestedChildii)
        every { nestedChildii.children } returns arrayOf(nestedChildi3a, nestedChildi3b)
        every { root.children } returns arrayOf(child)

        assertEquals(
            listOf(nestedChildi, nestedChildii, nestedChildi3a, nestedChildi3b),
            evaluator.findAllBy(
                "**/$nestedType/$nestedType"
            )
        )
    }
}
