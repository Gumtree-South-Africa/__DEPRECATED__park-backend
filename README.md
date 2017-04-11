# Park backend
## Setting up the application
### Environment Variables
Outside of the WAR file, some variables needs to be set before deploying. Some of them, are optional because they have a default value, but it is recommended to populate them anyway. The following table contains summarized information about those variables.

***IMPORTANT**: Environment variables without default value MUST be set, otherwise the application will fail at start.*
 
| Variable name | Description | Default Value | Example Value |
| ------------- | ----------- | ------------- | ------------- | 
| PARK_URL | REST API address | https://rest.vivanuncios.us | https://rest.vivanuncios.us |
| PARK_WEB_URL | Website address | https://www.vivanuncios.us | http://www.vivanuncios.us |
| LOG4J_CUSTOM_CONFIG | Path to the file that contains the log4j2 configuration file. More info about how to set up this config file: https://logging.apache.org/log4j/2.x/manual/configuration.html | /etc/tomcat/log4j2.xml |
| DB_MYSQL_URL | MySql contection URL | | jdbc:mysql://localhost:3306/park |
| DB_MYSQL_USER | MySql username | | | 	 
| DB_MYSQL_PASSWORD | MySql user password | |
| MEMCACHED_HOSTS | List of Memcached hosts and ports | | memcached-01.com:11211,memcached-02.com:11211|
| ELASTICSEARCH_CLUSTER_NAME | Name of the Elasticsearch cluster (in the Elasticsearch configuration, the cluster name is defined at Elasticsearch.yml under the name 'cluster.name') | | |
| ELASTICSEARCH_CLUSTER_NODES | List of Elasticsearch hosts and ports, separated by commas | | elastic-01.com:9300,elastic-02.com:9300 |
| ACTIVEMQ_BROKER_URL | ActiveMQ connection URL	| tcp://localhost:61616 | tcp://activemq.newapptest.com:61616 |
| ACTIVEMQ_USER | User to be used to connect to to ActiveMQ | admin | |
| ACTIVEMQ_PASSWORD | Password to be used to connect to to ActiveMQ	| admin | |
| ACTIVEMQ_PUSH_QUEUE | JMS push queue name	| push-queue | |
| ACTIVEMQ_EMAIL_QUEUE | JMS email queue name | email-queue | |
| MAIL_HOST | Mail server host | localhost | |
| MAIL_USER | If MAIL_AUTHENTICATION is set to true, then the username for the account at the mail host is required. | postmaster@vivanuncios.us | |
| MAIL_FROM | Mail account that appears in the emails send from Park |	postmaster@vivanuncios.us | | | MAIL_PWD | If MAIL_AUTHENTICATION is set to true, then the password for the account at the mail host is required | 41a1ae47a727d77039dede4b2675a44c	| | 
| MAIL_PORT | Mail server port. | 25 | | 
| MAIL_PROTOCOL | Mail protocol | smtp | |
| MAIL_SMTP | Local host name | ebay.com | |
| MAIL_AUTHENTICATION | If true, attempt to authenticate the user using the AUTH command | false | 
| MAIL_LOGO_URL | | | http://www.vivanuncios.us/statics/images/mailLogo.png | 
| SUPPORT_EMAIL_ADDRESS | Email address that receives user feedback emails. This sets the "to" field | support@vivanuncios.us | |
| EMAIL_ADDRESS | Email address that receives user feedback emails. This sets the "from" field | support@vivanuncios.us | |
| IOS_CERTIFICATE_FILE | Path to the APNS Certificate file | | `/opt/park/APNS_ENTERPRISE.pfx` |
| IOS_CERTIFICATE_PWD | Password for the APNS Certificate | | |
| FACEBOOK_APP_ID | App Id for Facebook and Park | 633329433410004 | |
| FACEBOOK_APP_SECRET | App secret for Facebook and Park | 328a506d8ab7e3e1ffe12b0b77716b13	 | | 
| TWITTER_API_KEY |App Id for Twitter and Park	| HuRLJWfZE5RE0Tv0dYsjYDMVf	 | | 
| TWITTER_API_SECRET | App secret for Twitter and Park | HT8vg9Gau6j3AS2GbmvzqMtkZe3qQZrS3uo6C6Rw9CO3ur7AVP | | 
| EPS_DEV_ID | | 54ff67d1-ce84-4e8e-b3ea-8e3586c2e3f6 | |
| EPS_APP_ID | | eBayClas-6322-436c-9865-3de70df7a632 | |
| EPS_CERT_ID | | 6e2ec48d-a4c0-48a2-967c-42d79a8aa26c | |	  
| EPS_URL | | https://api.ebay.com/ws/api.dll | | 
| EPS_TOKEN | | `AgAAAA**AQAAAA**aAAAAA**z8AjVw**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AEk4qkD5SLpQWdj6x9nY+seQ**iFABAA**AAMAAA**AMg/+l8ENtkoybe0APACdDupECn96cm3nqUnq/JlqVUNQHBwF+Qb7cWRJ7ekNoI64+UJtoliJLQCTaKwG4z9dOGXnEt1kBOUgNe2KedqplsQDuPpIUTwzctkowCS6EJB+7kee4NpJGil+Bq/KXfuWAZLnq244f3PZfMMgl+7v4LX4paOaFXrnokHaEJ/n/kTbjpjTl8bIEF4omwgZL0tOva2aklEUUwaQHv1iC2tsffqScJCu1pN73TO7vOTXaNh6p3dPJv8tY/j+cYJ5oU5WuZTRJv/xULIWrASZ4Ub6PAkknpx50bCMFEIhJ/+qhv2cTX+7/2SLJjGN+iVkf47uXZqY1978RzRL3g93T3kjg8Q46CHOl1j0oz3NjlTb0wmb5y/Wg0BpPZV2OAOahw6dFTMNrUjV1TWzAcyGEX5p3v4nqQIbZpLD5dp2+PY2tzFK0zUgjy3HAyX6bUytkTai9XADrjiU/qDgvb3cPP4ml9q7PxcaQtx1d9bn4+Y6BrfVbMAwIb1uCaDhCnN9ReFvTbaA5pLO+K26FlxU9P+A+k35DkTU9ussK5X1m9MRDbVY6mREsfZPVtuiAmE/ILrDYB098M0SYxsKp44Lz7w0r59dLDIzQV1pQh+xmE9XB88LNI3MAAtlK5cY07zVpG0c9HfRwPHKmRuCKprP5Ik3y5AmdHXgNmu9zqJ2FdrqdvyTUBuwF7nOKlSOhKmYKRU0b4y5GTmSRWagPmrJZohgbINC9+ZmxLYMZvRpe/4HPlM` | | 
| SWRVE_ADABANDONMENT_APIKEY_ANDROID | The Swrve api key for ad abandonment transactional push notification camapaign for Android | 41e6000a-2f00-4d90-9e8e-ae2ca10526f7 | |
| SWRVE_ADABANDONMENT_APIKEY_ANDROID | The Swrve api key for ad abandonment transactional push notification camapaign for iOS | 91ff15ec-36fd-42e2-af67-5df260802d3c | |



