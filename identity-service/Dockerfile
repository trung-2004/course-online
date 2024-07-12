# Sử dụng image OpenJDK phiên bản 21 với slim base image
FROM openjdk:21-jdk-slim
# Cài đặt Maven
RUN apt-get update && apt-get install -y maven
# Tạo thư mục /app trong container
RUN mkdir /app
# Sao chép các file pom.xml và source code vào /app trong container
COPY pom.xml /app
COPY src /app/src
# Thiết lập thư mục làm việc mặc định là /app
WORKDIR /app
# Build dự án bằng Maven
RUN mvn clean package -DskipTests
# Expose port 8083 (nếu cần thiết, thay đổi số cổng tương ứng với ứng dụng của bạn)
EXPOSE 8080
# Chạy ứng dụng khi container khởi động
CMD ["java", "-jar", "target/identity-service-0.0.1-SNAPSHOT.jar"]
