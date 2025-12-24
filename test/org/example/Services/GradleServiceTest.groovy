package org.example.Services

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class GradleServiceTest extends BasePipelineTest {

    @Before
    void setUp() {
        super.setUp()
        helper.scriptRoots += 'src'
    }

    @Test
    void testClean() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.clean(mockScript, "/path/to/local/repo")

        assertNotNull("sh should have been called", capturedCommand)
        assertEquals(
                "./gradlew -Dmaven.repo.local=/path/to/local/repo clean",
                capturedCommand.toString()
        )
    }

    @Test
    void testAssemble() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.assemble(mockScript, "/another/repo")

        assertEquals(
                "./gradlew -Dmaven.repo.local=/another/repo assemble",
                capturedCommand.toString()
        )
    }

    @Test
    void testBuild() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.build(mockScript, "/custom/repo")

        assertEquals(
                "./gradlew -Dmaven.repo.local=/custom/repo build",
                capturedCommand.toString()
        )
    }

    @Test
    void testPublishToLocalMaven() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.publishToLocalMaven(mockScript, "/maven/repo")

        assertEquals(
                "./gradlew -Dmaven.repo.local=/maven/repo publishToMavenLocal",
                capturedCommand.toString()
        )
    }

    @Test
    void testJacocoTestCoverageWithDefault() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.jacocoTestCoverage(mockScript, "/test/repo")

        assertEquals(
                "./gradlew -Dmaven.repo.local=/test/repo jacocoTestReport jacocoTestCoverageVerification -PminimumCoverage=80",
                capturedCommand.toString()
        )
    }

    @Test
    void testJacocoTestCoverageWithCustomValue() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        GradleService.jacocoTestCoverage(mockScript, "/coverage/repo", 90)

        assertEquals(
                "./gradlew -Dmaven.repo.local=/coverage/repo jacocoTestReport jacocoTestCoverageVerification -PminimumCoverage=90",
                capturedCommand.toString()
        )
    }

    @Test
    void testDifferentMavenRepoPaths() {
        def testCases = [
                "/home/user/.m2/repository" : "./gradlew -Dmaven.repo.local=/home/user/.m2/repository clean",
                "/opt/maven/repo"           : "./gradlew -Dmaven.repo.local=/opt/maven/repo clean",
                "C:\\Users\\test\\.m2\\repo": "./gradlew -Dmaven.repo.local=C:\\Users\\test\\.m2\\repo clean"
        ]

        testCases.each { repoPath, expectedCommand ->
            def capturedCommand = null
            def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

            GradleService.clean(mockScript, repoPath)

            assertEquals(
                    "Failed for repo path: $repoPath",
                    expectedCommand,
                    capturedCommand.toString()
            )
        }
    }
}