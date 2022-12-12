# prolog - backend
개발자 블로그 개발 프로젝트

## 목차
1. [사용 기술](#사용-기술)   
  1.1. [기술 채택 이유](#기술-채택-이유)   
2. [주요 기능](#주요-기능)
3. [DB 구조](#db-구조)
4. [시스템 아키텍쳐](#시스템-아키텍쳐)
5. [ISSUE](#issue)   
  5.1. [댓글 삭제 시나리오](#상위-댓글-삭제-시나리오에서-대댓글-처리-방법)   
  5.2. [Authentication](#사용자-인증-및-권한-부여)   
  5.3. [로깅](#logging)   
  5.4. [Request Body](#logging-시-request-body-접근-문제)   
  5.5. [DataJpaTest](#테스트코드-error)   
  5.6. [브랜치 관리](#repository-branch-관리)   
  5.7. [에러처리](#전역-exception-handler-추가)

## 사용 기술
* SpringBoot 2.6.7
* SpringDataJPA
* SpringSecurity
* JWT
* QueryDsl
* MySQL
* Redis
* H2DB (for only test)

### 기술 채택 이유
* SpringBoot : OOP, DI와 AOP를 통한 서버의 높은 안정성, 성능 그리고 설계가 용이하다는 점이 큰 장점으로 다가와 선택
* JWT : 사용자 인증 및 권한 부여를 위한 용도
* QueryDsl : SpringDataJPA만으로는 복잡한 쿼리를 표현하거나 동적 쿼리를 구성할 수 없기 때문
* MySql : 회원 정보와 게시글, 레이아웃 등 데이터 무결성과 정합성이 중요한 비즈니스 로직을 다루기 때문
* Redis : JWT 토큰을 저장하고 expire 속성을 이용한 자동 만료와 회원 기능에 대해 빠른 응답을 기대하여 사용
* H2DB : 테스트코드를 실행용 + MySql의 사용은 기존 데이터나 스키마에 영향이 갈 수 있으므로 선택

## 주요 기능
1. 회원 관리 (CRUD)
2. 게시글 관리 (CRUD)
3. 레이아웃 관리 (CRUD)
4. 레이아웃 틀 관리
5. 파일 업로드/삭제
6. 게시글 목록 조회 (전체/최근/내 글/좋아요한 글)
7. 게시글 검색
8. 카테고리 관리
9. 통계 기능
   
## DB 구조
![Prolog_DB](https://user-images.githubusercontent.com/77658870/204887305-00d62724-1a73-458e-afea-fbcc21fd4b56.png)
## 시스템 아키텍쳐
![시스템 아키텍쳐](https://user-images.githubusercontent.com/77658870/205274883-9e02ae84-61ea-4667-ba70-50357bc95d2c.png)
   
## ISSUE
### 상위 댓글 삭제 시나리오에서 대댓글 처리 방법
  * 기존 DB 설계상으로는 상위 댓글 삭제 시 FK 참조 에러가 발생
  * 상위 댓글 삭제를 위해 하위 대댓글을 모두 삭제하는 것은 기능면에서도, cost 면에서도 나쁘다고 판단
  * DB 설계를 수정 (Comment Table block column 추가)
  * 시나리오 변경 (상위 댓글 삭제 시 해당 댓글의 block column을 수정하고 front에서 필터링하여 출력)
### 사용자 인증 및 권한 부여
  * 잘못된 API 설계로 인해 회원pk가 URL에 다수 노출
  * 보안상 큰 문제이기 때문에 API 수정이 필요
  * 이를 위해 JWT 도입 및 API 수정 (회원pk 삭제 후 JWT token을 header에 추가)
### Logging
  * FE팀과의 연동 과정에서 서버를 구동하면서 누가 어떤 요청을 보냈는지, 에러가 발생했을 때 요청 데이터는 올바르게 전송했는지 확인할 방법이 없다는 불편함을 느낌
  * 본 프로젝트의 배포와 그 이후 관리를 위해서라도 로그 작성의 필요성을 절감했다
  * 배포상태에서는 로그를 콘솔에 출력할 뿐만 아니라 파일로 저장할 필요가 있기 때문에 XML 설정 파일을 추가
  * 최초 Logging은 Interceptor로 작성하려고 했으나 아래의 4번 ISSUE로 인해 Filter 방식으로 구현
### Logging 시 Request Body 접근 문제
  * 요청 데이터도 함께 Log를 남기려 하였으나 Request Body는 InputStream이기 때문에 한 번 읽어들이면 데이터가 사라져버린다
  * 결국 이후 Controller에서 @RequestBody를 매핑하지 못하여 에러가 발생하는 것을 확인
  * 이를 해결하기 위해 구현을 Interceptor -> Filter로 변경
  * Filter에서 Request를 인자로 Custom Http Wrapper 객체를 생성하고 데이터를 여러번 참조 가능하도록 byte[]타입으로 버퍼에 복사
  * 이후 Wrapper 객체를 doFilter()의 Request 파라미터로 이용하여 DispatcherServlet에 전달
   
[LogFilter 추가 commit history](https://github.com/bodyMist/prolog/commit/2b5168003b197b3a0861c29f66d6595a09566bf2)
   
### 테스트코드 Error
  * Redis를 이용하는 JWT와 소셜Email 인증 Repository의 bean 생성 실패로 인한 @DataJpaTest annotation 이용 불가
  * 두 Repository 클래스가 JpaRepository를 상속받고 @Repository annotation을 이용 중이며 클래스는 @Entity가 아닌 @RedisHash가 적용
  * DataJpaTest는 JPA 관련 테스트 설정을 로드하기 때문에 Bean Creation Error가 발생
  * 해당 두 클래스 EmailAuthTokenRedisRepository와 JwtAuthTokenRepository의 상속을 CrudRepository로 변경 및 @Repository annotation 삭제
  * 이후 DataJpaTest 단위테스트의 정상 작동을 확인
   
[수정 이력](https://github.com/bodyMist/prolog/commit/2b21084db07aac49638a8c796644de132cfcc56c)  
[테스트코드 작성](https://github.com/bodyMist/prolog/commit/ad7182d61a1d82571c223429cc9d6eb32728244c)

### Repository branch 관리
  * 기능 구현 및 연동 완료 이후 BE 코드를 유지보수 및 관리할 방법을 강구
  * 개발 단계에서는 contributors 모두 main branch에 direct push를 했으나 1.0v release 부터는 기존의 방법이 위험도가 높다고 판단
  * 0.1v의 테스트 배포를 시작하며 동시에 본 레포지토리에도 까다로운 branch 관리를 적용
  * 다음과 같은 이유들로 Gitflow 전략을 채택
    * release 이후, hotfix와 기능 추가에 대해 다른 팀원들의 이해를 돕기 위해 추적이 쉬워야한다
    * BE팀은 각자가 분담하고 있는 파트가 확연하게 구분되어 있지만 Logging과 설정파일 같이 공용으로 사용하는 파일에 대해 주의와 독립적 환경을 보장하기 위함
    * 무분별한 코드의 추가를 막기 위해 branch protection rule을 추가하여 contributor의 code review와 승인을 통해 merge를 허가
    
### 전역 Exception Handler 추가
  * 개발 단계에서 객체 반환과 @RestController의 사용으로 인해 에러 발생시에도 200Http 으로 반환
  * Controller Layer에서 반복적인 try-catch 사용으로 인해 가독성이 낮고 유지보수가 어렵다
  * 코드 리팩토링의 필요성을 느끼고 @ControllerAdvice와 @ExceptionHandler를 사용
  
[에러 처리 방식 변경 코드](https://github.com/bodyMist/prolog/commit/50184b5ea3a3f0b0191092749a0dc69be97fb6b9#diff-6dbc9c40bd40dee5384a1f26cb24e26eee37eed6f01e9f44a751174fe9ca2849)
