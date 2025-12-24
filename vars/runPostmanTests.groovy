import org.example.Services.PostmanService

def call(Map params = [:]) {

    if (!params.collection || !params.environment)
        error "Both 'collection' and 'environment' parameters are required!"
    def reportFile = params.reportFile ?: 'report.html'

    PostmanService.runPostmanTests(this, params.collection, params.environment, reportFile)
}