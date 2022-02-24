
FROM openjdk:8-jdk-alpine
#VOLUME /tmp
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
ADD target/*.jar app.jar
EXPOSE 7705
ENTRYPOINT ["java","-jar","app.jar"]
#ADD src/main/resources/application-staging.properties application-staging.properties
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.config.location=classpath:file:/application-staging.properties -jar /app.jar" ]
