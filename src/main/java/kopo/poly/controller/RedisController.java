package kopo.poly.controller;

import kopo.poly.dto.RedisDTO;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/redis")
@RequiredArgsConstructor
@Slf4j
public class RedisController {

    private final IMyRedisService myRedisService;

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     * */
    @PostMapping(value = "saveList")
    public RedisDTO saveList(HttpServletRequest request) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 가수 그룹 이름은 여러 명 입력될 수 있기에 배열로 받음
        // 배열로 받는 바업 : <input type="text" name="text" /> 의 name 속성 값이 동일하면 배열로 받아진다
        String[] text = request.getParameterValues("text");

        // String[] 구조를 List<String> 구조로 변환하기
        List<String> texts = Arrays.asList(text);

        RedisDTO pDTO = new RedisDTO();
        pDTO.setTexts(texts);

        // optional -> NPE 처리
        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveList(pDTO)).orElseGet(RedisDTO::new);

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rDTO;
    }

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     * */
    @PostMapping(value = "saveListJSON")
    public List<RedisDTO> saveListJSON(HttpServletRequest request) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 배열로 받는 바업 : <input type="text" name="text" /> 의 name 속성 값이 동일하면 배열로 받아진다
        String[] name = request.getParameterValues("name");
        String[] email = request.getParameterValues("email");
        String[] addr = request.getParameterValues("addr");

        // String[] 구조를 List<String> 구조로 변환하기
        List<RedisDTO> pList = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            RedisDTO pDTO = new RedisDTO();
            pDTO.setName(name[i]);
            pDTO.setEmail(email[i]);
            pDTO.setAddr(addr[i]);

            pList.add(pDTO);

            pDTO = null;
        }

        // optional -> NPE 처리
        List<RedisDTO> rList = Optional.ofNullable(myRedisService.saveListJSON(pList)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rList;
    }

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     * */
    @PostMapping(value = "saveHash")
    public RedisDTO saveHash(HttpServletRequest request) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String addr = request.getParameter("addr");

        log.info("name : " + name);
        log.info("email : " + email);
        log.info("addr : " + addr);

        RedisDTO pDTO = new RedisDTO();

        pDTO.setName(name);
        pDTO.setEmail(email);
        pDTO.setAddr(addr);

        // optional -> NPE 처리
        RedisDTO rDTO = Optional.ofNullable(myRedisService.saveHash(pDTO)).orElseGet(RedisDTO::new);

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rDTO;
    }

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     * */
    @PostMapping(value = "saveSetJSON")
    public Set<RedisDTO> saveSetJSON(HttpServletRequest request) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 배열로 받는 바업 : <input type="text" name="text" /> 의 name 속성 값이 동일하면 배열로 받아진다
        String[] name = request.getParameterValues("name");
        String[] email = request.getParameterValues("email");
        String[] addr = request.getParameterValues("addr");

        // 중복된 데이터를 입력받을 수 있으며, RedisDb 의 set 구조가 중복제거 하는지 테스트하기 위해
        // 중복된 데이터가 저장가능한 List 구조로 데이터를 저장함
        List<RedisDTO> pList = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            RedisDTO pDTO = new RedisDTO();
            pDTO.setName(name[i]);
            pDTO.setEmail(email[i]);
            pDTO.setAddr(addr[i]);

            pList.add(pDTO);

            pDTO = null;
        }

        // optional -> NPE 처리
        Set<RedisDTO> rSet = Optional.ofNullable(myRedisService.saveSetJSON(pList)).orElseGet(HashSet::new);

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rSet;
    }

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     * */
    @PostMapping(value = "saveZSetJSON")
    public Set<RedisDTO> saveZSetJSON(HttpServletRequest request) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 배열로 받는 바업 : <input type="text" name="order" /> 의 name 속성 값이 동일하면 배열로 받아진다
        String[] order = request.getParameterValues("order");
        String[] name = request.getParameterValues("name");
        String[] email = request.getParameterValues("email");
        String[] addr = request.getParameterValues("addr");

        // 중복된 데이터를 입력받을 수 있으며, RedisDb 의 set 구조가 중복제거 하는지 테스트하기 위해
        // 중복된 데이터가 저장가능한 List 구조로 데이터를 저장함
        List<RedisDTO> pList = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            RedisDTO pDTO = new RedisDTO();
            pDTO.setOrder(Float.parseFloat(order[i]));
            pDTO.setName(name[i]);
            pDTO.setEmail(email[i]);
            pDTO.setAddr(addr[i]);

            pList.add(pDTO);

            pDTO = null;
        }

        // optional -> NPE 처리
        Set<RedisDTO> rSet = Optional.ofNullable(myRedisService.saveZSetJSON(pList)).orElseGet(HashSet::new);

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rSet;
    }
}
