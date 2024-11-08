FROM openjdk:latest
WORKDIR /app
COPY . /app

RUN javac MessagePassing.java

CMD ["java", "MessagePassing"]
