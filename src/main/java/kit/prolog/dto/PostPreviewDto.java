package kit.prolog.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
public class PostPreviewDto {
    private PostDto postDto;
    private Long hits = 0L;
    private Long likes = 0L;
    private UserDto userDto;
    private LayoutDto layoutDto;
    public PostPreviewDto(Long postId, String title, LocalDateTime time, String name,
                          String image, Long likes) {
        this.postDto = new PostDto(postId, title, time);
        this.likes = likes;
        this.userDto = new UserDto(name, image);
    }
    public PostPreviewDto(Long postId, String title, LocalDateTime time,
                          String name, String image){
        this.postDto = new PostDto(postId, title, time);
        this.userDto = new UserDto(name, image);
    }

    public void addLayoutDto(List<LayoutDto> context) {
        context.forEach(node->{
            if (this.layoutDto == null) {
                this.layoutDto = new LayoutDto(node);
            }else {
                this.layoutDto.addUrl(node);
            }
        });
    }
}
