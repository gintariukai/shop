FROM openjdk:19
WORKDIR /usr/src/shop-client
COPY ./target/shop-client-*.jar .
CMD java -jar < find shop-*.jar