package kit.prolog.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@Getter
public class StatisticsDto {

    private long cumulativeViews; // 누적 조회수

    private long recentViews; // 최근 조회수 (기간 : 10일)

    private long januaryViews; //각 월별 조회수
    private long februaryViews;
    private long marchViews;
    private long aprilViews;
    private long mayViews;
    private long juneViews;
    private long julyViews;
    private long augustViews;
    private long septemberViews;
    private long octoberViews;
    private long novemberViews;
    private long decemberViews;

    public StatisticsDto(long cumulativeViews, long recentViews, long januaryViews,
                         long februaryViews, long marchViews, long aprilViews, long mayViews,
                         long juneViews, long julyViews, long augustViews, long septemberViews,
                         long octoberViews, long novemberViews, long decemberViews){

        this.cumulativeViews = cumulativeViews;
        this.recentViews = recentViews;
        this.januaryViews = januaryViews;
        this.februaryViews = februaryViews;
        this.marchViews = marchViews;
        this.aprilViews = aprilViews;
        this.mayViews = mayViews;
        this.juneViews = juneViews;
        this.julyViews = julyViews;
        this.augustViews = augustViews;
        this.septemberViews = septemberViews;
        this.octoberViews = octoberViews;
        this.novemberViews = novemberViews;
        this.decemberViews = decemberViews;

    }
}
