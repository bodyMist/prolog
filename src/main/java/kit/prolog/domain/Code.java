package kit.prolog.domain;

import kit.prolog.enums.CodeType;
import kit.prolog.enums.LayoutType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "CODES")
@DiscriminatorValue("3")
@NoArgsConstructor
@Getter
public class Code extends Layout{
    @Column(length = 2000)
    private String code;
    @Column(length = 2000)
    private String codeExplanation;

    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    public Code(List<String> codes) {
        this.code = codes.get(0);
        this.codeExplanation = codes.get(1);
        this.codeType = CodeType.valueOf(codes.get(2));
    }
}