### Elasticsearch
This application relays on [Elasticsearch 2.4](https://www.elastic.co/products/elasticsearch) to index data.
In this application, besides the normal Elasticsearch node configuration, we need two variables to be set as environment variables (as detailed above): `ELASTICSEARCH_CLUSTER_NAME` (which defines the name of the cluster) and `ELASTICSEARCH_CLUSTER_NODES` (which includes the list of nodes available). Both variables needs to be populated with information taken from Elasticsearch configuration file: `Elasticsearch.yml`. The first value can be found under the name of `cluster.name` and the second value is the combination of nodes names (properties under the name of node.name in each Elasticsearch.yml file) and nodes ports.

Additionally, we use two plugins: HEAD plugin for monitoring and administration from web and ICU for extra analysis modules.

Installation can be done with: 
* `./bin/plugin --install mobz/elasticsearch-head` 
* `./bin/plugin --install elasticsearch/elasticsearch-analysis-icu/2.7.0`

_In our environment, we let Elasticsearch engine to cope with node rebalancing (Elasticsearch usually uses port 9300 for cluster communication) and we only added a load balancer for query requests (usually at port 9200)._

### Memcached
We realy on *memcached* as a distributed caching system. We use it mainly for caching user sessions because we need to access them frecuenlty.
The configuration of *memcached* severs are efined by the environemnt. We only need `MEMCACHED_HOSTS` environment variable configured properly (_host1:port1,host2:port2,..._) 

**Note:** In case a failvoer server wanted to be configured, our code is ready to use only one Memcached server under a normal execution; but when this server fails, automatically starts using the second one instead. Their hosts are defined as the variable `MEMCACHED_HOSTS`. If needed, we can update our code in order to turn the second server into a redundancy instance.

### Database
We use MySQL 5 for data persistence.
The way we have to set this up is by using `DB_*` environment properties as defined above. 
Please consider the following links to configure that URL 
* Basic config: https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
* If *replication* is needed, https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-master-slave-replication-connection.html

**IMPORTANT:** It's mandatory to set this env vars (DB_MYSQL_URL, DB_MYSQL_USER, DB_MYSQL_PASSWORD).

_Note: we tested in our environmnet swith MySQL 5.6.17_

### ActiveMQ 
We use ActiveMQ as a JMS server for queueing notifications to be sent to users in two flavours: _email_ and _push_.
The goal of using JMS for that is to asynchronously send emails and/or push notifications to users from the backend when needed, for example: chats, item liked, user follow, etc.

The way to set up ActiveMQ is just by defining the specific env vars: `ACTIVEMQ_BROKER_URL`, `ACTIVEMQ_USER` and `ACTIVEMQ_PASSWORD`, `ACTIVEMQ_EMAIL_QUEUE`, `ACTIVEMQ_PUSH_QUEUE` as explained above.

Please refer to http://activemq.apache.org/uri-protocols.html to get support about connection URL

### Logging
We use [Log4j2](http://logging.apache.org/log4j/2.x/) for logging.
The configuration must be defined in an external file and addressed from the environment variable `logging.config`. Please refer to https://logging.apache.org/log4j/2.x/manual/configuration.html to get support on how to configure log4j2 config file. _We support all available formats: XML, properties, JSON, etc..._

### Email server 
We rely on javamail 1.4 for programatically **email sending**. Default values allow not to use authentication (MAIL_AUTHENTICATION default is false). In case there is a reason for using authentication, values to update are MAIL_PWD and MAIL_USER. The rest of the values are also set: MAIL_PORT is 25, MAIL_PROTOCOL is stmp, MAIL_HOST is localhost, MAIL_SMTP is ebay.com and, SUPPORT_EMAIL_ADDRESS and EMAIL_ADDRESS are the email addr
esses that we are using to send and receive emails. Additionally, MAIL_DUMMY_MODE could be useful for testing purposes if actual sending needs to be avoid. Default value is false, i.e. emails are sent.

### iOS Certificate
In order to send push notification to iOS devices, we need to use a valid certificate.
The path to the iOS certificate file and the password are required elements in the variable definition. They are addressed as `IOS_CERTIFICATE_FILE` (the path to the file including file name and extension) and `IOS_CERTIFICATE_PWD` as defined above.

_Please contact backend developers for getting the certificate and password if needed_

The incorrect definition of those values implies that iOS push notification cannot be sent and the mistake can be only find out in logs when trying to execute sending.

### URLs
Regarding URLs, the definition of two elements is required. First, `PARK_URL`, that contains the URL to the `REST API`. The second is `PARK_WEB_URL` that is included in the emails we send and specifies the URL for the website. None default value is set in this case.
For example, in the current Production environment, the values are `https://rest.vivanuncios.us` for `PARK_URL` and `https://www.vivanuncios.us` for `PARK_WEB_URL`.

### Social Credentials
We defined default values for social networks, anyway those are the credentials that we use for our Production releases. Those valued should be overridden with a new set of credentials for non-production environments. These are _Facebook_ and _Twitter_ configuration values for our QA environment:
```
FACEBOOK_APP_ID=660832997326314
FACEBOOK_APP_SECRET=450b0a369c94f751682e905ecb7e9e5c
TWITTER_API_KEY=bqxsFGsFaUCsyemxCmrPWGA8Y
TWITTER_API_SECRET=AekjV52btB3IHD139ZTj3PFxN1MXoUdVJm3oONQ87uvclvalBv
```

### Sitemap
We have a cron job that creates _sitemap.xml_ files daily, to let the Website index all URLs needed to to SEO friendly.

In order to configure that cron properly, a few environment variables are required  to obtain a successful generation of the sitemap files. If successful, the root sitemap file will be generated at the path defined with the variable `SITEMAP_PARK_ROOT_DIRECTORY`. This file will contain references to nested files that will be generated at `SITEMAP_PARK_NESTED_DIRECTORY`. The variable `SITEMAP_PARK_OLD_DIRECTORY` defines the destination of the sitemap generated the day before the current generation.
We use a job that daily relocates (at 2 am) the sitemap files from the previous location to the final destination. The final destination should correspond to the variable `SITEMAP_PARK_HOST` for the root sitemap file and `SITEMAP_PARK_ROOT_HOST` for the rest of the files. This job, besides relocating the files, deletes the ones generated the day before. In our environments, we set the `SITEMAP_PARK_NESTED_DIRECTORY` value as a directory inside the `SITEMAP_PARK_ROOT_DIRECTORY`. Therefore, a folder and the root sitemap file are the only files to be relocated.

### EPS
All variables related to ePS are set by default. We only have one EPS instance and use the same data in any environment. 


## First Steps After Configuration
After the infrastructure is setup and all the custom environment variables are configured, a few extra steps are required. Before the deployment, the database creation is needed. Additionally, when the boot was successfully done and before allowing users to enter the system, the creation and population of the Elasticsearch Indexes is needed. 
### Database migration
In order to update the database to the latest version of the schema, we add MySQL scripts in the WAR file. They are located at /park-core/src/main/resources/db/migration.
Before deploying the WAR file, a verification over the available scripts should be run in order to check if any modification needs to be apply into the database. If a new script is found, it should be executed (after making a database backup).
In our case, we trust Flyway to do this task. This is an example of the command we use:
```
flyway-3.2.1/flyway migrate -configFile=/var/lib/jenkins/flyway-3.2.1/conf/dev.properties -locations=filesystem:/var/lib/jenkins/jobs/REST_pull_and_compile_from_branch/workspace/park-core/src/main/resources/db/migration
```
Here, we tell Flyway to execute a migration operation with the configuration defined at `/var/lib/jenkins/flyway-3.2.1/conf/dev.properties` and to search for new scripts at `/var/lib/jenkins/jobs/REST_pull_and_compile_from_branch/workspace/park-core/src/main/resources/db/migration`. The property file contains the definition of MySql username, password and URL. The structure of this file must follow the next organization:
```
flyway.url=DB_MYSQL_URL
flyway.user=DB_MYSQL_USER
flyway.password=DB_MYSQL_PASSWORD
```
_Here, you need to replace DB_MYSQL_HOST,DB_MYSQL_PORT, DB_MYSQL_USER and DB_MYSQL_PASSWORD with the proper values and those are the same values you set as custom environment variables._

When executing the first database migration (before the first deployment), the set of scripts will be run resulting in the database creation and the definition of its tables. One of these scripts creates a database with name ‘park’. Therefore, you do not need to worry about its creation. After migration, deployment can be done.

### Elasticsearch reindex
After a successful boot, the next step is the _population of Elasticsearch indexes_. We provide an endpoint that allows executing this process. Triggering this process, Elasticsearch indexes will be filled with the corresponding database data. Therefore, you will need to data into your database. We will provide you a dump of our QA database.

This is the request that produces reindexing: `curl '{Host}/index/reindex' -X POST -d '{}' -H 'indexToken: r31nd3xS3cr3t' -H 'Content-Type: application/json'`

Where `{Host}` needs to the be replaced by the proper value.


### Start up locally (development mode)

##### IMPORTANT - Before 

* Start memcached
* Start elaseticSearch 2.4.0
 ```bash
 $ $ELASTIC_SEARCH_HOME/bin/elasticsearch
 ```
* Start activeMQ 5.12.2
 ```bash
 $ $ACTIVEMQ_HOME/bin/activemq start
 ```
* Start MySql
 ```bash
 $ $MYSQL_HOME/bin/mysqld
 ```
Once all dependent servers are started up, then we need to deploy locally in our Tomcat (7.0.53)
1. Package the app
 ```bash
 $ mvn clean package
 ```
 note: for quick *(but risky)* deploy you can skip tests adding: `-DskipTests=true` 
2. Deploy it locally.
Copy the recently created war file into tomcat's webappps folder and remove the previous one to avoid issues.
Enable also remote debugging (https://wiki.apache.org/tomcat/FAQ/Developing)
  * Windows example.
     ```win batch
     @echo off
    
     echo ">>>>> Copying WAR to Tomcat webapps folder >>>>"
    
     rmdir /s /q $TOMCAT_HOME/webapps/ROOT
     xcopy /y ./target/ROOT.war $TOMCAT_HOME/webapps
    
     echo ">>>>> Starting tomcat (remote debug enabled) >>>>>"
     set "CATALINA_HOME=$TOMCAT_HOME"
     :: jpda enables remote debugging
     $TOMCAT_HOME/bin/catalina.bat jpda start
     exit /b 0
     ```
 * Linux example
   > _To be done_

 * Mac example
   ```bash
   #!/usr/bin/env bash

   #uncommet the following line if tomcat must be stopped before
   #$TOMCAT_HOME/bin/catalina.sh stop

   echo ">>>>> Copying WAR to Tomcat webapps folder >>>>"
   rm -r $TOMCAT_HOME/webapps/ROOT
   cp  ./target/ROOT.war $TOMCAT_HOME/webapps

   echo ">>>>> Starting tomcat >>>>>"

   #if [ -z "$1"  -eq "debug" ]
   if [ "$1" = "debug" ]
     then
       # jpda enables remote debugging
       #$TOMCAT_HOME/bin/catalina.sh jpda start && tail -n 400 -f $TOMCAT_HOME/logs/catalina.out
       $TOMCAT_HOME/bin/catalina.sh jpda start && tail -n 400 -f $TOMCAT_HOME/logs/park.log
     else
       $TOMCAT_HOME/bin/catalina.sh run
   fi

   ```
3. Connect remote debugger from your IDE on port `8000`