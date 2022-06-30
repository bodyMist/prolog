INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password)
VALUES ('sky834459', 0, 'sky834459@gmail.com', '', '안냥하세요', '김태훈', '판교', '8344');
INSERT INTO users(account, alarm, email, image, introduce, name, nickname, password)
VALUES ('asdf1234', 0, 'asdf1234@gmail.com', '', '안냥하세요', '홍길동', '홍길동', '1234');

INSERT INTO categories(name, user_user_id) VALUES ('개발용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('취미용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('전시용', 1);
INSERT INTO categories(name, user_user_id) VALUES ('프론트', 2);
INSERT INTO categories(name, user_user_id) VALUES ('백', 2);

INSERT INTO molds(name, user_user_id) VALUES ('기본 틀', 1);
INSERT INTO molds(name, user_user_id) VALUES ('복합 틀', 1);
INSERT INTO molds(name, user_user_id) VALUES ('연관 틀', 1);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (1, 10.245, 34.349, '레이아웃 설명1', 20, 1, 30, 1);
INSERT INTO contexts(text, layout_id) VALUES ('상세 내용', 1);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (2, 20.121, 34.349, '레이아웃 설명2', 20, 0, 30, 1);
INSERT INTO images(url, layout_id) VALUES ('상세 내용', 2);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (3, 33.245, 34.349, '레이아웃 설명3', 20, 1, 30, 2);
INSERT INTO codes(code, layout_id) VALUES ('상세 내용', 3);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (4, 49.245, 34.349, '레이아웃 설명4', 20, 0, 30, 2);
INSERT INTO hyperlinks(url, layout_id) VALUES ('상세 내용', 4);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (5, 53.245, 34.349, '레이아웃 설명5', 20, 1, 30, 3);
INSERT INTO mathematics(context, layout_id) VALUES ('상세 내용', 5);

INSERT INTO layouts(dtype, coordinatex, coordinatey, explanation, height, main, width, mold_mold_id)
VALUES (6, 65.245, 34.349, '레이아웃 설명6', 20, 0, 30, 3);
INSERT INTO videos(url, layout_id) VALUES ('상세 내용', 6);

INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '1이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '2이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '3이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '4이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '5이것은 테스트용이예요', 2, 3, 1);
INSERT INTO posts(time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (CURRENT_TIME, '6이것은 테스트용이예요', 3, 3, 1);

INSERT INTO comments(context, time, post_post_id, user_user_id)
VALUES ('이것은 댓글입니다', CURRENT_TIME, 1, 1);
INSERT INTO comments(context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES ('요건 대댓글입니다', CURRENT_TIME, 1, 1, 1);