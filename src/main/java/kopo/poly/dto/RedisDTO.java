package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class RedisDTO {

    private String name;
    private String email;
    private String addr;
    private String text;    // 테스트 문구
    private float order;    // 저장순서

    private List<String> texts; // 여러개 문자열 저장
}
