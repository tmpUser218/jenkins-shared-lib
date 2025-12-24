package org.example.Services

import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*

class DockerServiceTest extends BasePipelineTest {

    @Before
    void setUp() {
        super.setUp()
        helper.scriptRoots += 'src'
    }

    @Test
    void testLogin() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.login(mockScript, "registry.example.com", "myuser", "mypassword")

        assertNotNull("sh should have been called", capturedCommand)
        assertEquals(
                "docker login registry.example.com -u myuser -p mypassword",
                capturedCommand.toString()
        )
    }

    @Test
    void testBuildWithAllParameters() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.build(mockScript, "myapp:latest", "Dockerfile.prod", "./app")

        assertEquals(
                "docker build -t myapp:latest -f Dockerfile.prod ./app",
                capturedCommand.toString()
        )
    }

    @Test
    void testBuildWithDefaultDockerfile() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.build(mockScript, "myapp:v1.0", "Dockerfile", "./project")

        assertEquals(
                "docker build -t myapp:v1.0 -f Dockerfile ./project",
                capturedCommand.toString()
        )
    }

    @Test
    void testBuildWithDefaultContextPath() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.build(mockScript, "service:dev", "docker/Dockerfile", ".")

        assertEquals(
                "docker build -t service:dev -f docker/Dockerfile .",
                capturedCommand.toString()
        )
    }

    @Test
    void testBuildWithAllDefaults() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.build(mockScript, "app:latest")

        assertEquals(
                "docker build -t app:latest -f Dockerfile .",
                capturedCommand.toString()
        )
    }

    @Test
    void testPush() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.push(mockScript, "myregistry.com/myapp:latest")

        assertEquals(
                "docker push myregistry.com/myapp:latest",
                capturedCommand.toString()
        )
    }

    @Test
    void testLogout() {
        def capturedCommand = null
        def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

        DockerService.logout(mockScript, "registry.example.com")

        assertEquals(
                "docker logout registry.example.com",
                capturedCommand.toString()
        )
    }

    @Test
    void testDifferentRegistriesAndImages() {
        def testCases = [
                [
                        registry: "docker.io",
                        username: "alice",
                        password: "secret123",
                        expected: "docker login docker.io -u alice -p secret123"
                ],
                [
                        registry: "registry.gitlab.com",
                        username: "gitlab-ci",
                        password: "token456",
                        expected: "docker login registry.gitlab.com -u gitlab-ci -p token456"
                ],
                [
                        registry: "ghcr.io",
                        username: "github",
                        password: "pat789",
                        expected: "docker login ghcr.io -u github -p pat789"
                ]
        ]

        testCases.each { testCase ->
            def capturedCommand = null
            def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

            DockerService.login(mockScript, testCase.registry, testCase.username, testCase.password)

            assertEquals(
                    "Failed for registry: ${testCase.registry}",
                    testCase.expected,
                    capturedCommand.toString()
            )
        }
    }

    @Test
    void testDifferentImageNamesForPush() {
        def testCases = [
                "myapp:latest"               : "docker push myapp:latest",
                "registry.example.com/app:v1": "docker push registry.example.com/app:v1",
                "ghcr.io/org/project:nightly": "docker push ghcr.io/org/project:nightly"
        ]

        testCases.each { imageName, expectedCommand ->
            def capturedCommand = null
            def mockScript = [sh: { cmd -> capturedCommand = cmd }] as Object

            DockerService.push(mockScript, imageName)

            assertEquals(
                    "Failed for image: $imageName",
                    expectedCommand,
                    capturedCommand.toString()
            )
        }
    }
}