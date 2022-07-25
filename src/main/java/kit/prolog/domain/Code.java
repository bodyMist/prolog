package kit.prolog.domain;

import kit.prolog.enums.CodeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "CODES")
@DiscriminatorValue("3")
@IdClass(CompositeKey.class)
@NoArgsConstructor
@Getter
public class Code {
    @Id
    @ManyToOne
    @JoinColumn(name = "LAYOUT_ID", nullable = false)
    private Layout layout;

    @Id
    @Column(name = "SEQUENCE", nullable = false)
    private Long sequence;


    @Column(length = 2000)
    private String code;
    @Column(length = 2000)
    private String codeExplanation;

    @Enumerated(EnumType.ORDINAL)
    private CodeType codeType;

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Code(List<String> content) {
        this.code = content.get(0);
        this.codeExplanation = content.get(1);
    }
}
