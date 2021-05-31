package com.automation.iosccevaluator.xcui.setup

import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlTag
import io.mockk.every
import io.mockk.mockk

internal object XmlTagMockFactory {
    fun createXmlAttributeMock(name: String, value: String): XmlAttribute {
        val attr: XmlAttribute = mockk()
        every { attr.name } returns name
        every { attr.value } returns value
        return attr
    }

    fun createXmlTagMock(attributes: Array<XmlAttribute>): XmlTag {
        val xmlTag: XmlTag = mockk()
        every { xmlTag.attributes } returns attributes
        every { xmlTag.children } returns arrayOf()
        return xmlTag
    }
}
