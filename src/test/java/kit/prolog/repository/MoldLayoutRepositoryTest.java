package kit.prolog.repository;

import kit.prolog.dto.LayoutDto;
import kit.prolog.dto.MoldDto;
import kit.prolog.repository.jpa.LayoutRepository;
import kit.prolog.repository.jpa.MoldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MoldLayoutRepositoryTest {
    @Autowired MoldRepository moldRepository;
    @Autowired LayoutRepository layoutRepository;

    @Test
    void 레이아웃틀_조회(){
        Long userId = 1L;
        List<MoldDto> moldList = moldRepository.findByUser_Id(userId);
        System.out.println(moldList);
        // Projection 확인
        assertThat(moldList.get(0).getClass()).isEqualTo(MoldDto.class);
    }

    @Test
    void 레이아웃_리스트_조회(){
        Long moldId = 1L;
        List<LayoutDto> layoutList = layoutRepository.findByMold_Id(moldId);
        System.out.println(layoutList);
        // Projection 확인
        assertThat(layoutList.get(0).getClass()).isEqualTo(LayoutDto.class);
    }
}
