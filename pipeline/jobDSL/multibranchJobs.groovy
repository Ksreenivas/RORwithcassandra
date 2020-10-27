multibranchPipelineJob("cassandra-example-using-rails") {

    triggers {
        cron('@hourly')
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(5)
        }
    }   

    branchSources {
        github {
            scanCredentialsId('Sets-scan-credentials-for-authentication-with-GitHub.')
            repoOwner('Ksreenivas')
            repository("cassandra-example-using-rails")
            buildOriginBranch(true)
            buildOriginPRMerge(true)
            buildOriginBranchWithPR(false)
            id("cassandra-example-using-rails-branch-id")
        }
    }

    factory {
        // Search for Jenkinsfile inside repository
        workflowBranchProjectFactory {
            scriptPath('/RORwithcassandra/cassandra-example-using-rails-master/Jenkinsfile')
        }
    }
}