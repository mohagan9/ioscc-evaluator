package com.automation.iosccevaluator.xcui

import com.automation.iosccevaluator.xcui.AttributeEvaluator.isAttributeMatch
import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlAttributeMock
import com.automation.iosccevaluator.xcui.setup.XmlTagMockFactory.createXmlTagMock
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AttributeEvaluatorTest {
    @Test
    fun isAttributeMatch_givenEqualsConditionWithAnyMatchingXmlTagAttribute_returnsTrue() {
        val xmlTag = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        ))
        assertTrue(
            isAttributeMatch("name == \"test\"", xmlTag)
        )
    }

    @Test
    fun isAttributeMatch_givenEqualsConditionWithNoMatchingXmlTagAttribute_returnsFalse() {
        val xmlTag = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        ))
        assertFalse(
            isAttributeMatch("name == \"blue\"", xmlTag)
        )
    }

    @Test
    fun isAttributeMatch_givenNotEqualsConditionWithAnyMatchingXmlTagAttribute_returnsTrue() {
        val xmlTag = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        ))
        assertTrue(
            isAttributeMatch("name != \"blue\"", xmlTag)
        )
    }

    @Test
    fun isAttributeMatch_givenNotEqualsConditionWithNoMatchingXmlTagAttribute_returnsFalse() {
        val xmlTag = createXmlTagMock(arrayOf(
            createXmlAttributeMock("type", "value"),
            createXmlAttributeMock("name", "test"),
            createXmlAttributeMock("color", "blue")
        ))
        assertFalse(
            isAttributeMatch("name != \"test\"", xmlTag)
        )
    }
}
