# BE_server
<div style="text-align: center;">
  <img src="https://github.com/FastCampus-Mini5/BE_server/assets/86757234/55cceba1-9349-4336-9439-8fd86e195f24"/>
</div>

<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/java 11-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring Security-6DB33F?style=for-the-badge&logo=spring Security&logoColor=white">
  
  <br>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-58FAD0?style=for-the-badge&logo=JPA&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/docker-005F0F?style=for-the-badge&logo=docker&logoColor=white"> 
  <img src="https://img.shields.io/badge/h2-E34F26?style=for-the-badge&logo=h2&logoColor=white"> 
  <img src="https://img.shields.io/badge/-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white">
  <br>
  
  <div align=center><h2>💬 Communication</h2></div>
  <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white">
  <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/Zoom-2D8CFF?style=for-the-badge&logo=Zoom&logoColor=white">
  <br>

  <div align=center><h1>👨‍👩‍👧‍👦 팀원 역할</h1></div>
  <table>
    <tbody>
      <tr>
        <td align="center"><a href="https://github.com/hyunsb">
          <img src="https://avatars.githubusercontent.com/u/96504592?v=4" width="100px;" alt=""/><br /><sub><b>정현수 : 관리자 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/inyoung0215">
          <img src="https://avatars.githubusercontent.com/u/86757234?v=4" width="100px;" alt=""/><br /><sub><b>황인영 : 관리자 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/gdtknight">
          <img src="https://avatars.githubusercontent.com/u/115003898?v=4" width="100px;" alt=""/><br /><sub><b>신용호 : 유저 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/YangSooHyun0">
          <img src="https://avatars.githubusercontent.com/u/111266513?v=4" width="100px;" alt=""/><br /><sub><b>양수현 : 유저 </b></sub></a><br />
        </td>
      </tr>
      <tr>
        <td>
          관리자 로그인,<br />관리자 회원가입,<br />관리자 회원관리,<br />페이징
        </td>
        <td>
          연차, 당직 승인,<br />연차, 당직 리스트,<br />페이징
        </td>
        <td>
          유저 로그인,<br />유저 회원가입,<br />서버 배포
        </td>
        <td>
          연차, 당직 신청,<br />연차, 당직 취소,<br />전체 리스트 캘린더,<br />엑셀 다운로드
        </td>
      </tr>
    </tbody>
  </table>
  <div align=center><h1>🐣 프로젝트 시작하는 법</h1></div>
  <div><h4>★본 프로젝트는 관리자 서버와 게시판 서버가 분리되어 있어 프로그램을 실행시키기 위해 설정이 필요합니다★</h4></div>
  </div>
  <div align=center><h3>1. yml파일 설정하기</h3></div>
  <div align=center><h4>1-1. module-board의 yml파일 설정하기 (게시판 실행시)</h4></div>

  ```yaml
server:
  port: 8080
  servlet:
    encoding:
      charset: utf-8
      force: true

spring:
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
  datasource:
    username: # 본인의mysql 데이터 name
    password: # 본인의mysql 데이터 password
    url: # 본인의 mysql 데이터 url
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        ddl-auto: update
        format_sql: true
file:
  dir: C://Users//User//IdeaProjects//normal-board//module-board//src//main//resources//static//asset//upload//
# 파일 경로는  //normal-board 전까지 본인의 프로젝트 경로로 맞게 설정하세요
```
<div align=center><h4>1-2. module-admin의 yml파일 설정하기 (관리자 실행시)</h4></div>

```yaml
server:
  port: 8081
  servlet:
    encoding:
      charset: utf-8
      force: true
spring:
  datasource:
    username: # 본인의mysql 데이터 name
    password: # 본인의mysql 데이터 password
    url: # 본인의 mysql 데이터 url
    driver-class-name: com.mysql.cj.jdbc.Driver
  thymeleaf:
    prefix: classpath:templates/thymeleaf
    suffix: .html
    mode: HTML
    check-template-location: true
    cache: false
  mail:
    host: smtp.gmail.com
    port: 587
    username: # 보내는이 이름
    password: # 본인 설정 gmail 인증 비밀번호
    properties:
      mail:
        smtp:
          auth: true
          timeout: 5000
          starttls:
            enable: true
mybatis:
  mapper-locations:
    - classpath:mapper/**.xml # classpath -> resource 폴더를 찾음.
  configuration:
    map-underscore-to-camel-case: true # under_score 형식을 카멜표기법으로 변환
file:
  dir: C://Users//User//IdeaProjects//normal-board//module-board//src//main//resources//static//asset//upload//report
# 파일 경로는  //normal-board 전까지 본인의 프로젝트 경로로 맞게 설정하세요
```
<div align=center><h3>2. 포트 다르게 잡기 <br> edit configurations 화면에서 Modify options클릭 -> add VM Options추가<br>-Dserver.port=8080 (board 앱 쪽)<br>-Dserver.port=8081 (admin 앱 쪽)</h3></div>

![image](https://github.com/Spring-Board-Toy3/normal-board/assets/69192549/84d5cefe-112f-4caa-98bb-886e83b1324b)

<div align=center>
  <h3>3. 신고하기 img경로 폴더 생성 : module-board의 다음 경로에 report폴더를 생성해주세요</h3>

  ![image](https://github.com/Spring-Board-Toy3/normal-board/assets/69192549/2083f0a0-1fa5-4562-ad6c-c18748f645c2)
</div>
  <div align=center><h1>🖥 기능 소개</h1></div>
  <div align=center><h3>1. 회원가입(/join) & 로그인(/login)<br>- 회원가입시 아이디 중복체크</h3></div> 
<div align=center>
