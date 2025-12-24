package org.example.Services

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class PostmanServiceTest extends BasePipelineTest {

    @Before
    void setUp() {
        super.setUp()
        helper.scriptRoots += 'src'
    }

    @Test
    void testRunPostmanTests() {
        // Arrange
        def capturedCommand = null
        def mockScript = [sh: { cmd ->
            capturedCommand = cmd
            println("[DEBUG] sh called with command: $cmd")
        }] as Object

        String collection = "myCollection.json"
        String environment = "env.json"
        String expectedCommand = "newman run myCollection.json -e env.json --reporters cli,htmlextra --reporter-htmlextra-export report.html"

        // Act
        PostmanService.runPostmanTests(mockScript, collection, environment)

        // Assert
        assertNotNull("sh should have been called", capturedCommand)
        assertEquals(
                "Command should match expected format",
                expectedCommand,
                capturedCommand.toString()
        )
    }

    @Test
    void testRunPostmanTestsWithCustomReportFile() {
        // Arrange
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        String collection = "api-tests.json"
        String environment = "production.json"
        String customReportFile = "custom-report.html"
        String expectedCommand = "newman run api-tests.json -e production.json --reporters cli,htmlextra --reporter-htmlextra-export custom-report.html"

        // Act
        PostmanService.runPostmanTests(mockScript, collection, environment, customReportFile)

        // Assert
        assertEquals(
                "Command should include custom report file",
                expectedCommand,
                capturedCommand.toString()
        )
    }

    @Test
    void testRunPostmanTestsWithDefaultReportFile() {
        // Arrange
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        String collection = "test-collection.json"
        String environment = "staging.json"
        String expectedCommand = "newman run test-collection.json -e staging.json --reporters cli,htmlextra --reporter-htmlextra-export report.html"

        // Act
        PostmanService.runPostmanTests(mockScript, collection, environment, "report.html")

        // Assert
        assertEquals(
                "Command should use default report file when explicitly passed",
                expectedCommand,
                capturedCommand.toString()
        )
    }

    @Test
    void testRunPostmanTestsWithDifferentCollections() {
        // Arrange
        def testCases = [
                ["collection1.json", "env1.json"] : "newman run collection1.json -e env1.json --reporters cli,htmlextra --reporter-htmlextra-export report.html",
                ["collection2.json", "env2.json"] : "newman run collection2.json -e env2.json --reporters cli,htmlextra --reporter-htmlextra-export report.html",
                ["api-collection.json", "qa.json"]: "newman run api-collection.json -e qa.json --reporters cli,htmlextra --reporter-htmlextra-export report.html"
        ]

        testCases.each { inputs, expectedCommand ->
            def capturedCommand = null
            def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

            // Act
            PostmanService.runPostmanTests(mockScript, inputs[0], inputs[1])

            // Assert
            assertEquals(
                    "Failed for collection: ${inputs[0]}, environment: ${inputs[1]}",
                    expectedCommand,
                    capturedCommand.toString()
            )
        }
    }
}