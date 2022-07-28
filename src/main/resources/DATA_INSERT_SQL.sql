# 사용자 1
INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password)
VALUES ('sky834459', 0, 'sky834459@gmail.com', '', '안냥하세요', '김태훈', '판교', '8344');

# 카테고리 3
INSERT INTO categories(name, user_user_id) VALUES ('개발용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('취미용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('전시용', 1);

# 레이아웃틀 3
INSERT INTO molds(name, user_user_id) VALUES ('기본 틀', 1);
INSERT INTO molds(name, user_user_id) VALUES ('복합 틀', 1);
INSERT INTO molds(name, user_user_id) VALUES ('연관 틀', 1);

# 레이아웃 6
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (1, 10.245, 34.349, '레이아웃 설명1', 20, 1, 30, 1);
INSERT INTO contexts(text, layout_id) VALUES ('상세 내용', 1);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (2, 20.121, 34.349, '레이아웃 설명2', 20, 0, 30, 1);
INSERT INTO images(layout_id, sequence, url) VALUES (2, 1, 'https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png');
INSERT INTO images(layout_id, sequence, url) VALUES (2, 2, 'https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png');
INSERT INTO images(layout_id, sequence, url) VALUES (2, 3, 'https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png');

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (3, 33.245, 34.349, '레이아웃 설명3', 20, 1, 30, 2);
INSERT INTO codes(layout_id, code, code_explanation, code_type) VALUES (3, 'Generic is cool', '자바 제네릭 쵝오', 'JAVA');
INSERT INTO codes(layout_id, code, code_explanation, code_type) VALUES (1, 'C Struct is old', 'c언어의 구조체에 대한 설명', 'C');
INSERT INTO codes(layout_id, code, code_explanation, code_type) VALUES (2, 'Cpp support Class', '개쩌는 cpp는 개쩌는 클래스 기반 프로그래밍이 가능하다', 'CPP');

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (4, 49.245, 34.349, '레이아웃 설명4', 20, 0, 30, 2);
INSERT INTO hyperlinks(url, layout_id) VALUES ('상세 내용', 4);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (5, 53.245, 34.349, '레이아웃 설명5', 20, 1, 30, 3);
INSERT INTO mathematics(context, layout_id) VALUES ('상세 내용', 5);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (6, 65.245, 34.349, '레이아웃 설명6', 20, 0, 30, 3);
INSERT INTO videos(url, layout_id) VALUES ('상세 내용', 6);

# 게시글 6
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '1이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '2이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '3이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '4이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '5이것은 테스트용이예요', 3, 3, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '6이것은 테스트용이예요', 3, 3, 1);

# 태그 6
INSERT INTO tags(name) VALUES ('딥러닝');
INSERT INTO tags(name) VALUES ('react');
INSERT INTO tags(name) VALUES ('typescript');
INSERT INTO tags(name) VALUES ('MVC');
INSERT INTO tags(name) VALUES ('웹프로그래밍');
INSERT INTO tags(name) VALUES ('syn flooding');

INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (1,1);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (1,2);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (1,3);

INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (2,3);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (2,4);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (2,5);

INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (3,1);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (3,5);
INSERT INTO posts_and_tags(post_post_id, tag_tag_id) VALUES (3,6);

# 첨부파일 1
INSERT INTO attachments(extension, name, url, post_post_id) VALUES ('txt', '실험 파일', '', 1);
# 조회 10
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 1);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 1);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 1);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 1);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 1);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 2);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 2);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 2);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 3);
INSERT INTO hits(time, post_post_id) VALUES (CURRENT_TIME, 3);
# 좋아요 1
INSERT INTO likes(post_post_id, user_user_id) VALUES (1,1);

INSERT INTO comments(context, time, post_post_id, user_user_id)
VALUES ('이것은 댓글입니다', CURRENT_TIME, 1, 1);
INSERT INTO comments(context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES ('요건 대댓글입니다', CURRENT_TIME, 1, 1, 1);