FROM adoptopenjdk/openjdk11:alpine

COPY target/packet-publisher-1.0.jar /

EXPOSE 8280

CMD java -jar packet-publisher-1.0.jar
