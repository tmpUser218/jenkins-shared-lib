import org.example.Services.DockerService

def call(Map params = [:]) {
    validateParams(params)
    DockerService.push(this, params.imageName)
}

private void validateParams(Map params) {
    if (!params || !params.imageName)
        error('dockerPush: image name is required')
}