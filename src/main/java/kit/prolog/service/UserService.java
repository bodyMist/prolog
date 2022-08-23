package kit.prolog.service;

import kit.prolog.domain.User;
import kit.prolog.dto.UserInfoDto;
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
    private final ContextRepository contextRepository;

    // email 회원가입
    public boolean createUserByEmail(User newUser){
        newUser.setSns(0); // email 회원가입
        User user = userRepository.findOneByAccountAndEmail(newUser.getAccount(), newUser.getEmail());
        if(user == null){
            userRepository.save(newUser);
            return true;
        }else{
            return false;
        }
    }

    // 소셜 회원가입
    public boolean createUserBySocial(User newUser){
        newUser.setSns(1); // 소셜 회원가입
        User user = userRepository.findOneByAccountAndEmail(newUser.getAccount(), newUser.getEmail());
        if(user != null){
            userRepository.save(newUser);
            return true;
        }else{
            return false;
        }
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
    public boolean updateUser(Long memberPk, UserInfoDto modifiedUser){
        User user = userRepository.findOneById(memberPk);
        try{
            if(user != null){
                user.setName(modifiedUser.getName());
                user.setImage(modifiedUser.getImage());
                user.setIntroduce(modifiedUser.getIntroduction());
                user.setNickname(modifiedUser.getNickname());
                user.setAlarm(modifiedUser.isAlarm());
                userRepository.save(user);
                return true;
            }else{
                return false;
            }
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }

    // 회원 탈퇴
    // 댓글,좋아요,조회,첨부파일,게시글태그,레이아웃->태그,레이아웃틀,게시글->카테고리->회원
    public User deleteUser(Long memberPk){
        User user = new User();
        try{
            likeRepository.deleteAllByUser_Id(memberPk);
            postRepository.findByUser_Id(memberPk).forEach(post -> {
                likeRepository.deleteAllByPost_Id(post.getId());
                commentRepository.deleteAllByPost_Id(post.getId());
                hitRepository.deleteAllByPost_Id(post.getId());
                // 파일서버에 삭제 요청 필요
                attachmentRepository.deleteAllByPost_Id(post.getId());
                postTagRepository.deleteAllByPost_Id(post.getId());
                contextRepository.deleteAllByPost_Id(post.getId());
                Long moldId = postRepository.findMoldIdByPostId(post.getId());
                postRepository.deleteById(post.getId());
                layoutRepository.deleteAllByMold_Id(moldId);
            });
            moldRepository.deleteByUser_Id(memberPk);

            // 유저가 작성한 게시글에 대해 모든 댓글들을 삭제한 후,
            // 유저의 남은 comment를 block처리 후 userFK를 null로 변경
            commentRepository.blockCommentsByUserId(memberPk);
            categoryRepository.deleteAllByUser_Id(memberPk);

            user = userRepository.findOneById(memberPk);
            if(user != null)
                userRepository.deleteById(memberPk);
        }catch (NullPointerException e){
            System.out.println("Error : no user");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return user;
    }

    // 로그인
    public User login(String account, String password){
        User user;
        try{
            user = userRepository.findOneByAccountAndPassword(account, password);
            if(user != null)
                return user;
            else
                return null;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return null;
        }
    }

    // 아이디 찾기
    public String searchAccount(String email){
        User user;
        String account = "";
        try{
            user = userRepository.findOneByEmail(email);
            if(user != null){
                return user.getAccount();
            }else{
                return null;
            }
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return account;
        }
    }

    // 비밀번호 변경
    public boolean changePassword(String email, String account, String password){
        User user;
        try{
            user = userRepository.findOneByAccountAndEmail(account, email);
            if(user != null){
                user.setPassword(password);
                return true;
            }
            else
                return false;
        }catch (NullPointerException e){
            System.out.println("Error : no user");
            return false;
        }
    }
}
