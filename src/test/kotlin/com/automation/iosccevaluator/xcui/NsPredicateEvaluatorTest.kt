package com.automation.iosccevaluator.xcui

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

internal class NsPredicateEvaluatorTest {
    companion object {
        private lateinit var evaluator: NsPredicateEvaluator
        private val root: XmlTag = mockk()

        @BeforeAll
        @JvmStatic
        fun setup() {
            evaluator = NsPredicateEvaluator(root)
        }
    }

    @Test
    fun findBy_givenRootWithMatchingAttributeValue_returnsRoot() {
        val attr:XmlAttribute = mockk()
        every { attr.name } returns "type"
        every { attr.value } returns "value"
        every { root.attributes } returns arrayOf(attr)

        assertTrue(arrayOf(root).contentEquals(evaluator.findBy("type == \"value\"")))
    }

    @Test
    fun findBy_givenRootWithNoMatchingAttributeValue_returnsEmpty() {
        val attr:XmlAttribute = mockk()
        every { attr.name } returns "type"
        every { attr.value } returns "no-match"
        every { root.attributes } returns arrayOf(attr)

        assertTrue(arrayOf<XmlTag>().contentEquals(evaluator.findBy("type == \"value\"")))
    }
}