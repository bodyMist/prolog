package kit.prolog.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory query;

    private final QPost post = QPost.post;
    private final QMold mold = QMold.mold;
    private final QUser user = QUser.user;
    private final QCategory category = QCategory.category;
    private final QLayout layout = QLayout.layout;
    private final QHit hit = QHit.hit;
    private final QLike like = QLike.like;

    @Override
    public PostDetailDto findPostById(Long postId) {
        return query.select(
                Projections.constructor(PostDetailDto.class,
                        user.name,
                        user.image,
                        post.id,
                        post.title,
                        post.time,
                        mold.id,
                        category.id,
                        category.name,
                        hit.count()
                )
        )
                .from(post)
                .leftJoin(user).on(post.user.id.eq(user.id))
                .leftJoin(mold).on(post.mold.id.eq(mold.id))
                .leftJoin(category).on(post.category.id.eq(category.id))
                .leftJoin(hit).on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .groupBy(hit.post.id)
                .fetchOne();
    }

    @Override
    public List<PostPreviewDto> findPostByCategoryName(String account, String categoryName, int cursor) {
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

    @Override
    public List<PostPreviewDto> findPostByUserId(Long userId, int cursor) {
        return null;
    }

    private BooleanExpression greaterThanCursor(int cursor){
        return cursor == 0 ? null : QPost.post.id.gt(cursor);
    }
}
