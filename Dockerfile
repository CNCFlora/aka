FROM diogok/java8

WORKDIR /opt
RUN mkdir -p /var/data/aka
CMD ["java","-server","-DDATABASE=/var/data/aka/aka.db","-XX:+UseConcMarkSweepGC","-XX:+UseCompressedOops","-XX:+DoEscapeAnalysis","-jar","aka.jar"]
VOLUME /var/data/aka

ADD target/aka-0.0.1-standalone.jar /opt/aka.jar

