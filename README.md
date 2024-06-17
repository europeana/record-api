# Record API
New version (v3) of the Record API that is for the first time decoupled from Search API

## Deployment
1. Generate a Docker image using the project's [Dockerfile](Dockerfile)

2. Configure the application by generating a `record-api.user.properties` file and placing this in the 
[k8s](k8s) folder. After deployment this file will override the settings specified in the `record-api.properties` file
located in the [record-api-web/src/main/resources](record-api-web/src/main/resources) folder. The .gitignore file make sure the .user.properties file
is never committed.

3. Configure the deployment by setting the proper environment variables specified in the configuration template files
in the [k8s](k8s) folder

4. Deploy to Kubernetes infrastructure

# Europeana Record API

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=europeana_record-api)](https://sonarcloud.io/summary/new_code?id=europeana_record-api)
