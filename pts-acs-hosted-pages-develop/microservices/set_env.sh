#!/bin/sh
# Use this file to set environment specific configuration.
# This is a template which lists the variables that can be set, but should not set them to a value.
# Edit the file locally to configure your own environment and use git stash to save your local copy to reapply
# !!!! Very important note !!!!
# !!!! DO NOT COMMIT local changes to GitHub unless adding a new environment variable!!!!
# !!!! Failure to observe this rule should result in PR rejection !!!!
#
# Environment variables
#
# This variable defines the secret key used to sign Json Web Tokens in this environment.
# It must a minimum of 512 bytes long (64 characters).
export JWT_SECRETKEY_FORM=""
# Set the full name (including path) of the PID file written by Spring Boot
# If no value is provided the default will be './acs-microservices.pid'
export PID_FILE=""
# This variable sets the Admin UI password for this environment
export ADMIN_UI_PASSWORD=""
# This variable defines the Spring profile to use when running the Microservices application.
# This should correspond to a variant of the application.yml file.
# For example to use the config defined in application-integrationtest.yml set this value to "integrationtest"
# Comment it out to use the default profile
export SPRING_PROFILES_ACTIVE=""
# Mongo Db configuration.  Comment out any values not required
#export SPRING_DATA_MONGODB_HOST=""
#export SPRING_DATA_MONGODB_DATABASE=""
#export SPRING_DATA_MONGODB_PORT=""
#export SPRING_DATA_MONGODB_PASSWORD=""
#export SPRING_DATA_MONGODB_USERNAME=""
#
# Add any additional environment variables to override Spring config here.
# Basic guidelines:
# All uppercase
# Replace period (.) with underscore (_)
# Remove hyphens
# Example spring.main.log-startup-info becomes SPRING_MAIN_LOGSTARTUPINFO
# See here for documentation: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-relaxed-binding-from-environment-variables

# Spring Boot Admin settings - set these values to use SB Admin
# The URL of the Spring Boot Admin server
export SPRING_BOOT_ADMIN_URL=""
# The username for the SpringBoot Admin server
export SPRING_SECURITY_USER_NAME="ptsacs"
# The password for the SpringBoot Admin server
export SPRING_SECURITY_USER_PASSWORD=""
# Username and password used by Spring Boot Admin to authenticate with Actuator endpoints
export SB_ADMIN_USER=${SPRING_SECURITY_USER_NAME}
export SB_ADMIN_PASSWORD=${SPRING_SECURITY_USER_PASSWORD}

# Set the path for the log file here - not including trailing slash or filename
export SPRING_LOGGING_PATH=""
