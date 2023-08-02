# Record API
New version (v3) of the Record API that is for the first time decoupled from Search API

## Deploy
To deploy your instance you can use the Docker files in `docker/compose` folder. This image contains Tomcat only.
However at the moment we do not have a Mongo database or Solr engine in Docker yet.

The addresses and login credentials of all these services are specified in the europeana.properties file located in the
/api2/api2-war/src/main/resources/ folder. For the moment you still need to fill in all the 'REMOVED' values (login
credentials for services that are not dockerized yet). **Make sure you never commit these changes!**
It's safer to place these login credentials in a europeana.user.properties file in the same folder because this file
is set to be ignored by git. All settings in the europeana.user.properties will override those in the europeana.properties.


# Europeana Record API

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=europeana_record-api)](https://sonarcloud.io/summary/new_code?id=europeana_record-api)
