package kit.prolog.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.QComment;
import kit.prolog.domain.QPost;
import kit.prolog.dto.CommentLv1Dto;
import kit.prolog.dto.CommentLv2Dto;
import kit.prolog.util.QuerydslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QComment qComment = QComment.comment;
    private final QPost qPost = QPost.post;

    @Override
    public List<CommentLv1Dto> findByPostId(Long postId, Long userId, Pageable pageable) {
        Long postWriterId = queryFactory.select(qPost.user.id)
                .from(qPost)
                .where(qPost.id.eq(postId))
                .fetchOne();

        List<CommentLv1Dto> commentLv1Dtos = selectCommentDtoFrom(CommentLv1Dto.class, userId, postWriterId)
                .where(qComment.post.id.eq(postId))
                .where(qComment.upperComment.isNull())
                .orderBy(QuerydslUtil.ordersFromPageable(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        commentLv1Dtos.stream()
                .forEach(commentLv1Dto ->
                        commentLv1Dto.setLowerComments(
                                selectCommentDtoFrom(CommentLv2Dto.class, userId, postWriterId)
                                        .where(qComment.upperComment.id.eq(commentLv1Dto.getId()))
                                        .orderBy(QuerydslUtil.ordersFromPageable(pageable.getSort()))
                                        .fetch()));

        return commentLv1Dtos;
    }

    private <T extends CommentLv2Dto> JPQLQuery<T> selectCommentDtoFrom(Class<T> commentDtoType, Long userId, Long postWriterId) {
        return queryFactory.select(
                        Projections.fields(
                                commentDtoType,
                                qComment.id,
                                qComment.user.id.as("userId"),
                                qComment.user.nickname,
                                qComment.user.image.as("userImage"),
                                qComment.user.id.eq(postWriterId).as("isPostWriter"),
                                qComment.user.id.eq(userId).as("isCommentWriter"),
                                qComment.context,
                                qComment.time))
                .from(qComment);
    }
}
