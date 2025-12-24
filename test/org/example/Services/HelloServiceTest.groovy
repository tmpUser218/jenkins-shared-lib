package org.example.Services

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class HelloServiceTest extends BasePipelineTest {

    @Before
    void setUp() {
        super.setUp()
        helper.scriptRoots += 'src'
    }

    @Test
    void testSayHello() {
        def capturedMessage = null
        def mockScript = [echo: { msg ->
            capturedMessage = msg
            println("[DEBUG] Echo called: $msg")
        }] as Object

        // Act: Call the service
        HelloService.sayHello(mockScript, "TestUser")

        // Assert: Verify the message
        assertNotNull("Echo should have been called", capturedMessage)
        assertEquals(
                "Hello, TestUser from a shared library",
                capturedMessage.toString()
        )
    }

    @Test
    void testSayHelloWithDefaultUser() {
        def capturedMessage = null
        def mockScript = [echo: { msg -> capturedMessage = msg }] as Object

        HelloService.sayHello(mockScript, "user")

        assertEquals(
                "Hello, user from a shared library",
                capturedMessage.toString()
        )
    }

    @Test
    void testSayHelloWithDifferentNames() {
        def testCases = [
                "Alice"  : "Hello, Alice from a shared library",
                "Bob"    : "Hello, Bob from a shared library",
                "Charlie": "Hello, Charlie from a shared library"
        ]

        testCases.each { name, expectedMessage ->
            def capturedMessage = null
            def mockScript = [echo: { msg -> capturedMessage = msg }] as Object

            HelloService.sayHello(mockScript, name)

            assertEquals(
                    "Failed for name: $name",
                    expectedMessage,
                    capturedMessage.toString()
            )
        }
    }
}