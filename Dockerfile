FROM maven:3.6.3-openjdk-11

COPY . ./

EXPOSE 80

CMD ["mvn", "spring-boot:run"]
