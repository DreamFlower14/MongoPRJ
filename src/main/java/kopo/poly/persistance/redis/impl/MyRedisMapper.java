package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyRedisMapper implements IMyRedisMapper {

    private final RedisTemplate<String, Object> redisDB;

    @Override
    public int saveString(String redisKey, RedisDTO pDTO) throws Exception {
        return 0;
    }

    @Override
    public RedisDTO getString(String redisKey) throws Exception {
        return null;
    }

    @Override
    public int saveList(String redisKey, RedisDTO pDTO) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입
        redisDB.setValueSerializer(new StringRedisSerializer()); // String 타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면
            redisDB.delete(redisKey);   // 삭제하기

            log.info("redisKey 가 존재해서 기존 값을 삭제하였습니다.");
        }

        List<String> tests = pDTO.getTexts(); // 저장될 문자열

        for (String text : tests) { // List 에 저장된 문자열들을 Redis list 객체에 저장

            // 오름 차순으로 저장하기
            // redisDB.opsForList().rightPush(redisKey, CmmUtil.nvl(text));

            // 내림차순으로 저장하기
            redisDB.opsForList().leftPush(redisKey, CmmUtil.nvl(text));
        }

        //  저장되는 데이터의 유효기간은 5시간으로 정의
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return res;
    }

    @Override
    public RedisDTO getList(String redisKey) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 결과 값 저장할 객체
        RedisDTO rDTO = new RedisDTO();

        // redis 저장 및 일기에 대한 데이터 타입 지정 (String 타입)
        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new StringRedisSerializer());

        if (redisDB.hasKey(redisKey)) {
            List<String> rList = (List) redisDB.opsForList().range(redisKey, 0, -1);

            rDTO.setTexts(rList);
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rDTO;
    }

    @Override
    public int saveListJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면
            redisDB.delete(redisKey);   // 삭제하기

            log.info("redisKey 가 존재해서 기존 값을 삭제하였습니다.");
        }

        // 오름 차순으로 저장하기
        pList.forEach(dto -> redisDB.opsForList().rightPush(redisKey, dto));

        //  저장되는 데이터의 유효기간은 5시간으로 정의
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return res;
    }

    @Override
    public List<RedisDTO> getListJSON(String redisKey) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 결과 값 저장할 객체
        List<RedisDTO> rList = null;

        // redis 저장 및 일기에 대한 데이터 타입 지정 (String 타입)
        redisDB.setKeySerializer(new StringRedisSerializer());

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입


        if (redisDB.hasKey(redisKey)) {
            rList = (List) redisDB.opsForList().range(redisKey, 0, -1);
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rList;
    }

    @Override
    public int saveHash(String redisKey, RedisDTO pDTO) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입
        redisDB.setHashKeySerializer(new StringRedisSerializer());  // hash 구조의 키 타입
        redisDB.setHashValueSerializer(new StringRedisSerializer());    // hash 구조의 값 타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면
            redisDB.delete(redisKey);   // 삭제하기

            log.info("redisKey 가 존재해서 기존 값을 삭제하였습니다.");
        }

        redisDB.opsForHash().put(redisKey, "name", CmmUtil.nvl(pDTO.getName()));
        redisDB.opsForHash().put(redisKey, "email", CmmUtil.nvl(pDTO.getEmail()));
        redisDB.opsForHash().put(redisKey, "addr", CmmUtil.nvl(pDTO.getAddr()));

        //  저장되는 데이터의 유효기간은 5시간으로 정의
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return res;
    }

    @Override
    public RedisDTO getHash(String redisKey) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 결과 값 저장할 객체
        RedisDTO rDTO = new RedisDTO();

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입
        redisDB.setHashKeySerializer(new StringRedisSerializer());  // hash 구조의 키 타입
        redisDB.setHashValueSerializer(new StringRedisSerializer());    // hash 구조의 값 타입


        if (redisDB.hasKey(redisKey)) {
            String name = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "name"));
            String email = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "email"));
            String addr = CmmUtil.nvl((String) redisDB.opsForHash().get(redisKey, "addr"));

            log.info("name : " + name);
            log.info("email : " + email);
            log.info("addr : " + addr);

            rDTO.setName(name);
            rDTO.setEmail(email);
            rDTO.setAddr(addr);
        }

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rDTO;
    }

    @Override
    public int saveSetJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면
            redisDB.delete(redisKey);   // 삭제하기

            log.info("redisKey 가 존재해서 기존 값을 삭제하였습니다.");
        }

        // 오름 차순으로 저장하기
        pList.forEach(dto -> redisDB.opsForSet().add(redisKey, dto));

        //  저장되는 데이터의 유효기간은 5시간으로 정의
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return res;
    }

    @Override
    public Set<RedisDTO> getSetJSON(String redisKey) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 결과 값 저장할 객체
        Set<RedisDTO> rSet = null;

        // redis 저장 및 일기에 대한 데이터 타입 지정 (String 타입)
        redisDB.setKeySerializer(new StringRedisSerializer());

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입
        
        if (redisDB.hasKey(redisKey)) {
            rSet = (Set) redisDB.opsForSet().members(redisKey); // redis 데이터 조회하기
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rSet;
    }

    @Override
    public int saveZSetJSON(String redisKey, List<RedisDTO> pList) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        // redis 저장 및 읽기 에 대한 데이터 타입 지정
        redisDB.setKeySerializer(new StringRedisSerializer());  // String 타입

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입

        if (redisDB.hasKey(redisKey)) { // 데이터가 존재하면
            redisDB.delete(redisKey);   // 삭제하기

            log.info("redisKey 가 존재해서 기존 값을 삭제하였습니다.");
        }

        for (RedisDTO dto : pList) {
            redisDB.opsForZSet().add(redisKey, dto, dto.getOrder()); // 저장순서는 사용자가 입력한 order 값에 따름
        }

        //  저장되는 데이터의 유효기간은 5시간으로 정의
        redisDB.expire(redisKey, 5, TimeUnit.HOURS);

        res = 1;

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return res;
    }

    @Override
    public Set<RedisDTO> getZSetJSON(String redisKey) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 결과 값 저장할 객체
        Set<RedisDTO> rSet = null;

        // redis 저장 및 일기에 대한 데이터 타입 지정 (String 타입)
        redisDB.setKeySerializer(new StringRedisSerializer());

        // redisDTO 에 저장된 데이터를 자동으로 JSON 으로 변경하기
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisDTO.class)); // String 타입

        if (redisDB.hasKey(redisKey)) {
            rSet = (Set) redisDB.opsForZSet().range(redisKey,0 , -1); // redis 데이터 조회하기
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rSet;    }
}
