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
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .fetch().size();

        long tendaysView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.after(LocalDateTime.now().minusDays(10)))
                .fetch().size();

        long januaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 1,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),2,1,0,0)))
                .fetch().size();

        long februaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 2,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),3,1,0,0)))
                .fetch().size();

        long marchView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 3,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),4,1,0,0)))
                .fetch().size();

        long aprilView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 4,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),5,1,0,0)))
                .fetch().size();

        long mayView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 5,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),6,1,0,0)))
                .fetch().size();

        long juneView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 6,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),7,1,0,0)))
                .fetch().size();

        long julyView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 7,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),8,1,0,0)))
                .fetch().size();

        long augustView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 8,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),9,1,0,0)))
                .fetch().size();

        long septemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 9,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),10,1,0,0)))
                .fetch().size();

        long octoberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 10,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),1,1,0,0)))
                .fetch().size();

        long novemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 11,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear(),12,1,0,0)))
                .fetch().size();

        long decemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(postId))
                .where(hit.time.between(LocalDateTime.of(LocalDateTime.now().getYear(), 12,1,0,0),LocalDateTime.of(LocalDateTime.now().getYear()+1,1,1,0,0)))
                .fetch().size();

        StatisticsDto statisticsDto = new StatisticsDto(cumulativeViews,tendaysView,januaryView
                                                        ,februaryView,marchView,aprilView,mayView
                                                        ,juneView,julyView,augustView,septemberView
                                                        ,octoberView,novemberView,decemberView);
        return statisticsDto;
    }

    @Override
    public StatisticsDto findStatisticsByUserId(long userId, int year) {
        long cumulativeViews = query.select(hit.id)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .fetchCount();

        long tendaysView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.after(LocalDateTime.now().minusDays(10)))
                .fetchCount();

        long januaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,1,1,0,0),LocalDateTime.of(year,2,1,0,0)))
                .fetchCount();

        long februaryView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,2,1,0,0),LocalDateTime.of(year,3,1,0,0)))
                .fetchCount();

        long marchView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,3,1,0,0),LocalDateTime.of(year,4,1,0,0)))
                .fetchCount();

        long aprilView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,4,1,0,0),LocalDateTime.of(year,5,1,0,0)))
                .fetchCount();

        long mayView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,5,1,0,0),LocalDateTime.of(year,6,1,0,0)))
                .fetchCount();

        long juneView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,6,1,0,0),LocalDateTime.of(year,7,1,0,0)))
                .fetchCount();

        long julyView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,7,1,0,0),LocalDateTime.of(year,8,1,0,0)))
                .fetchCount();

        long augustView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,8,1,0,0),LocalDateTime.of(year,9,1,0,0)))
                .fetchCount();

        long septemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,9,1,0,0),LocalDateTime.of(year,10,1,0,0)))
                .fetchCount();

        long octoberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,10,1,0,0),LocalDateTime.of(year,11,1,0,0)))
                .fetchCount();

        long novemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,11,1,0,0),LocalDateTime.of(year,12,1,0,0)))
                .fetchCount();

        long decemberView = query.select(hit)
                .from(hit).join(post)
                .on(post.id.eq(hit.post.id))
                .where(post.id.eq(userId))
                .where(hit.time.between(LocalDateTime.of(year,12,1,0,0),LocalDateTime.of(year+1,1,1,0,0)))
                .fetchCount();

        StatisticsDto statisticsDto = new StatisticsDto(cumulativeViews,tendaysView,januaryView
                ,februaryView,marchView,aprilView,mayView
                ,juneView,julyView,augustView,septemberView
                ,octoberView,novemberView,decemberView);
        return statisticsDto;
    }
}
