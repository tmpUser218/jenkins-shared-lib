package org.example.Services

class GradleService {
    private static String gradleCmd(String mavenLocalRepo) {
        return "./gradlew -Dmaven.repo.local=${mavenLocalRepo}"
    }

    static String clean(script, String mavenLocalRepo) {
        script.sh "${gradleCmd(mavenLocalRepo)} clean"
    }

    static String assemble(script, String mavenLocalRepo) {
        script.sh "${gradleCmd(mavenLocalRepo)} assemble"
    }

    static String build(script, String mavenLocalRepo) {
        script.sh "${gradleCmd(mavenLocalRepo)} build"
    }

    static String publishToLocalMaven(script, String mavenLocalRepo) {
        script.sh "${gradleCmd(mavenLocalRepo)} publishToMavenLocal"
    }

    static String jacocoTestCoverage(script, String mavenLocalRepo, int minimumCoverage = 80) {
        script.sh "${gradleCmd(mavenLocalRepo)} jacocoTestReport jacocoTestCoverageVerification -PminimumCoverage=${minimumCoverage}"
    }
}