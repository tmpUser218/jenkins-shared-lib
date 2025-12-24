import org.example.Services.GradleService

def call(Map params) {
    if (!params || !params.repoPath)
        error('gradleClean: repo path is required')
    GradleService.clean(this, params.repoPath)
}