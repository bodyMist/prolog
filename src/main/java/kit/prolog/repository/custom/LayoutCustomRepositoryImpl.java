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
    private final QContext context = QContext.context;
    private final QImage image = QImage.image;
    private final QCode code = QCode.code1;
    private final QHyperlink hyperlink = QHyperlink.hyperlink;
    private final QMathematics mathematics = QMathematics.mathematics;
    private final QVideo video = QVideo.video;
    private final QDocument document = QDocument.document;

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
        switch (type) {
            case CONTEXT:
                layoutDto = new LayoutDto(
                        query
                                .select(context.text)
                                .from(context)
                                .where(context.id.eq(layoutId))
                                .fetchOne()
                );
                break;
            case IMAGE:
                layoutDto = new LayoutDto(
                        query
                                .select(image.url)
                                .from(image)
                                .where(image.layout.id.eq(layoutId))
                        .fetch()
                );
                break;
            case CODES:
                layoutDto = new LayoutDto(
                        query
                                .selectFrom(code)
                                .where(code.id.eq(layoutId))
                                .fetchOne()
                );
                break;
            case HYPERLINK:
                layoutDto = new LayoutDto(
                        query
                                .select(hyperlink.url)
                                .from(hyperlink)
                                .where(hyperlink.id.eq(layoutId))
                        .fetchOne()
                );
                break;
            case MATHEMATICS:
                layoutDto = new LayoutDto(
                        query
                                .select(mathematics.context)
                                .from(mathematics)
                                .where(mathematics.id.eq(layoutId))
                        .fetchOne()
                );
                break;
            case VIDEOS:
                layoutDto = new LayoutDto(
                        query
                                .select(video.url)
                                .from(video)
                                .where(video.id.eq(layoutId))
                        .fetchOne()
                );
                break;
            case DOCUMENTS:
                layoutDto = new LayoutDto(
                        query
                                .select(document.url)
                                .from(document)
                                .where(document.id.eq(layoutId))
                        .fetchOne()
                );
                break;
        }
        return layoutDto;
    }
}
