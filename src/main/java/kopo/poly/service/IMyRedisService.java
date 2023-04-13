package kopo.poly.service;

import kopo.poly.dto.RedisDTO;

import java.util.List;
import java.util.Set;

public interface IMyRedisService {

    /**
     * String 타입 저장 및 가져오기
     */
    RedisDTO saveList(RedisDTO pDTO) throws Exception;

    /**
     * List 타입에 json 형태로 저장하기
     */
    List<RedisDTO> saveListJSON(List<RedisDTO> pList) throws Exception;

    /**
     * hash 타입에 문자열 형태로 저장하기
     */
    RedisDTO saveHash(RedisDTO pDTO) throws Exception;

    /**
     * Set 타입에 Json 형태로 저장하기
     */
    Set<RedisDTO> saveSetJSON(List<RedisDTO> pList) throws Exception;

    /**
     * ZSet 타입에 JSOn 형태로 저장하기
     */
    Set<RedisDTO> saveZSetJSON(List<RedisDTO> pList) throws Exception;
}
