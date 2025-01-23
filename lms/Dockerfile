FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/lms-0.0.1-SNAPSHOT.jar app.jar

ENV DB_HOST=db_host_placeholder
ENV DB_PORT=3306
ENV DB_NAME=db_name_placeholder
ENV DB_USERNAME=db_user_placeholder
ENV DB_PASSWORD=db_password_placeholder
ENV ACCESS_SECRET_KEY=placeholder
ENV REFRESH_SECRET_KEY=placeholder
ENV ACCESS_VALIDITY_SECONDS=600
ENV REFRESH_VALIDITY_SECONDS=86400
ENV AWS_ACCESS_KEY_ID=placeholder
ENV AWS_SECRET_ACCESS_KEY=placeholder
ENV AWS_REGION=placeholder
ENV AWS_S3_BUCKET=placeholder
ENV REDIS_HOST=placeholder

ENTRYPOINT ["java", "-jar", "app.jar"]