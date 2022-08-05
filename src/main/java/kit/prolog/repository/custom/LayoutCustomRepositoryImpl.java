package kit.prolog.repository.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.LayoutDto;
import kit.prolog.enums.LayoutType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LayoutCustomRepositoryImpl implements LayoutCustomRepository {
    private final JPAQueryFactory query;

    private final QMold mold = QMold.mold;
    private final QLayout layout = QLayout.layout;

    @Override
    public List<LayoutDto> findLayoutDetailByMold_Id(Long moldId) {

        List<LayoutDto> layoutList = query.select(
                Projections.constructor(LayoutDto.class,
                        layout.id,
                        layout.dtype,
                        layout.coordinateX,
                        layout.coordinateY,
                        layout.width,
                        layout.height
                )
        )
                .from(layout)
                .leftJoin(mold).on(layout.mold.id.eq(mold.id))
                .where(layout.mold.id.eq(moldId))
                .fetch();

        layoutList.forEach(layoutDto -> {
            layoutDto.addContent(selectLayout(layoutDto.getDtype(), layoutDto.getId()));
        });
        return layoutList;
    }

    public LayoutDto selectLayout(int layoutType, Long layoutId){
        LayoutDto layoutDto = null;
        LayoutType type = LayoutType.values()[layoutType];
        return null;
    }
}
