# 사용자 3
INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password, sns, social_key)
VALUES ('sky834459', 0, 'sky834459@gmail.com', '', '안냥하세요', '김태훈', '판교', '8344', 0, null);
INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password,  sns, social_key)
VALUES ('IntegerIsNullable', 0, 'IntegerIsNullable@naver.com', '', '안냥하세요', '김정수', 'int', '0000', 0, null);
INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password,  sns, social_key)
VALUES ('javaJesus', 0, 'javaJesus@gmail.com', '', '안냥하세요', '이자바', '객체지향만세', '1234',0, null);


# 카테고리 3 + 하위 3
INSERT INTO categories(name, user_user_id) VALUES ('개발용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('취미용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('전시용', 1);

INSERT INTO categories(name, user_user_id, upper_id) VALUES ('React', 1, 1);
INSERT INTO categories(name, user_user_id, upper_id) VALUES ('Game', 1, 2);
INSERT INTO categories(name, user_user_id, upper_id) VALUES ('워크숍', 1, 3);

# 레이아웃틀 2
INSERT INTO molds(name, user_user_id) VALUES ('기본 틀', 1);
INSERT INTO molds(name, user_user_id) VALUES ('복합 틀', 1);

# 게시글 14
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '1이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '2이것은 테스트용이예요', 1, 2, 1);
INSERT INTO posts(time, title, category_category_id, user_user_id)
VALUES (CURRENT_TIME, '3이것은 테스트용이예요', 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '4이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '5이것은 테스트용이예요', 1, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '6이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '7이것은 테스트용이예요', 1, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '8이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '9이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '10이것은 테스트용이예요', 1, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '11이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '12이것은 테스트용이예요', 1, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '13이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '14이것은 테스트용이예요', 1, 2, 1);

# 레이아웃 6
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width, mold_mold_id)
VALUES (1, 10.245, 34.349, '레이아웃 설명1', 20, 30, 1);
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width, mold_mold_id)
VALUES (2, 20.121, 34.349, '레이아웃 설명2', 20, 30, 1);
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width, mold_mold_id)
VALUES (3, 33.245, 34.349, '레이아웃 설명3', 20, 30, 2);
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width)
VALUES (4, 49.245, 34.349, '레이아웃 설명4', 20, 30);
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width)
VALUES (5, 53.245, 34.349, '레이아웃 설명5', 20, 30);
INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, width)
VALUES (6, 65.245, 34.349, '레이아웃 설명6', 20, 30);


# 내용 40
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (1, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (1, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (1, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, code, code_explanation, code_type, main) values (2, 3, '코드내용', '코드설명', 'CPP', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (2, 4, 'https://www.erdcloud.com/d/c6EcfX5rhWDrvNi7i', false);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (3, 5, '수학공식', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (3, 6, 'https://www.youtube.com/watch?v=i2jWju6cUvk', false);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (4, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (4, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (4, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (5, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (5, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (5, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (6, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (6, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (6, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (7, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (7, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (7, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (8, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (8, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (8, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (9, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (9, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (9, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (10, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (10, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (10, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (11, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (11, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (11, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (12, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (12, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (12, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (13, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (13, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (13, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);
INSERT INTO contexts(post_post_id, layout_layout_id, context, main) values (14, 1, '줄글 내용', false);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (14, 2, 'http://img4.tmon.kr/cdn4/deals/2021/11/22/9164630210/front_9d836_cfq0o.jpg', true);
INSERT INTO contexts(post_post_id, layout_layout_id, url, main) values (14, 2, 'https://www.flaticon.com/kr/free-icon/example_5578817', true);

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

# 좋아요 6
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (1,1, CURRENT_TIME);
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (2,1, CURRENT_TIME);
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (3,1, CURRENT_TIME);
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (2,2, CURRENT_TIME);
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (3,2, CURRENT_TIME);
INSERT INTO likes(post_post_id, user_user_id, time) VALUES (3,3, CURRENT_TIME);

INSERT INTO comments(context, time, post_post_id, user_user_id)
VALUES ('이것은 댓글입니다', CURRENT_TIME, 1, 1);
INSERT INTO comments(context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES ('요건 대댓글입니다', CURRENT_TIME, 1, 1, 1);