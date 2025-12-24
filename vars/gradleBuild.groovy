import org.example.Services.GradleService

def call(Map params) {
    if (!params || !params.repoPath)
        error('gradleBuild: repo path is required')
    GradleService.build(this, params.repoPath)
}