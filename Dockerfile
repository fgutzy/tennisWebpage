 FROM openjdk:11
WORKDIR /
ADD target/demo-0.0.1-SNAPSHOT.jar app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8090

ENV DB_URL=jdbc:mysql://localhost:3306/tennis_db
ENV DB_USERNAME=root
ENV DB_PASSWORD=rootpassword

CMD java -jar -Dspring.profiles.active=prod app.jar
