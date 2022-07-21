package kit.prolog.domain;

import kit.prolog.enums.CodeType;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "CODES")
@DiscriminatorValue("3")
@IdClass(CompositeKey.class)
@NoArgsConstructor
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

    public Code(String code) {
        this.code = code;
    }
}
