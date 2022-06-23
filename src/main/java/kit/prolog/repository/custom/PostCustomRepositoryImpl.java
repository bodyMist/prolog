package kit.prolog.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostPreviewDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PostCustomRepositoryImpl implements PostCustomRepository{
    @PersistenceContext
    EntityManager em;

    @Override
    public PostDetailDto findPostById(Long id) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPost post = QPost.post;
        QComment comment = QComment.comment;
        QMold mold = QMold.mold;
        QLayout layout = QLayout.layout;
        QCategory category = QCategory.category;
        QAttachment attachment = QAttachment.attachment;
        QLike like = QLike.like;
        QTag tag = QTag.tag;

        return null;
    }

    @Override
    public List<PostPreviewDto> findPostByCategoryName(String account, String categoryName, int cursor) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QUser user = QUser.user;
        QPost post = QPost.post;
        QMold mold = QMold.mold;
        QLayout layout = QLayout.layout;
        QCategory category = QCategory.category;
        QLike like = QLike.like;

        return query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, layout, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.account.eq(account)))
                .innerJoin(category).on(category.eq(post.category).and(category.name.eq(categoryName)))
                .innerJoin(like).on(post.eq(like.post))
                .innerJoin(mold).on(mold.eq(post.mold))
                .innerJoin(layout).on(mold.eq(layout.mold).and(layout.main.eq(true)))
                .where(greaterThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression greaterThanCursor(int cursor){
        return cursor == 0 ? null : QPost.post.id.gt(cursor);
    }
}
