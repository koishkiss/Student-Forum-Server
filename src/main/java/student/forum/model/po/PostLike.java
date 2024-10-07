package student.forum.model.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostLike {
    private Integer postId;

    private Integer uid;

    private Date likeTime;
}
