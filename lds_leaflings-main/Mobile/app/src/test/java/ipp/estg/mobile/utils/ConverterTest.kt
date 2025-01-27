package ipp.estg.mobile.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ConverterTest{

    private lateinit var converter: Converter

    @Before
    fun setUp() {
        converter = Converter()
    }

    @Test
    fun `test fromStringList with empty list`() {
        val emptyList = emptyList<String>()
        val result = converter.fromStringList(emptyList)
        assertEquals("[]", result)
    }

    @Test
    fun `test fromStringList with single item`() {
        val singleItemList = listOf("test")
        val result = converter.fromStringList(singleItemList)
        assertEquals("[\"test\"]", result)
    }

    @Test
    fun `test fromStringList with multiple items`() {
        val multipleItemsList = listOf("item1", "item2", "item3")
        val result = converter.fromStringList(multipleItemsList)
        assertEquals("[\"item1\",\"item2\",\"item3\"]", result)
    }

    @Test
    fun `test toStringList with empty JSON array`() {
        val emptyJsonArray = "[]"
        val result = converter.toStringList(emptyJsonArray)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `test toStringList with single item`() {
        val singleItemJson = "[\"test\"]"
        val result = converter.toStringList(singleItemJson)
        assertEquals(1, result.size)
        assertEquals("test", result[0])
    }

    @Test
    fun `test toStringList with multiple items`() {
        val multipleItemsJson = "[\"item1\",\"item2\",\"item3\"]"
        val result = converter.toStringList(multipleItemsJson)
        assertEquals(3, result.size)
        assertEquals("item1", result[0])
        assertEquals("item2", result[1])
        assertEquals("item3", result[2])
    }

    @Test
    fun `test roundtrip conversion`() {
        val originalList = listOf("test1", "test2", "test3")
        val jsonString = converter.fromStringList(originalList)
        val resultList = converter.toStringList(jsonString)
        assertEquals(originalList, resultList)
    }

    @Test(expected = com.google.gson.JsonSyntaxException::class)
    fun `test toStringList with invalid JSON`() {
        converter.toStringList("invalid json")
    }

    @Test
    fun `test fromStringList with special characters`() {
        val specialCharsList = listOf("test\"quote", "test\\backslash", "test\nnewline")
        val result = converter.fromStringList(specialCharsList)
        val convertedBack = converter.toStringList(result)
        assertEquals(specialCharsList, convertedBack)
    }
}