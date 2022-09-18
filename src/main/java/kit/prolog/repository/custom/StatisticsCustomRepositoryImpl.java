package kit.prolog.repository.custom;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;

import kit.prolog.dto.StatisticsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticsCustomRepositoryImpl implements StatisticsCustomRepository {

    private final JPAQueryFactory query;
    private final QHit hit = QHit.hit;
    private final QPost post = QPost.post;

    @Override
    public StatisticsDto findStatisticByPostId(long userId, long postId) {

        long cumulativeViews = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .fetch().size();

        long tendaysView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.after(LocalDateTime.now().minusDays(10)))
                .fetch().size();

        long januaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(12), LocalDateTime.now().minusMonths(11)))
                .fetch().size();

        long februaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(11), LocalDateTime.now().minusMonths(10)))
                .fetch().size();

        long marchView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(10), LocalDateTime.now().minusMonths(9)))
                .fetch().size();

        long aprilView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(9), LocalDateTime.now().minusMonths(8)))
                .fetch().size();

        long mayView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(8), LocalDateTime.now().minusMonths(7)))
                .fetch().size();

        long juneView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(7), LocalDateTime.now().minusMonths(6)))
                .fetch().size();

        long julyView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(6), LocalDateTime.now().minusMonths(5)))
                .fetch().size();

        long augustView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(5), LocalDateTime.now().minusMonths(4)))
                .fetch().size();

        long septemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(4), LocalDateTime.now().minusMonths(3)))
                .fetch().size();

        long octoberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(3), LocalDateTime.now().minusMonths(2)))
                .fetch().size();

        long novemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(2), LocalDateTime.now().minusMonths(1)))
                .fetch().size();

        long decemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now()))
                .fetch().size();

        StatisticsDto statisticsDto = new StatisticsDto(cumulativeViews,tendaysView,januaryView
                                                        ,februaryView,marchView,aprilView,mayView
                                                        ,juneView,julyView,augustView,septemberView
                                                        ,octoberView,novemberView,decemberView);
        return statisticsDto;
    }

    @Override
    public StatisticsDto findStatisticsByUserId(long userId) {

        long cumulativeViews = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .fetch().size();

        long tendaysView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.after(LocalDateTime.now().minusDays(10)))
                .fetch().size();

        long januaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(12), LocalDateTime.now().minusMonths(11)))
                .fetch().size();

        long februaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(11), LocalDateTime.now().minusMonths(10)))
                .fetch().size();

        long marchView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(10), LocalDateTime.now().minusMonths(9)))
                .fetch().size();

        long aprilView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(9), LocalDateTime.now().minusMonths(8)))
                .fetch().size();

        long mayView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(8), LocalDateTime.now().minusMonths(7)))
                .fetch().size();

        long juneView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(7), LocalDateTime.now().minusMonths(6)))
                .fetch().size();

        long julyView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(6), LocalDateTime.now().minusMonths(5)))
                .fetch().size();

        long augustView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(5), LocalDateTime.now().minusMonths(4)))
                .fetch().size();

        long septemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(4), LocalDateTime.now().minusMonths(3)))
                .fetch().size();

        long octoberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(3), LocalDateTime.now().minusMonths(2)))
                .fetch().size();

        long novemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(2), LocalDateTime.now().minusMonths(1)))
                .fetch().size();

        long decemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.now().minusMonths(1), LocalDateTime.now()))
                .fetch().size();

        StatisticsDto statisticsDto = new StatisticsDto(cumulativeViews,tendaysView,januaryView
                ,februaryView,marchView,aprilView,mayView
                ,juneView,julyView,augustView,septemberView
                ,octoberView,novemberView,decemberView);
        return statisticsDto;
    }
}
