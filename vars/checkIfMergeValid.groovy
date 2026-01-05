import org.example.Services.GitService

def call(Map args) {
    def gitService = new GitService(this)

    return gitService.checkIfMergeValid(
            args.source,
            args.target
    )
}
