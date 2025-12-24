import org.example.Services.DockerService

def call(Map params = [:]) {
    validateParams(params)
    DockerService.login(this, params.registryUrl, params.username, params.password)
}

private void validateParams(Map params) {
    if (!params || !params.registryUrl || !params.username || !params.password)
        error('dockerLogin: parameters must include registryURL, username, and password')
}