import org.example.Services.GradleService

def call(Map params) {
    if (!params || !params.repoPath)
        error('gradleJacocoTestCoverage: repo path is required')
    GradleService.jacocoTestCoverage(this, params.repoPath)
}