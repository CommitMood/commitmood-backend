package com.ssafy.commitmood.dto;

import com.ssafy.commitmood.domain.CommitLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GithubCommitDtoMapper {

//    public CommitLog toEntity(GithubCommitDto dto, Long repoId, Long authorId) {
//        if (dto == null) {
//            log.warn("dto is null");
//            return null;
//        }
//
//        return CommitLog.builder()
//                .repoId(repoId)
//                .authorId(authorId)
//                .githubCommitSha(dto.getSha())
//                .committedAt(committedAt)
//                .message(message)
//                .htmlUrl(dto.getHtmlUrl())
//                .additions(additions)
//                .deletions(deletions)
//                .totalChanges(totalChanges)
//                .filesChanged(null)  // 파일 변경 수는 별도 API로 가져와야 함
//                .build();
//    }
}
