package com.coop.land.dummyDataGeneration;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import com.coop.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
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

    private final Random random = new Random();

    // 한국어 닉네임 생성을 위한 데이터
    private final String[] koreanNicknamePrefix = {"행복한", "즐거운", "찬란한", "멋진", "귀여운", "대단한", "힘찬", "용감한", "똑똑한", "현명한"};
    private final String[] koreanNicknameSuffix = {"여행자", "독서가", "개발자", "요리사", "선생님", "학생", "작가", "연구자", "디자이너", "게이머"};

    // 게시글 제목 데이터 - 다양성을 위해 확장
    private final String[] walkthroughTitles = {
            "이 게임 공략법 정리해봤습니다",
            "숨겨진 지역 찾는 방법",
            "보스 쉽게 물리치는 팁",
            "초보자를 위한 단계별 공략",
            "레벨 빠르게 올리는 방법",
            "히든 아이템 획득 방법",
            "퀘스트 효율적으로 클리어하기",
            "엔딩 모음 및 달성 방법",
            "레이드 공략 가이드",
            "최강 캐릭터 빌드 추천"
    };

    private final String[] recommendationTitles = {
            "이 게임 꼭 해보세요",
            "2024년 최고의 인디 게임 추천",
            "RPG 좋아하시는 분들께 추천",
            "스토리가 좋은 게임 TOP 5",
            "그래픽이 아름다운 게임 모음",
            "멀티플레이어 추천 게임",
            "한국에서 개발된 우수 게임들",
            "스팀 세일 꼭 구매해야 할 게임",
            "스트레스 해소에 좋은 게임 추천",
            "짧은 시간에 즐길 수 있는 캐주얼 게임"
    };

    // 게임 이름 목록 추가 - 제목 다양화를 위해
    private final String[] gameNames = {
            "엘든 링", "발더스 게이트 3", "젤다의 전설", "파이널 판타지 16", "데드 셀",
            "스타필드", "사이버펑크 2077", "디아블로 4", "스타크래프트", "롤",
            "오버워치", "배틀그라운드", "발로란트", "모바일 레전드", "포켓몬",
            "데스티니", "몬스터 헌터", "레드 데드 리뎀션", "더 라스트 오브 어스", "갓 오브 워"
    };

    // 게시글 내용 데이터 - 길이와 다양성 증가
    private final String[] contentPrefixes = {
            "안녕하세요. 오늘은 제가 경험한 내용을 공유하려고 합니다. ",
            "많은 분들이 궁금해하시는 내용을 정리했습니다. ",
            "게임을 하다가 발견한 꿀팁입니다. ",
            "제 경험을 바탕으로 작성한 가이드입니다. ",
            "여러분의 게임 플레이에 도움이 되었으면 합니다. ",
            "오랜 시간 연구한 결과를 공유합니다. ",
            "처음 접하시는 분들을 위한 내용입니다. ",
            "고수분들만 아는 숨겨진 정보입니다. ",
            "공식 가이드에 나오지 않는 팁입니다. ",
            "여러 커뮤니티의 정보를 종합했습니다. "
    };

    private final String[] contentMiddles = {
            "먼저 게임의 기본 메커니즘을 이해하는 것이 중요합니다. ",
            "효율적으로 레벨을 올리려면 다음 순서로 진행하세요. ",
            "숨겨진 아이템은 주로 맵의 구석에 있습니다. ",
            "보스 전투에서는 패턴을 파악하는 것이 핵심입니다. ",
            "리소스 관리는 게임 중반부터 매우 중요해집니다. ",
            "스킬 빌드는 플레이 스타일에 맞게 선택하세요. ",
            "초반에는 메인 퀘스트에 집중하는 것이 좋습니다. ",
            "PVP 콘텐츠는 레벨 캡을 찍은 후 도전하세요. ",
            "게임 설정에서 몇 가지 옵션을 변경하면 플레이가 훨씬 편해집니다. ",
            "다른 플레이어와의 협동은 난이도를 크게 낮춰줍니다. "
    };

    private final String[] contentSuffixes = {
            "여러분의 경험도 댓글로 공유해주세요.",
            "추가 질문이 있으시면 언제든 물어봐주세요.",
            "다음에는 더 심화된 내용을 공유하겠습니다.",
            "함께 게임을 즐기고 싶으신 분들은 친구 추가해주세요.",
            "이 정보가 도움이 되었다면 좋아요 부탁드립니다.",
            "앞으로도 유용한 정보 계속 올리겠습니다.",
            "게임사의 다음 업데이트 내용도 정리해서 올리겠습니다.",
            "여러분의 피드백 덕분에 더 좋은 글을 쓸 수 있었습니다.",
            "이 글이 초보자분들에게 도움이 되었으면 좋겠습니다.",
            "게임을 더 재미있게 즐기시길 바랍니다."
    };

    @Test
    @Rollback(false) // 트랜잭션 롤백 방지
    public void generateVeryLargePostData() {
        try {
            // 사용자 20명 생성 (더 많은 게시글을 위해 사용자 수 증가)
            List<Member> members = IntStream.rangeClosed(1, 20)
                    .mapToObj(i -> createMember(
                            "user" + i + "@example.com",
                            "password" + i,
                            generateKoreanNickname()))
                    .collect(Collectors.toList());
            memberRepository.saveAll(members);

            System.out.println("회원 20명 생성 완료");

            // 게시글 100,000개 생성 (카테고리별로 50,000개씩)
            // 한 번에 너무 많은 데이터를 저장하면 메모리 문제가 발생할 수 있으므로 배치 처리
            int batchSize = 1000;
            int totalPosts = 100000;
            int totalBatches = totalPosts / batchSize;

            for (int batch = 0; batch < totalBatches; batch++) {
                List<Post> posts = new ArrayList<>();
                int startIdx = batch * batchSize;

                for (int i = 0; i < batchSize; i++) {
                    int currentPostIndex = startIdx + i;
                    PostCategory category = (currentPostIndex < totalPosts / 2) ?
                            PostCategory.WALKTHROUGH : PostCategory.RECOMMENDATION;

                    String gameTitle = getRandomElement(gameNames);
                    String titleBase = (category == PostCategory.WALKTHROUGH) ?
                            getRandomElement(walkthroughTitles) :
                            getRandomElement(recommendationTitles);

                    String title = gameTitle + " " + titleBase + " " + random.nextInt(1000);

                    posts.add(createPost(
                            getRandomElement(members),
                            title,
                            generatePostContent(),
                            category));
                }

                postRepository.saveAll(posts);
                System.out.println("게시글 " + (batch + 1) + "번째 배치 (" + batchSize + "개) 생성 완료 - 총 " + (startIdx + batchSize) + "개");
            }

            System.out.println("총 게시글 " + totalPosts + "개 생성 완료!");
            System.out.println("주의: 대량의 데이터 생성 작업이므로 시간이 오래 걸릴 수 있습니다.");

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