import org.example.Services.GitService

def call(Map args) {
    def gitService = new GitService(this)

    return gitService.hasYamlValueChanged(
            args.file ?: 'values.yaml',
            args.key,
            args.from ?: 'HEAD~1',
            args.to ?: 'HEAD'
    )
}
