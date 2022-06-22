package kit.prolog.dto;

import java.util.List;
/*
* 게시글 상세조회 API 반환용 DTO
*
* */
public class PostDetailDto {
    private UserDto userDto;
    private PostDto postDto;
    private LayoutDto layoutDto;
    private CategoryDto categoryDto;
    private AttachmentDto attachmentDto;
    private List<String> tags;
    private int hits;
    private LikeDto likeDto;
    private List<CommentDto> comments;
}
