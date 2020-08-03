FROM java:8
#ADD target/eureka-0.1.jar eureka-0.1.jar
#RUN mvn clean install
COPY target/eureka-0.1.jar eureka-0.1.jar
EXPOSE 9000
#CMD ["cd", "EurekaService", "&&", "java", "-jar", "*.jar"]
ENTRYPOINT ["java", "-jar", "/eureka-0.1.jar"]
