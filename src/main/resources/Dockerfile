FROM openjdk:8-jdk

ENV TZ=Asia/Shanghai

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY two_state_interactive-0.0.1-SNAPSHOT.jar /home/two_state_interactive.jar

CMD ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/two_state_interactive.jar","--spring.profiles.active=prod"]