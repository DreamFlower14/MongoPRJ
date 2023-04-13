package kopo.poly.service.impl;

import kopo.poly.dto.RedisDTO;
import kopo.poly.persistance.redis.IMyRedisMapper;
import kopo.poly.service.IMyRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyRedisService implements IMyRedisService {

    // Mapper 객체 가져오기
    private final IMyRedisMapper myRedisMapper;


    @Override
    public RedisDTO saveList(RedisDTO pDTO) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 저장할 RedisKey
        String redisKey = "myRedis_List";

        // 저장 결과
        RedisDTO rDTO = null;

        int res = myRedisMapper.saveList(redisKey, pDTO);

        if (res == 1) {  // 저장에 성공하면
            log.info("Redis 저장 성공!!");
            rDTO = myRedisMapper.getList(redisKey);
        } else {
            log.info("Redis 저장 실패!!");
            new Exception("Redis 저장 실패!!");
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rDTO;
    }

    @Override
    public List<RedisDTO> saveListJSON(List<RedisDTO> pList) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 저장할 RedisKey
        String redisKey = "myRedis_List_JSON";

        // 저장 결과
        List<RedisDTO> rList = null;

        int res = myRedisMapper.saveListJSON(redisKey, pList);

        if (res == 1) {  // 저장에 성공하면
            log.info("Redis 저장 성공!!");
            rList = myRedisMapper.getListJSON(redisKey);
        } else {
            log.info("Redis 저장 실패!!");
            new Exception("Redis 저장 실패!!");
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rList;
    }

    @Override
    public RedisDTO saveHash(RedisDTO pDTO) throws Exception {

        String functionName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 저장할 RedisKey
        String redisKey = "myRedis_Hash";

        // 저장 결과
        RedisDTO rDTO = null;

        int res = myRedisMapper.saveHash(redisKey, pDTO);

        if (res == 1) {  // 저장에 성공하면
            log.info("Redis 저장 성공!!");
            rDTO = myRedisMapper.getHash(redisKey);
        } else {
            log.info("Redis 저장 실패!!");
            new Exception("Redis 저장 실패!!");
        }

        log.info(this.getClass().getName() + "." + functionName + " 끝!!");

        return rDTO;
    }

    @Override
    public Set<RedisDTO> saveSetJSON(List<RedisDTO> pList) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 저장할 RedisKey
        String redisKey = "myRedis_Set_JSON";

        // 저장 결과
        Set<RedisDTO> rSet = null;

        int res = myRedisMapper.saveSetJSON(redisKey, pList);

        if (res == 1) {  // 저장에 성공하면
            log.info("Redis 저장 성공!!");
            rSet = myRedisMapper.getSetJSON(redisKey);
        } else {
            log.info("Redis 저장 실패!!");
            new Exception("Redis 저장 실패!!");
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rSet;
    }

    @Override
    public Set<RedisDTO> saveZSetJSON(List<RedisDTO> pList) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 저장할 RedisKey
        String redisKey = "myRedis_ZSet_JSON";

        // 저장 결과
        Set<RedisDTO> rSet = null;

        int res = myRedisMapper.saveZSetJSON(redisKey, pList);

        if (res == 1) {  // 저장에 성공하면
            log.info("Redis 저장 성공!!");
            rSet = myRedisMapper.getZSetJSON(redisKey);
        } else {
            log.info("Redis 저장 실패!!");
            new Exception("Redis 저장 실패!!");
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rSet;
    }
}
