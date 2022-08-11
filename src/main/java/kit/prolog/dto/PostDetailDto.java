package kit.prolog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/*
* 게시글 상세조회 API 반환용 DTO
*
* */
@Getter
@Setter
public class PostDetailDto {
    private UserDto userDto;
    private Long moldId;
    private PostDto postDto;
    private CategoryDto categoryDto;
    private LikeDto likeDto;
    private List<AttachmentDto> attachmentDto;
    private List<LayoutDto> layoutDto;
    private List<String> tags;
    private Long hits;
    private List<CommentLv1Dto> comments;

    public PostDetailDto(String userName, String userImage, Long postId, String postTitle,
                         LocalDateTime postTime, Long moldId, Long categoryId, String categoryName, Long hits) {
        this.userDto = new UserDto(userName, userImage);
        this.postDto = new PostDto(postId, postTitle, LocalDate.from(postTime));
        this.categoryDto = new CategoryDto(categoryId, categoryName);
        this.moldId = moldId;
        this.hits = hits;
    }
}
