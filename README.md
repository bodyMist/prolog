# prolog - backend
개발자 블로그 개발 프로젝트

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
* Spring : 
* JWT : 사용자 인증 및 권한 부여를 위한 용도
* QueryDsl : SpringDataJPA만으로는 복잡한 쿼리를 표현하거나 동적 쿼리를 구성할 수 없기 때문
* MySql : 회원 정보와 게시글, 레이아웃 등 데이터 무결성과 정합성이 중요한 비즈니스 로직을 다루기 때문
* Redis : JWT 토큰을 저장하고 expire 속성의 자동 만료 기능과 회원 기능에 대해 빠른 응답을 기대하여 사용
* H2DB : 테스트코드를 실행용 및 MySql의 사용은 기존 데이터나 스키마에 영향이 갈 수 있으므로 선택

## 주요 기능
1. 회원 관리 (CRUD)
2. 게시글 관리 (CRUD)
3. 레이아웃 관리 (CRUD)
4. 레이아웃 틀 관리 (CRD)
5. 파일 업로드/삭제
6. 게시글 목록 조회 (전체/최근/내 글/좋아요한 글)
7. 게시글 검색
8. 카테고리 관리
9. 통계 기능

## DB 구조
![Prolog_DB](https://user-images.githubusercontent.com/77658870/204887305-00d62724-1a73-458e-afea-fbcc21fd4b56.png)

## ISSUE
1. 상위 댓글 삭제 시나리오에서 대댓글 처리 방법
    * 기존 DB 설계상으로는 상위 댓글 삭제 시 FK 참조 에러가 발생
    * 상위 댓글 삭제를 위해 하위 대댓글을 모두 삭제하는 것은 기능면에서도, cost 면에서도 나쁘다고 판단
    * DB 설계를 수정 (Comment Table block column 추가)
    * 시나리오 변경 (상위 댓글 삭제 시 해당 댓글의 block column을 수정하고 front에서 필터링하여 출력)
2. 사용자 인증 및 권한 부여
    * User pk 를 
3. Logging
3. branch 관리
5. 테스트코드 Error
    * Redis를 이용하는 JWT와 소셜Email 인증 Repository의 bean 생성 실패로 인한 @DataJpaTest annotation 이용 불가
    * 두 Repository 클래스가 JpaRepository를 상속받고 @Repository annotation을 이용 중이며 클래스는 @Entity가 아닌 @RedisHash가 적용
    * DataJpaTest는 JPA 관련 테스트 설정을 로드하기 때문에 Bean Creation Error가 발생
    * 해당 두 클래스 EmailAuthTokenRedisRepository와 JwtAuthTokenRepository의 상속을 CrudRepository로 변경 및 @Repository annotation 삭제
    * 이후 DataJpaTest가 정상 작동
    
    
    
    
