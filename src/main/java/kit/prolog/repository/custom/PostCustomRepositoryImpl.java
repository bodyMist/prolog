package kit.prolog.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.PostDetailDto;
import kit.prolog.dto.PostDto;
import kit.prolog.dto.PostPreviewDto;
import kit.prolog.repository.jpa.LayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private final JPAQueryFactory query;

    private final QPost post = QPost.post;
    private final QMold mold = QMold.mold;
    private final QUser user = QUser.user;
    private final QCategory category = QCategory.category;
    private final QLayout layout = QLayout.layout;
    private final QHit hit = QHit.hit;
    private final QLike like = QLike.like;
    private final QContext context = QContext.context1;
    private final QPostTag postTag = QPostTag.postTag;
    private final QTag tag = QTag.tag;

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
    public List<LayoutDto> selectDetailLayout(Long postId){
        return query.select(
                Projections.constructor(LayoutDto.class,
                        layout, context.context, context.code, context.codeExplanation
                        ,context.codeType, context.url, context.main
                ))
                .from(context)
                .innerJoin(layout).on(context.layout.id.eq(layout.id))
                .where(context.post.id.eq(postId))
                .fetch();
    }

    @Override
    public List<PostPreviewDto> findPostByCategoryName(String account, String categoryName, int cursor) {
        List<PostPreviewDto> previewDtos = query.select(
                        Projections.constructor(PostPreviewDto.class,
                                post.id, post.title, post.time,
                                user.name, user.image, like.count())
                )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.account.eq(account)))
                .innerJoin(category).on(category.eq(post.category).and(category.name.eq(categoryName)))
                .leftJoin(like).on(post.eq(like.post))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        getPostsHits(previewDtos);

        previewDtos.forEach(post -> {
            List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
            post.addLayoutDto(layoutContext);
                }
        );
        return previewDtos;
    }

    @Override
    public List<PostPreviewDto> findMyPostByUserId(String account, int cursor) {
        List<PostPreviewDto> previewDtos = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user).and(user.account.eq(account)))
                .leftJoin(like).on(post.eq(like.post))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        getPostsHits(previewDtos);

        previewDtos.forEach(post -> {
                    List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
                    post.addLayoutDto(layoutContext);
                }
        );
        return previewDtos;
    }

    @Override
    public List<PostPreviewDto> findLikePostByUserId(String account, int cursor) {
        List<PostPreviewDto> previewDtos = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user))
                .leftJoin(like).on(post.eq(like.post).and(like.user.eq(user)))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        getPostsHits(previewDtos);

        previewDtos.forEach(post -> {
                    List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
                    post.addLayoutDto(layoutContext);
                }
        );
        return previewDtos;
    }

    @Override
    public List<PostPreviewDto> findHottestPosts(int page) {
        List<PostPreviewDto> previewDtos = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user))
                .leftJoin(like).on(post.eq(like.post))
                .where(like.time.after(LocalDateTime.now().minusDays(7)).or(like.time.isNull()))
                .groupBy(post)
                .orderBy(post.id.desc())
                .offset(page)
                .limit(PAGE_SIZE)
                .fetch();

        getPostsHits(previewDtos);

        previewDtos.forEach(post -> {
                    List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
                    post.addLayoutDto(layoutContext);
                }
        );
        return previewDtos;
    }

    @Override
    public List<PostPreviewDto> findRecentPosts(int cursor) {
        List<PostPreviewDto> previewDtos = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image, like.count())
        )
                .from(post)
                .innerJoin(user).on(post.user.eq(user))
                .leftJoin(like).on(post.eq(like.post))
                .where(lowerThanCursor(cursor))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
        previewDtos.forEach(post -> {
                    List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
                    post.addLayoutDto(layoutContext);
                }
        );
        return previewDtos;
    }

    @Override
    public List<PostPreviewDto> searchPosts(String keyword, int cursor) {
        List<PostPreviewDto> previewDtos = query.select(
                Projections.constructor(PostPreviewDto.class,
                        post.id, post.title, post.time,
                        user.name, user.image)
        )
                .from(post)
                .innerJoin(user).on(user.id.eq(post.user.id))
                .leftJoin(context).on(context.post.id.eq(post.id))
                .leftJoin(postTag).on(postTag.post.id.eq(post.id))
                .leftJoin(tag).on(tag.id.eq(postTag.tag.id))
                .where(post.title.contains(keyword).and(lowerThanCursor(cursor)))
                .groupBy(post)
                .orderBy(post.id.desc())
                .limit(PAGE_SIZE)
                .fetch();

        previewDtos.forEach(post -> {
            List<LayoutDto> layoutContext = selectMainContext(post.getPostDto().getId()).fetch();
            Long likeCount = selectLikes(post.getPostDto().getId()).fetchOne();
            post.addLayoutDto(layoutContext);
            post.setLikes(likeCount);
        });

        return previewDtos;
    }

    private JPQLQuery<Long> selectLikes(Long postId){
        return query.select(like.count()).from(like).where(like.post.id.eq(postId));
    }
    private JPQLQuery<LayoutDto> selectMainContext(Long postId){
        return query.select(
                        Projections.constructor(LayoutDto.class,
                                layout.dtype, layout.width, layout.height, layout.explanation,
                                context.context, context.code, context.codeExplanation, context.codeType, context.url
                        ))
                .from(context)
                .innerJoin(layout).on(context.layout.id.eq(layout.id))
                .where(context.post.id.eq(postId)
                        .and(context.main.eq(true))
                );
    }
    private void getPostsHits(List<PostPreviewDto> previewDtos){
        List<Long> idList = previewDtos.stream()
                .map(PostPreviewDto::getPostDto)
                .map(PostDto::getId).collect(Collectors.toList());

        Map<Long, Long> hitCount = new HashMap<>();
        List<Tuple> tuples = query
                .select(hit.post.id, hit.count())
                .from(hit)
                .where(hit.post.id.in(idList))
                .groupBy(hit.post).fetch();
        for(Tuple tuple : tuples){
            hitCount.put(tuple.get(0, Long.class), tuple.get(1, Long.class));
        }
        previewDtos.forEach(dto -> {
            Long hit = hitCount.get(dto.getPostDto().getId());

            dto.setHits(hit == null ? 0 : hit);
        });
    }
    private BooleanExpression lowerThanCursor(int cursor){
        return cursor == 0 ? null : post.id.lt( cursor);
    }
}