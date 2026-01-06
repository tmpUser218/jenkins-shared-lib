package org.example.Services

class GitService implements Serializable {

    def steps

    GitService(steps) {
        this.steps = steps
    }

    /**
     * Checks if a specific key in a YAML file has changed between two commits
     */
    boolean hasYamlValueChanged(
            String filePath,
            String yamlKey,
            String fromRef = 'HEAD~1',
            String toRef = 'HEAD'
    ) {
        steps.echo "Checking change for '${yamlKey}' in ${filePath}"

        String oldValue = readYamlValue(fromRef, filePath, yamlKey)
        String newValue = readYamlValue(toRef, filePath, yamlKey)

        steps.echo "Old value: ${oldValue}"
        steps.echo "New value: ${newValue}"

        return oldValue != newValue
    }

    /**
     * Merge source branch into production branch
     */
    void mergeToProduction(
            String productionBranch = 'production',
            String sourceBranch = 'HEAD'
    ) {
        steps.bat """
            set -e

            git config --global user.email "Jenkins@example.com"
            git config --global user.name "Jenkins Machine"

            git fetch origin

            git checkout ${productionBranch}
            git pull origin ${productionBranch}

            git merge --no-ff ${sourceBranch}
        """
            // git push origin ${productionBranch}
    }

    /**
     * Checks if source branch can be merged into target branch without conflicts
     * Does NOT perform the merge
     */
    boolean checkIfMergeValid(
            String sourceBranch,
            String targetBranch
    ) {
        try {
            steps.bat """
                set -e

                git config --global user.email "Jenkins@example.com"
                git config --global user.name "Jenkins Machine"

                git fetch origin

                git checkout ${targetBranch}
                git pull origin ${targetBranch}

                git merge --no-commit --no-ff ${sourceBranch}

                REM Check if merge succeeded (no conflicts)
                if %errorlevel% neq 0 (
                    exit 1
                )

                git merge --abort
            """
            return true
        } catch (Exception e) {
            // Cleanup in case merge partially applied
            steps.bat 'git merge --abort || true'
            return false
        }
    }

    /**
     * Internal helper to extract a YAML key value from a Git ref
     */
    private String readYamlValue(String gitRef, String filePath, String yamlKey) {
        return steps.bat(
                script: """
                @git show ${gitRef}:${filePath} \
                | yq e .${yamlKey} -
            """,
                returnStdout: true
        ).trim()
    }

}
