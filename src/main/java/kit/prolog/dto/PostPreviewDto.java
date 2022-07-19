package kit.prolog.dto;

import kit.prolog.domain.Layout;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class PostPreviewDto {
    private PostDto postDto;
    private int likes;
    private UserDto userDto;
    private LayoutDto layoutDto;
    public PostPreviewDto(Long postId, String title, LocalDateTime time, String name,
                          String image, Long layoutId, int layoutType, Long likes) {
        this.postDto = new PostDto(postId, title, time.toLocalDate());
        this.likes = likes.intValue();
        this.userDto = new UserDto(name, image);
        this.layoutDto = new LayoutDto(layoutId, layoutType);
    }

    public void setLayoutDto(String context) {
        this.layoutDto.setContext(context);
    }
}
