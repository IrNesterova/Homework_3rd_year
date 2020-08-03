FROM java:8
#ADD target/zuul-0.1.jar zuul-0.1.jar
#RUN mvn clean install
COPY target/zuul-0.1.jar zuul-0.1.jar
EXPOSE 8080
#CMD ["cd", "ZuulService", "&&", "java", "-jar", "*.jar"]
ENTRYPOINT ["java", "-jar", "zuul-0.1.jar"]
