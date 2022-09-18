package kit.prolog.repository.custom;

import kit.prolog.dto.StatisticsDto;

public interface StatisticsCustomRepository {

    StatisticsDto findStatisticByPostId(long userId, long postId);

    StatisticsDto findStatisticsByUserId(long userId, int year);

}
