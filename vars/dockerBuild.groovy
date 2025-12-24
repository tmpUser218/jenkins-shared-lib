import org.example.Services.DockerService

def call(Map params = [:]) {
    validateParams(params)
    String dockerfilePath = params.dockerfilePath ?: 'Dockerfile'
    String contextPath = params.contextPath ?: '.'
    DockerService.build(this, params.imageName, dockerfilePath, contextPath)
}

private void validateParams(Map params) {
    if (!params || !params.imageName)
        error('dockerBuild: image name is required')
}