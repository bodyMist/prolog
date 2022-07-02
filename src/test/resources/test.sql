INSERT INTO users(user_id, account, alarm, email, image, introduce, name, nickname, password)
VALUES (1, 'sky834459', 0, 'sky834459@gmail.com', 'https://1', '안냥하세요', '김태훈', '판교', '8344');
INSERT INTO users(user_id, account, alarm, email, image, introduce, name, nickname, password)
VALUES (2, 'asdf1234', 0, 'asdf1234@gmail.com', 'https://2', '안냥하세요', '홍길동', '홍길동', '1234');

INSERT INTO categories(category_id, name, user_user_id) VALUES (1, '개발용', 1);
INSERT INTO categories(category_id, name, user_user_id) VALUES (2, '취미용', 1);
INSERT INTO categories(category_id, name, user_user_id) VALUES (3, '전시용', 1);
INSERT INTO categories(category_id, name, user_user_id) VALUES (4, '프론트', 2);
INSERT INTO categories(category_id, name, user_user_id) VALUES (5, '백', 2);

INSERT INTO molds(mold_id, name, user_user_id) VALUES (1, '기본 틀', 1);
INSERT INTO molds(mold_id, name, user_user_id) VALUES (2, '복합 틀', 1);
INSERT INTO molds(mold_id, name, user_user_id) VALUES (3, '연관 틀', 1);

INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (1, CURRENT_TIME, '1이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (2, CURRENT_TIME, '2이것은 테스트용이예요', 1, 1, 1);
INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (3, CURRENT_TIME, '3이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (4, CURRENT_TIME, '4이것은 테스트용이예요', 2, 2, 1);
INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (5, CURRENT_TIME, '5이것은 테스트용이예요', 2, 3, 1);
INSERT INTO posts(post_id, time, title, category_category_id, mold_mold_id, user_user_id)
VALUES (6, CURRENT_TIME, '6이것은 테스트용이예요', 3, 3, 1);

INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (1, '이것은 댓글입니다1', '2022-07-02 14:19:28.000000', 1, 1);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (2, '이것은 댓글입니다2', '2022-07-02 14:19:28.000001', 1, 1);
INSERT INTO comments(comment_id, context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES (3, '요건 대댓글입니다1', '2022-07-02 14:19:28.000002', 2, 1, 2);
INSERT INTO comments(comment_id, context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES (4, '요건 대댓글입니다2', '2022-07-02 14:19:28.000003', 2, 1, 1);
INSERT INTO comments(comment_id, context, time, upper_comment_comment_id, post_post_id, user_user_id)
VALUES (5, '요건 대댓글입니다3', '2022-07-02 14:19:28.000004', 2, 1, 2);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (6, '이것은 댓글입니다3', '2022-07-02 14:19:28.000005', 1, 2);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (7, '이것은 댓글입니다4', '2022-07-02 14:19:28.000006', 1, 1);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (8, '이것은 댓글입니다5', '2022-07-02 14:19:28.000007', 1, 2);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (9, '이것은 댓글입니다6', '2022-07-02 14:19:28.000008', 1, 1);
INSERT INTO comments(comment_id, context, time, post_post_id, user_user_id)
VALUES (10, '이것은 댓글입니다7', '2022-07-02 14:19:28.000009', 1, 1);