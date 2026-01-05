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
     * Merge current branch into production branch
     */
    void mergeToProduction(
            String productionBranch = 'production',
            String sourceBranch = 'HEAD'
    ) {
        steps.echo "Merging ${sourceBranch} into ${productionBranch}"

        steps.bat """
            git fetch origin
            git checkout ${productionBranch}
            git pull origin ${productionBranch}
            git merge --no-ff ${sourceBranch} -m "Merge ${sourceBranch} into ${productionBranch}"
            git push origin ${productionBranch}
        """
    }

    /**
     * Checks if source branch can be merged into target branch without conflicts
     * Does NOT perform the merge
     */
    boolean checkIfMergeValid(
            String sourceBranch,
            String targetBranch
    ) {
        steps.echo "🔎 Checking merge validity: ${sourceBranch} -> ${targetBranch}"

        int status = steps.bat(
                script: """
            git fetch origin
            git checkout ${targetBranch}
            git pull origin ${targetBranch}

            git merge --no-commit --no-ff ${sourceBranch} || true
        """,
                returnStatus: true
        )

        // Abort merge if Git entered merge state
        steps.bat(
                script: "git merge --abort || true",
                returnStatus: true
        )

        if (status == 0) {
            steps.echo "Merge is valid (no conflicts)"
            return true
        }

        steps.echo "Merge conflicts detected"
        return false
    }


    /**
     * Internal helper to extract a YAML key value from a Git ref
     */
    private String readYamlValue(String gitRef, String filePath, String yamlKey) {
        return steps.bat(
                script: """
                git show ${gitRef}:${filePath} \
                | yq e '.${yamlKey}' -
            """,
                returnStdout: true
        ).trim()
    }
}
