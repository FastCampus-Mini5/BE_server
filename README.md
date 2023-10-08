<div style="text-align: center;">

  <img src="https://github.com/FastCampus-Mini5/BE_server/assets/86757234/55cceba1-9349-4336-9439-8fd86e195f24"/>
</div>
<br>

<div align=center><h1> 🐶 프로젝트 소개</h1></div>

> **개발 기간** : 2023. 07. 24. 월 ~ 2023. 08. 10. 목 <br /> > **배포 주소** : [당연하지](https://group5ofcourse.netlify.app/ 'https://group5ofcourseadmin.netlify.app/')<br /> > **백엔드 레포지토리** : [백엔드](https://github.com/FastCampus-Mini5/BE_server) <br /> > **프론트 유저 레포지토리** : [프론트 유저](https://github.com/FastCampus-Mini5/FE-Of-course) <br /> > **프론트 관리자 레포지토리** : [프론트 관리자](https://github.com/FastCampus-Mini5/FE-Of-course-admin)

<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/java 11-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-58FAD0?style=for-the-badge&logo=JPA&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/spring Security-6DB33F?style=for-the-badge&logo=spring Security&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/docker-005F0F?style=for-the-badge&logo=docker&logoColor=white"> 
  <img src="https://img.shields.io/badge/h2-E34F26?style=for-the-badge&logo=h2&logoColor=white"> 
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
          <img src="https://avatars.githubusercontent.com/u/96504592?v=4" width="200px;" alt=""/><br /><sub><b>정현수 : 관리자 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/inyoung0215">
          <img src="https://avatars.githubusercontent.com/u/86757234?v=4" width="200px;" alt=""/><br /><sub><b>황인영 : 관리자 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/gdtknight">
          <img src="https://avatars.githubusercontent.com/u/115003898?v=4" width="200px;" alt=""/><br /><sub><b>신용호 : 유저 </b></sub></a><br />
        </td>
        <td align="center"><a href="https://github.com/YangSooHyun0">
          <img src="https://avatars.githubusercontent.com/u/111266513?v=4" width="200px;" alt=""/><br /><sub><b>양수현 : 유저 </b></sub></a><br />
        </td>
      </tr>
      <tr>
        <td>
          관리자 로그인,<br />관리자 회원가입,<br />관리자 회원관리,<br />페이징
        </td>
        <td>
          연차 / 당직 승인,<br />연차 / 당직 리스트,<br />페이징
        </td>
        <td>
          유저 로그인,<br />유저 회원가입,<br />서버 배포
        </td>
        <td>
          연차 / 당직 신청,<br />연차 / 당직 취소,<br />전체 리스트 캘린더,<br />엑셀 다운로드
        </td>
      </tr>
    </tbody>
  </table>

  </div>
  <div align=center><h1>🐣 프로젝트 시작하는 법</h1></div>

## 1. 사전 준비

### 1-1. Docker 설지

- https://www.docker.com/products/docker-desktop/

### 1-2. .env 파일 생성 및 설정

```dotenv
# SECRET_KEY 는 임의로 설정해도 상관없으나 길이와 형식은 동일해야 합니다.
JWT_SECRET_KEY=7f186169-ea6d-4bb8-80a1-148329b820c1
AES_SECRET_KEY=53B7A77CA2E9BA6CAA901E3A7087E638

# 아래 정보를 바탕으로 docker compose 가 실행됩니다
DB_URL=localhost
DB_NAME=kdtmini5             # DB 이름
DB_PORT=3306                 # DB 포트
DB_USER=kdtmini5             # DB 사용자 ID
DB_PASSWORD=kdtKDTminiMINI5! # DB 사용자 패스워드
DB_TYPE=mysql
DB_DRIVER=com.mysql.cj.jdbc.Driver

MAIL_SENDER_HOST=smtp.naver.com
MAIL_SENDER_PORT=465
MAIL_SENDER_USERNAME=jhss0113@naver.com  # 네이버 ID - 메일 환경 설정에서 POP/IMAP 발송 설정 필요
MAIL_SENDER_PASSWORD=Z26WP98RH64K        # 네이버 2단계 인증 - 애플리케이션별 비밀번호
```

### 1-3. (Option) IntelliJ - EnvFile Plugin

- 해당 플러그인을 사용할 경우 IntelliJ 에서 위에서 작성한 `.env`를 이용하여 환경 변수를 지정할 수 있습니다.
- `.env` 파일로 환경 변수 설정하기

### 1-4. application-dev.yml 내용 직접 수정 (1-3. 미적용시)

- `1-2` 의 내용을 참고하여 `admin` 과 `application` 의 `application-dev.yml` 을 아래와 같이 수정해주세요.
- 도커를 사용하지 않고 `Local Database`를 사용하는 경우에도 해당됩니다.

```yaml
# 다른 부분 생략
spring:
  datasource:
    username: ${DB_USER} # DB_USER 직접 입력
    password: ${DB_PASSWORD} # DB_PASSWORD 직접 입력
    url: jdbc:mysql://localhost:3306/${DB_NAME} # DB_NAME 에 직접 입력. mysql 이라고 가정.
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 1-5. `admin`, `application` 동시 실행시 테스트 데이터 초기화 하나만 활성화

- 테스트 데이터의 내용은 `admin`, `application` 이 동일합니다.
- 둘을 동시에 실행하실 경우 둘 중 하나를 선택하여 `application-dev.yml` 을 수정하여 주세요
- 단, 비활성화된 애플리케이션을 나중에 실행해야 합니다.

```yaml
spring: 
  jpa:
    hibernate:
      ddl-auto: none # none 으로 수정
      naming:
        physical-strategy: org.hibernate.boot.model.naming.  CamelCaseToUnderscoresNamingStrategy
    properties:
      hibernate:
        format_sql: true
        show_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect
    defer-datasource-initialization: false # false 로 수정
    open-in-view: true

## 아래의 내용을 삭제
  sql:
    init:
      mode: always
      data-locations: classpath:test/data.sql
```
</div>
  <div align=center><h1>🖥 기능 소개</h1></div>
</div>
  <div align=center><h3>🧙🏻‍♂️ 로그인, 회원가입 페이지 구성</h3></div> 
  
<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/86c4a636-6e00-4c20-803e-fd58b5d89e0f" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 로그인 페이지
  </div>
</th>
 
 <th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/b3c430d9-d7d6-45d8-bb85-078c41831639" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 회원가입 페이지
  </div>
</th>
</table>

<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/f6a1ca67-7338-4f16-bc4a-f417c4b1db2f" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 프로필 수정 페이지
  </div>
</th>
 
 <th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/1cfe99ae-f5c4-4481-a2f8-7d1a6aa1f7f1" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 비밀번호 재설정 페이지
  </div>
</th>
</table>

<br/>

---
<br/>

</div>
  <div align=center><h3>🧙🏻‍♀️ Side Bar - 네비게이션 페이지 구성</h3></div> 

<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/1cd9afb1-0cfa-44a5-a1c3-27e9bbf15fc0" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 홈 페이지 
  </div>
</th>

<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/52ea23ad-8f01-4996-8793-27a72b52a39e" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 연차 신청 페이지
  </div>
</th>
</table>

<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/088b632c-4b33-4f5f-ad35-71d621c1a62a" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 당직 신청 페이지
  </div>
</th>
 
 <th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/ffb2f0e5-ee09-498a-bbe1-56a38ab4b22b" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
 내 일정보기 페이지
  </div>
</th>
</table>
<br/>

---
<br/>

</div>
  <div align=center><h3>🧙🏻 관리자 권한 페이지 구성</h3></div> 

<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/d72d0d62-03e3-4d44-af5e-9accaf9a8d45" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
관리자 로그인 페이지
  </div>
</th>
 
 <th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/6f9255bb-6f60-4b57-b6bd-47ac654e33d2" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
유저 리스트 페이지 
  </div>
</th>
 </table>

 <table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/8b676bef-7452-425e-9cdd-5cca4a977f57" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
연차 요청 리스트 페이지
  </div>
</th>

<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/bd33acb8-cc8f-4bc2-a261-9cb2c26fc5a5" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
연차 리스트 페이지
  </div>
</th>
</table>

<table>
<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/0e0df96a-8bbf-419b-90c6-1cd5896cffae" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
당직 요청 리스트 페이지
  </div>
</th>

<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
    <img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/04ea1474-2507-478e-8eb7-84e45147fd2c" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
  </div>
  <div align="center">
당직 리스트 페이지
  </div>
</th>
</table>

<br/>

</div>
  <div align=center><h1> ERD </h1></div> 

<th style={{width: "25%"}}>
  <div style={{width: "50%"}}>
<img src="https://github.com/FastCampus-Mini5/backend_server/assets/111266513/b0c29bc1-c8ad-4fba-9f76-3955aa67ef3a" style={{width: "50%", height: "50%", objectFit: "contain"}}/>
     </div>
  </th>
<br/>


### 📝 License
Copyright © 2023 [당연하지](https://github.com/FastCampus-Mini5/BE_server).<br/>
This project is [MIT](https://github.com/wupitch/wupitch-server/blob/main/LICENSE) licensed.
