package org.example.Services

class PostmanService {

    static void runPostmanTests(script, String collection, String environment, String reportFile = 'report.html') {
        def cmd = "newman run ${collection} -e ${environment} --reporters cli,htmlextra --reporter-htmlextra-export ${reportFile}"
        script.sh cmd
    }
}
