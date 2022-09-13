package kit.prolog.domain;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "CONTEXTS")
@Setter
@NoArgsConstructor
public class Context{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTEXT_ID", nullable = false)
    private Long id;

    @Column(length = 2000)
    private String context;
    @Column(length = 2000)
    private String url;
    @Column(length = 2000)
    private String code;
    private String codeType;
    @Column(length = 2000)
    private String codeExplanation;
    @Column(nullable = false)
    private boolean main = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Layout layout;

    public Context(boolean main, Post post, Layout layout) {
        this.main = main;
        this.post = post;
        this.layout = layout;
    }

    public Context(String url, boolean main, Post post, Layout layout) {
        this.url = url;
        this.main = main;
        this.post = post;
        this.layout = layout;
    }
}
