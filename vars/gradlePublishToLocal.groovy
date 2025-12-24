import org.example.Services.GradleService

def call(Map params) {
    if (!params || !params.repoPath)
        error('gradlePublishToLocalMaven: repo path is required')
    GradleService.publishToLocalMaven(this, params.repoPath)
}