package kit.prolog.repository.jpa;

import kit.prolog.domain.Attachment;
import kit.prolog.dto.AttachmentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<AttachmentDto> findByPost_Id(Long postId);
    void deleteAllByPost_Id(Long postId);
    Optional<Attachment> findByName(String name);
    Optional<Attachment> findByUrl(String url);
}
