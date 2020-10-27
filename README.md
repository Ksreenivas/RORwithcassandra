# RORwithcassandra
Rails App talking to Cassandra cluster

### Local Development (non docker)

```bash
$ make local
```

This command starts a local development server and open up a browser window. Most changes are reflected live without having to restart the server.

### Steps to deploy RubyonRails application using Cassandra

- Steps to follow to deploy ruby on rails applications to kubernetes cluster using Helm Charts and Jenkins pipeline and JobDSL plugin

## 1. Consider a ROR application cassandra-example-using-rails-master

- Fork the Repository to make necessary changes https://github.com/Ksreenivas/cassandra-example-using-rails

## 2. Inside root directory of create Dockerfile

- Create a Dockerfile inside the `RORwithcassandra/cassandra-example-using-rails-master/Dockerfile`

## 2.1  `[MultiBranchPipeline]` Inside root directory create Jenkinsfile 

 - Create a Jenkinsfile inside the `RORwithcassandra/cassandra-example-using-rails-master/Jenkinsfile`

## 3. Build the Docker image

 - `docker login` Login into Docker hub by providing your credentials.
 -  Run `docker build -t ror-cassandra:1.0.0 .`
    ```
    ➜  assignment docker images | grep cassandra
    ror-cassandra                                          1.0.0               a9cd6eb142d6        17 hours ago        93.4MB
    sreenivask/ror-cassandra                               1.0.0               a9cd6eb142d6        17 hours ago        93.4MB
    ```
- Run `docker tag a9cd6eb142d6 sreenivask/ror-cassandra:1.0.0`

## 4. Publish the Docker image `docker hub`

- Tag docker image `docker tag a9cd6eb142d6 sreenivask/ror-cassandra:1.0.0`
- Publish docker image to docker hub `docker push sreenivask/ror-cassandra:1.0.0`

```bash
➜  assignment: docker push sreenivask/ror-cassandra:1.0.0
The push refers to repository [docker.io/sreenivask/ror-cassandra]
f841a6c9dc76: Layer already exists
2a199f6c86ef: Layer already exists
01c25d87419c: Layer already exists
93ffda01d49b: Layer already exists
b899f29bf10b: Layer already exists
410b5fd47642: Layer already exists
12b703c99815: Layer already exists
e44bb72897d4: Layer already exists
720dc6953f4e: Layer already exists
ace0eda3e3be: Layer already exists
1.0.0: digest: sha256:08d2145d0b6a3811d14953c8a8cde133ad2e3e4a9515de0704614fc7a3228d03 size: 2409
```

## 5. Create the Helm Chart

- Create rails-app helm chart and cassandra cluster helm chart and Ingress controller

### 5.1 Create Helm chart for Rails-app

- Create helm chart for Rails-app `helm create rails-app`
- Update rails-app helm chart and run `helm lint .`
```bash
➜  rails-app helm lint .
==> Linting .
[INFO] Chart.yaml: icon is recommended

1 chart(s) linted, 0 chart(s) failed
```
- Create helm template to cross verify `helm template ./rails-app > rails-app-helm-template.yaml`


### 5.2 Create Helm chart for cassandra cluster

- Create helm chart for Rails-app `helm create cassandra`
- Update rails-app helm chart and run `helm lint .`
```bash
➜  cassandra helm lint .
==> Linting .
[INFO] Chart.yaml: icon is recommended

1 chart(s) linted, 0 chart(s) failed
```
- Create helm template to cross verify `helm template ./cassandra > cassandra-helm-template.yaml`


## 6. Deploy rails-app and cassandra cluster in Kubernetes cluster

- `Deploy helm chart` - You can either run `helm install rails-app .` 
    or `kubectl apply -f rails-app-helm-template.yaml`
- `Deploy helm chart` - You can either run `helm install cassandra .` or `kubectl apply -f cassandra-helm-template.yaml`

## 7. Update the source code

- Update Rails app with new features or bug-fixes.
- Run 
```bash
docker lint 
docker tag new-version 
docker build new-version 
Run Integration tests, Big Test, Large Tests
docker push new-version
```
## 8. Rollout updates (and rollbacks)
### Update

- Update helm charts 
```bash
Update the new docker image version in values.yaml file
```
- Run `helm upgrade rails-app .`
- To check revisions `helm history rails-app`
```bash
➜  rails-app helm history rails-app
REVISION	UPDATED                 	STATUS  	CHART          	APP VERSION	DESCRIPTION
1       	Mon Oct 26 19:07:28 2020	deployed	rails-app-0.1.0	1.16.0     	Install complete
```
### Rollback

- Rollback helm charts, check history of helm chart `helm history rails-app`
- Rollback to revision 1 `helm rollback rails-app 1`

## JobDSL plugin and Pipeline plugin

- Create free style job in jenkin https://plugins.jenkins.io/job-dsl/
- Free style job Build from groovy script file `/RORwithcassandra/pipeline/jobDSL/seedPipeline.groovy` 
  This seed job create a scans and create a `MBP MultiBranchPipeline` from `/RORwithcassandra/pipeline/jobDSL/multibranchJobs.groovy`
```bash
node('master') {
  stage('Checkout') {
    // Clean workspace and checkout shared library repository on the jenkins master
    cleanWs()
    checkout scm
  }

  stage('Seed') {
    // seed the jobs
    jobDsl(targets: 'resources/jobDSL/*.groovy', sandbox: false)
  }
}
```