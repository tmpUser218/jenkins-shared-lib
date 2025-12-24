import org.example.Services.GradleService

def call(Map params) {
    if (!params || !params.repoPath)
        error('gradleAssemble: repo path is required')
    GradleService.assemble(this, params.repoPath)
}