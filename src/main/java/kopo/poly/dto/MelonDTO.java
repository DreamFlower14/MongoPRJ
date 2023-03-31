package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 변수에 저장된 값이 기본값(String)이 아닌 경우에 제외한다
 * jackson 라이브러리는 int 와 같은 숫자 데이터타입에 데이터가 저장되지 않아 발생하는 NUll 오류를 방지하기 위해
 * 값이 없으면 0으로 저장한다.
 * Include.NON_NULL 은 값이 NULL 인 것만 방지하기  때문에 숫자 타입은 출력되는 문제가 있음
 * */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class MelonDTO {

    String collectTime; // 수집 시간

    String song; // 노래 제목

    String singer; // 가수

    int singerCnt; // 차트에 등록된 가수별 노래 수

    String updateSinger; // 수정할 가수명

    String nickname; // 추가될 닉네임

    List<String> member; // 추가될 그룹 멤버 이름들

    String addFieldValue; // 추가될 필드 값

    int Rank; // 노래 순위
}
