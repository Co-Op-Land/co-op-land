package com.coop.land.dummyDataGeneration;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.repository.CommentRepository;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class DummyDataGenerationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final Random random = new Random();

    // 한국어 닉네임 생성을 위한 데이터
    private final String[] koreanNicknamePrefix = {"행복한", "즐거운", "찬란한", "멋진", "귀여운", "대단한", "힘찬", "용감한", "똑똑한", "현명한"};
    private final String[] koreanNicknameSuffix = {"여행자", "독서가", "개발자", "요리사", "선생님", "학생", "작가", "연구자", "디자이너", "게이머"};

    // 게시글 제목 데이터
    private final String[] walkthroughTitles = {
            "이 게임 공략법 정리해봤습니다",
            "숨겨진 지역 찾는 방법",
            "보스 쉽게 물리치는 팁",
            "초보자를 위한 단계별 공략",
            "레벨 빠르게 올리는 방법"
    };

    private final String[] recommendationTitles = {
            "이 게임 꼭 해보세요",
            "2024년 최고의 인디 게임 추천",
            "RPG 좋아하시는 분들께 추천",
            "스토리가 좋은 게임 TOP 5",
            "그래픽이 아름다운 게임 모음"
    };

    // 게시글 내용 데이터
    private final String[] contentPrefixes = {
            "안녕하세요. 오늘은 제가 경험한 내용을 공유하려고 합니다. ",
            "많은 분들이 궁금해하시는 내용을 정리했습니다. ",
            "게임을 하다가 발견한 꿀팁입니다. ",
            "제 경험을 바탕으로 작성한 가이드입니다. ",
            "여러분의 게임 플레이에 도움이 되었으면 합니다. "
    };

    private final String[] contentMiddles = {
            "먼저 게임의 기본 메커니즘을 이해하는 것이 중요합니다. ",
            "효율적으로 레벨을 올리려면 다음 순서로 진행하세요. ",
            "숨겨진 아이템은 주로 맵의 구석에 있습니다. ",
            "보스 전투에서는 패턴을 파악하는 것이 핵심입니다. ",
            "리소스 관리는 게임 중반부터 매우 중요해집니다. "
    };

    private final String[] contentSuffixes = {
            "여러분의 경험도 댓글로 공유해주세요.",
            "추가 질문이 있으시면 언제든 물어봐주세요.",
            "다음에는 더 심화된 내용을 공유하겠습니다.",
            "함께 게임을 즐기고 싶으신 분들은 친구 추가해주세요.",
            "이 정보가 도움이 되었다면 좋아요 부탁드립니다."
    };

    // 댓글 내용 데이터
    private final String[] commentContents = {
            "정말 유용한 정보 감사합니다!",
            "이 방법 덕분에 클리어할 수 있었어요",
            "저는 다른 방법으로 해결했는데, 이 방법이 더 효율적인 것 같네요",
            "추가 정보가 있을까요?",
            "이 게임 정말 재밌죠. 저도 도전해봐야겠어요"
    };

    @Test
    @Rollback(false) // 트랜잭션 롤백 방지
    public void generateDummyData() {
        try {
            // 사용자 10명 생성
            List<Member> members = IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> createMember(
                            "user" + i + "@example.com",
                            "password" + i,
                            generateKoreanNickname()))
                    .collect(Collectors.toList());
            memberRepository.saveAll(members);

            System.out.println("회원 10명 생성 완료");

            // 게시글 100개 생성 (카테고리별로 50개씩)
            List<Post> posts = IntStream.rangeClosed(1, 100)
                    .mapToObj(i -> {
                        PostCategory category = (i <= 50) ?
                                PostCategory.WALKTHROUGH : PostCategory.RECOMMENDATION;

                        String title = (category == PostCategory.WALKTHROUGH) ?
                                getRandomElement(walkthroughTitles) + " " + random.nextInt(100) :
                                getRandomElement(recommendationTitles) + " " + random.nextInt(100);

                        return createPost(
                                getRandomElement(members),
                                title,
                                generatePostContent(),
                                category);
                    })
                    .collect(Collectors.toList());
            postRepository.saveAll(posts);

            System.out.println("게시글 100개 생성 완료");

            // 각 게시글마다 댓글 3개씩 생성
            List<Comment> allComments = posts.stream()
                    .flatMap(post -> IntStream.rangeClosed(1, 3)
                            .mapToObj(i -> createComment(
                                    getRandomElement(members),
                                    post,
                                    getRandomElement(commentContents)))
                    )
                    .collect(Collectors.toList());
            commentRepository.saveAll(allComments);

            System.out.println("댓글 " + allComments.size() + "개 생성 완료");

            // 대댓글 생성 (절반의 댓글에 1개의 대댓글 추가)
            List<Comment> replies = IntStream.range(0, allComments.size() / 2)
                    .mapToObj(i -> createComment(
                            getRandomElement(members),
                            allComments.get(i).getPost(),
                            "ㄴ " + getRandomElement(commentContents),
                            allComments.get(i)))
                    .collect(Collectors.toList());
            commentRepository.saveAll(replies);

            System.out.println("대댓글 " + replies.size() + "개 생성 완료");

            System.out.println("더미 데이터 생성 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;  // 테스트 실패로 처리
        }
    }

    private Member createMember(String email, String password, String nickname) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();  // role은 기본값으로 USER 설정됨
    }

    private Post createPost(Member member, String title, String content, PostCategory category) {
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }

    private Comment createComment(Member member, Post post, String content) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .build();
    }

    private Comment createComment(Member member, Post post, String content, Comment parent) {
        return Comment.builder()
                .member(member)
                .post(post)
                .content(content)
                .parent(parent)
                .build();
    }

    private <T> T getRandomElement(T[] array) {
        return array[random.nextInt(array.length)];
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    private String generateKoreanNickname() {
        return getRandomElement(koreanNicknamePrefix) +
                getRandomElement(koreanNicknameSuffix) +
                random.nextInt(100);
    }

    private String generatePostContent() {
        return getRandomElement(contentPrefixes) +
                getRandomElement(contentMiddles) +
                getRandomElement(contentSuffixes);
    }
}
