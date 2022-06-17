### ElasticStack Setup for ACS Hosted Pages and Microservices

This document describes how log file data is transferred from the individual servers running the different components
into the ElasticStack for aggregation and visualisation.

The flow of data is described below.

The SpringBoot app running on each server writes two log files:

* Microservices API
  * a regular text based log file to ```/var/springboot/ptsacs/log```
  * a JSON encoded log file to ```/var/springboot/ptsacs/log/logstash```
* Hosted Pages
  * a regular text based log file to ```/var/tomcat/ptsacs/log```
  * a JSON encoded log file to ```/var/tomcat/ptsacs/log/logstash```
  
Text based log files are configured as follows:
* use a Size and Time Based Rolling File Appender
* daily file rolling
* rolled files are automatically Gzipped
* max file size of 100Mb
* total file size of 6.75Gb
* up to 30 days file retention.

JSON files for Logstash are configured as follows:

* Size and Time Based Rolling File Appender
* daily file rolling
* max file size of 100Mb
* total file size of 2Gb
* up to 7 days file retention
* these files are not compressed on roll over as they are processed by Filebeat and sent to Logstash for indexing, which
  is also why the file retention period is shorter
  
SpringBoot logging configuration is defined in
the file ```src/main/resources/logback-spring.xml``` for each application.

Filebeat is configured on each app server to parse the log files in the ```logstash``` directory and forward their
content to the ```logstash``` server for the environment.

The Logstash server includes a pipeline which receives the log file data and injects it into ElasticSearch.  The data
in ElasticSearch is written to an index matching the pattern ```microservices-logs-%{+YYYY.MM.dd}```,
e.g. ```microservices-logs-2021.02.17```.

After that, the data is indexed in ElasticSearch and made available for visualisation in Kibana.

Templates for the configuration files for ElasticStack are stored in the structure below in GitHub:

```
+-+ elasticstack - README.md (this document)
  |
  +--filebeat -+
  |            |
  |            +-- hostedpages - filebeat.yml (Filebeat config for hosted pages app)
  |            |
  |            +-- microservices - filebeat.yml (Filebeat config for microservices app)
  |
  +--logstash -+-- pipelines.yml (Logstash pipeline configurations)
               |
               +-- monitor -+
                            |
                            +-- microservices-logs.conf (Logstash configuration for Microservices and Hosted Pages logs)
```
To deploy to a new environment simply edit the appropriate ```filebeat.yml``` file as follows:
* In the```filebeat.inputs``` section check that the correct path is set for the input with type ```log```
* set the Kibana host details in the section ```setup.kibana```
* set the Logstash details in the section ```output.logstash```

If Filebeat is not using its default port number (5044) then you will need to change the port number in the Logstash
```microservices-logs.conf``` file accordingly.

