# Sử dụng image OpenJDK phiên bản 21 với slim base image
FROM openjdk:21-jdk-slim
# Cài đặt tzdata để thiết lập múi giờ
RUN apt-get update && apt-get install -y tzdata
# Đặt múi giờ
ENV TZ=Asia/Ho_Chi_Minh
# Thiết lập múi giờ
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
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
# Expose port 8085 (thay đổi số cổng tương ứng với ứng dụng của bạn)
EXPOSE 8085
# Chạy ứng dụng khi container khởi động
CMD ["java", "-jar", "target/payment1-service-0.0.1-SNAPSHOT.jar"]
