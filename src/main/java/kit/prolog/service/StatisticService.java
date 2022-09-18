package kit.prolog.service;
import kit.prolog.dto.StatisticsDto;
import kit.prolog.repository.custom.StatisticsCustomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class StatisticService {

    StatisticsCustomRepository statisticsCustomRepository;

    public StatisticsDto viewStatisticByPostId(Long userId, Long postId){
        return statisticsCustomRepository.findStatisticByPostId(userId, postId);
    }

    public StatisticsDto viewStatisByUserId(Long userId, Long year){
        return statisticsCustomRepository.findStatisticsByUserId(userId, year);
    }
}
