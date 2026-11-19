package com.human.mini_prj.rag;

import com.human.mini_prj.rag.dto.IndexReqDto;
import com.human.mini_prj.rag.dto.IndexResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Supplier;

// FastAPI 호출 담당
@Component // Spring Container에 Bean 등록
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 자동으로 만들어 줌
public class RagClient {
    private final RestClient restClient;

    public int index(IndexReqDto req) {

    }

    private <T> T call(String action, Supplier<T> request) {
        try {
            return request.get();
        } catch (ResourceAccessException e) {          // 연결 실패 · 시간 초과
            throw new RagUnavailableException("AI 서비스에 연결할 수 없습니다.", e);
        } catch (RestClientResponseException e) {      // 401 · 422 · 502 등 오류 응답
            throw new RagUnavailableException("AI 서비스 " + action + " 오류 (" + e.getStatusCode().value() + ")", e);
        } catch (RestClientException e) {
            throw new RagUnavailableException("AI 서비스 " + action + " 실패", e);
        }
    }

}
