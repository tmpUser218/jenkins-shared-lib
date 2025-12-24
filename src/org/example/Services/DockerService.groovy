package org.example.Services

class DockerService {

    static String login(script, String registryUrl, String username, String password) {
        script.sh "docker login ${registryUrl} -u ${username} -p ${password}"
    }

    static String build(script, String imageName, String dockerfilePath = 'Dockerfile', String contextPath = '.') {
        script.sh "docker build -t ${imageName} -f ${dockerfilePath} ${contextPath}"
    }

    static String push(script, String imageName) {
        script.sh "docker push ${imageName}"
    }

    static String logout(script, String registryUrl) {
        script.sh "docker logout ${registryUrl}"
    }
}
