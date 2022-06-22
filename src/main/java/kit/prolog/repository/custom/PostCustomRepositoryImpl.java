package kit.prolog.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kit.prolog.domain.*;
import kit.prolog.dto.PostDetailDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PostCustomRepositoryImpl implements PostCustomRepository{
    @PersistenceContext
    EntityManager em;

    @Override
    public PostDetailDto findPostById(Long id) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPost post = QPost.post;
        QComment comment = QComment.comment;
        QMold mold = QMold.mold;
        QLayout layout = QLayout.layout;
        QCategory category = QCategory.category;
        QAttachment attachment = QAttachment.attachment;
        QLike like = QLike.like;
        QTag tag = QTag.tag;

        return null;
    }
}
