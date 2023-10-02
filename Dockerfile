FROM openjdk:17
ADD *.jar app.jar

MAINTAINER Andrew Skrypka <skrpk.a.v@gmail.com>

LABEL image.name="projectX"

EXPOSE 8080:8080

ENTRYPOINT ["java", "-Xmx200m", "-jar","app.jar"]