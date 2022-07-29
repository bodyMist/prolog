package kit.prolog.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostPreviewDto;
import kit.prolog.repository.jpa.LayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private final JPAQueryFactory query;
    private final LayoutRepository layoutRepository;

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
                        user.name, user.image, layout.id, layout.dtype, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.account.eq(account)))
                .innerJoin(category).on(category.eq(post.category).and(category.name.eq(categoryName)))
                .innerJoin(like).on(post.eq(like.post))
                .innerJoin(mold).on(mold.eq(post.mold))
                .innerJoin(layout).on(mold.eq(layout.mold).and(layout.main.eq(true)))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<PostPreviewDto> findMyPostByUserId(Long userId, int cursor) {
        List<PostPreviewDto> posts = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, layout.id, layout.dtype, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.id.eq(userId)))
                .leftJoin(like).on(post.eq(like.post))
                .innerJoin(mold).on(mold.eq(post.mold))
                .innerJoin(layout).on(mold.eq(layout.mold).and(layout.main.eq(true)))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        posts.forEach(post -> {
            post.getLayoutDto().addContent(
                    layoutRepository
                            .selectLayout(
                                    post.getLayoutDto().getDtype(), post.getLayoutDto().getId()
                            ));
        });
        return posts;
    }

    @Override
    public List<PostPreviewDto> findLikePostByUserId(Long userId, int cursor) {
        List<PostPreviewDto> posts = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, layout.id, layout.dtype, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.id.eq(userId)))
                .innerJoin(like).on(post.eq(like.post))
                .innerJoin(mold).on(mold.eq(post.mold))
                .innerJoin(layout).on(mold.eq(layout.mold).and(layout.main.eq(true)))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        posts.forEach(post -> {
            post.getLayoutDto().addContent(
                    layoutRepository
                            .selectLayout(
                                    post.getLayoutDto().getDtype(), post.getLayoutDto().getId()
                            ));
        });
        return posts;
    }

    @Override
    public List<PostPreviewDto> findAllPost(int cursor) {
        List<PostPreviewDto> posts = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, layout.id, layout.dtype, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user))
                .leftJoin(like).on(post.eq(like.post))
                .innerJoin(mold).on(mold.eq(post.mold))
                .innerJoin(layout).on(mold.eq(layout.mold).and(layout.main.eq(true)))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        posts.forEach(post -> {
            post.getLayoutDto().addContent(
                    layoutRepository
                            .selectLayout(
                                    post.getLayoutDto().getDtype(), post.getLayoutDto().getId()
                            ));
        });
        return posts;
    }

    private BooleanExpression lowerThanCursor(int cursor){
        return cursor == 0 ? null : post.id.lt( cursor);
    }
}
