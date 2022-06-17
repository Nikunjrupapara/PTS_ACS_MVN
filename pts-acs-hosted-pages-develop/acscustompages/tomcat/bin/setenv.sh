#!/bin/bash
# Set the path (not including filename) to use for Spring Boot's application log file
export LOG_PATH=""
# Set the name of the Spring profile to use: one of development, qa or production
export SPRING_PROFILES_ACTIVE="development"

# Connect JMX metrics to logstash
#export CATALINA_OPTS=" -Dcom.sun.management.jmxremote.port=9000 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
# Uncomment the following line to debug logging configuration
#export CATALINA_OPTS="$CATALINA_OPTS -Dlogback.statusListenerClass=ch.qos.logback.core.status.OnConsoleStatusListener"

# MongoDB configuration - uncomment and set values if not using a local Mongo database
#export SPRING_DATA_MONGODB_HOST=""
#export SPRING_DATA_MONGODB_DATABASE=""
#export SPRING_DATA_MONGODB_PORT=""
#export SPRING_DATA_MONGODB_PASSWORD=""
#export SPRING_DATA_MONGODB_USERNAME=""

# Spring Boot Admin settings - uncomment and set these values to use SB Admin
# The base URL of the Tomcat server the application is running in, e.g. http://localhost:8081
#export SPRING_BOOT_ADMIN_CLIENT_INSTANCE_SERVICEBASEURL=""
# The password to use for the Spring Boot Admin server
#export SPRING_BOOT_ADMIN_CLIENT_PASSWORD=""
# The username to use for the Spring Boot Admin server
#export SPRING_BOOT_ADMIN_CLIENT_USERNAME=""
# The URL of the Spring Boot Admin server
#export SPRING_BOOT_ADMIN_URL=""
