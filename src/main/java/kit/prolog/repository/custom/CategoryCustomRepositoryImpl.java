package kit.prolog.repository.custom;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.QCategory;
import kit.prolog.domain.QPost;
import kit.prolog.dto.CategoryInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QCategory qCategory = QCategory.category;
    private final QPost qPost = QPost.post;

    private final NumberPath<Long> countPath = Expressions.numberPath(Long.class, "count");

    @Override
    public List<CategoryInfoDto> findInfoByUserId(Long userId) {
        List<CategoryInfoDto> lv1Categories = selectCategoryInfoDtoFromCategory()
                .where(qCategory.user.id.eq(userId))
                .where(qCategory.upperCategory.isNull())
                .fetch();

        lv1Categories.stream()
                .forEach(lv1Category -> {
                    List<CategoryInfoDto> lv2Categories = fetchChildrenOf(lv1Category.getId());
                    lv2Categories.stream()
                            .forEach(lv2Category ->
                                    lv2Category.setChild(fetchChildrenOf(lv2Category.getId())));
                    lv1Category.setChild(lv2Categories);
                });

        return lv1Categories;
    }

    private NumberExpression<Long> postCount() {
        return Expressions.asNumber(
                JPAExpressions.select(qPost.count())
                        .from(qPost)
                        .where(qPost.category.eq(qCategory)));
    }

    private JPAQuery<CategoryInfoDto> selectCategoryInfoDtoFromCategory() {
        return queryFactory.select(
                        Projections.fields(
                                CategoryInfoDto.class,
                                qCategory.id,
                                qCategory.name,
                                ExpressionUtils.as(postCount(), countPath)))
                .from(qCategory);
    }

    private List<CategoryInfoDto> fetchChildrenOf(Long categoryId) {
        return selectCategoryInfoDtoFromCategory()
                .where(qCategory.upperCategory.id.eq(categoryId))
                .fetch();
    }
}
