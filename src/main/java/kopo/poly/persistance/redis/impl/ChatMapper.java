package kopo.poly.persistance.redis.impl;

import kopo.poly.dto.ChatDTO;
import kopo.poly.persistance.redis.IChatMapper;
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

@RequiredArgsConstructor
@Slf4j
@Component
public class ChatMapper implements IChatMapper {

    public final RedisTemplate<String, Object> redisDB;

    @Override
    public Set<String> getRoomList() throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 이름이 chat 으로 시작하는 Key 만 가져오기
        Set<String> rSet = (Set) redisDB.keys("Chat*");

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rSet;
    }

    @Override
    public int insertChat(ChatDTO pDTO) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        int res = 0;

        // 대화방 키 정보
        String roomKey = CmmUtil.nvl(pDTO.getRoomKey());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDTO.class));

        redisDB.opsForList().leftPush(roomKey, pDTO);

        res = 1;

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return res;
    }

    @Override
    public List<ChatDTO> getChat(ChatDTO pDTO) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // redis 에서 가져온 결과 저장할 객체
        List<ChatDTO> rList = null;

        // 대화방 키 정보
        String roomKey = CmmUtil.nvl(pDTO.getRoomKey());

        redisDB.setKeySerializer(new StringRedisSerializer());
        redisDB.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDTO.class));

        if (redisDB.hasKey(roomKey)) {
            // 저장된 정체 레코드 가져오기
            rList = (List) redisDB.opsForList().range(roomKey, 0, -1);


        }

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rList;
    }

    @Override
    public boolean setTimeOutHour(String roomKey, int hours) throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");
        return redisDB.expire(roomKey, hours, TimeUnit.HOURS);
    }

    @Override
    public boolean setTimeOutMinute(String roomKey, int minutes) throws Exception {
        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");
        return redisDB.expire(roomKey, minutes, TimeUnit.MINUTES);
    }
}
