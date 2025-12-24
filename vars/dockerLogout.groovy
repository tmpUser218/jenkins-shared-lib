import org.example.Services.DockerService

def call(Map params = [:]) {
    validateParams(params)
    DockerService.logout(this, params.registryUrl)
}

private void validateParams(Map params) {
    if (!params || !params.registryUrl)
        error('dockerLogout: registryURL is required')
}