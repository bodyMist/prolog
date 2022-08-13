package kit.prolog.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class PostPreviewDto {
    private PostDto postDto;
    private Long hits = 0L;
    private int likes;
    private UserDto userDto;
    private LayoutDto layoutDto;
    public PostPreviewDto(Long postId, String title, LocalDateTime time, String name,
                          String image, Long likes) {
        this.postDto = new PostDto(postId, title, time.toLocalDate());
        this.likes = likes.intValue();
        this.userDto = new UserDto(name, image);
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public void setLayoutDto(List<LayoutDto> context) {
        context.forEach(node->{
            if (this.layoutDto == null) {
                this.layoutDto = new LayoutDto(node);
            }else {
                this.layoutDto.addUrl(node);
            }
        });
    }
}
