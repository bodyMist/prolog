package kit.prolog.service;

import kit.prolog.domain.User;
import kit.prolog.repository.jpa.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MoldRepository moldRepository;
    private final LayoutRepository layoutRepository;
    private final CategoryRepository categoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final PostTagRepository postTagRepository;
    private final CommentRepository commentRepository;
    private final HitRepository hitRepository;

    // email 회원가입
    public boolean createUserByEmail(User user){
        if(userRepository.existsUserByEmail(user.getEmail())){
            userRepository.save(user);
        }
        // error catch 추가 예정
        return true;
    }

    // 소셜 회원가입
    public boolean createUserBySocial(User user){
        if(userRepository.existsUserByEmail(user.getEmail())){
            userRepository.save(user);
            // account = email
        }
        // error catch 추가 예정
        return true;
    }

    // 회원 정보 조회
    public User readUser(Long memberPk){
        User user = new User();
        try{
            user = userRepository.findOneById(memberPk);
            return user;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }
        // user 존재하면 user 반환
        // 비어있는 user 반환
        return user;
    }

    // 회원 정보 수정
    public User updateUser(Long memberPk, User modifiedUser){
        User user = new User();
        try{
            user = userRepository.findOneById(memberPk);
            user.setName(modifiedUser.getName());
            user.setImage(modifiedUser.getImage());
            user.setIntroduce(modifiedUser.getIntroduce());
            user.setNickname(modifiedUser.getNickname());
            user.setAlarm(modifiedUser.getAlarm());
            userRepository.save(user);
            user = userRepository.findOneById(memberPk);
            return user;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return null;
        }
    }

    // 회원 탈퇴
    // 댓글,좋아요,조회,첨부파일,게시글태그,레이아웃->태그,레이아웃틀,게시글->카테고리->회원
    public User deleteUser(Long memberPk){
        User user = new User();
        try{

            likeRepository.deleteAllByUser_Id(memberPk);
            postRepository.findByUser_Id(memberPk).forEach(post -> {
                commentRepository.deleteAllByPost_Id(post.getId());
                hitRepository.deleteAllByPost_Id(post.getId());
                // 파일서버에 삭제 요청 필요
                attachmentRepository.deleteAllByPost_Id(post.getId());
                postTagRepository.deleteAllByPost_Id(post.getId());
                Long moldId = postRepository.findMoldIdByPostId(post.getId());
                postRepository.deleteById(post.getId());
                layoutRepository.deleteAllByMold_Id(moldId);
                moldRepository.deleteById(moldId);
            });
            // 유저가 작성한 게시글에 대해 모든 댓글들을 삭제한 후,
            // 유저의 남은 comment를 block처리 후 userFK를 null로 변경
            commentRepository.blockCommentsByUserId(memberPk);
            categoryRepository.deleteAllByUser_Id(memberPk);

            user = userRepository.findOneById(memberPk);
            if(user != null)
                userRepository.deleteById(memberPk);
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }
        return user;
    }

    // 로그인
    public boolean login(String account, String password){
        User user;
        try{
            user = userRepository.findByAccountAndPassword(account, password);
            if(user != null)
                return true;
            else
                return false;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }

    // 아이디 찾기
    public String searchAccount(String email){
        User user;
        String account = "";
        try{
            user = userRepository.findOneByEmail(email);
            account = user.getAccount();
            return account;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return account;
        }
    }

    // 비밀번호 변경을 위한 user 검색
    public boolean changePassword(String account, String password){
        User user;
        try{
            user = userRepository.findByAccountAndPassword(account, password);
            if(user != null)
                return true;
            else
                return false;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }
}
