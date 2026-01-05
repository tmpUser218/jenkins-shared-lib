import org.example.Services.GitService

def call(Map args = [:]) {
    def gitService = new GitService(this)

    gitService.mergeToProduction(
            args.productionBranch ?: 'production',
            args.sourceBranch ?: 'HEAD'
    )
}
