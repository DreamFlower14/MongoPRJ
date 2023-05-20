package kopo.poly.persistance.redis;

import kopo.poly.dto.RedisDTO;

import java.util.List;
import java.util.Set;

public interface IMyRedisMapper {

    /**
     * String 타입 저장하기
     *
     * @param redisKey Redis 저장 키
     * @param pDTO     저장할 여부
     * @return 저장 성공 여부
     */
    int saveString(String redisKey, RedisDTO pDTO) throws Exception;

    /**
     * String 타입 가져오기
     *
     * @param redisKey 가져올 RedisKey
     * @return 결과값
     */
    RedisDTO getString(String redisKey) throws Exception;

    /**
     * List 타입에 여러 문자열로 저장하기 (동기화)
     *
     * @param redisKey redis 저장 키
     * @param pDTO     저장할 정보들
     * @return 저장 성공 여부
     */
    int saveList(String redisKey, RedisDTO pDTO) throws Exception;

    /**
     * LIst 타입에 어려 문자열로 저장된 데이터 가져오기
     *
     * @param redisKey 가져올 RedisKEy
     * @return 결과 값
     */
    RedisDTO getList(String redisKey) throws Exception;

    /**
     * List 타입에 Json 형태로 저장하기
     *
     * @param redisKey redis 저장키
     * @param pList    저장할 정보들
     * @return 저장 성공 여부
     */
    int saveListJSON(String redisKey, List<RedisDTO> pList) throws Exception;

    /**
     * List 타입에 JSON 형태로 저장된 데이터 가져오기
     *
     * @param redisKey 가져올 redisKey
     * @return 결과 값
     */
    List<RedisDTO> getListJSON(String redisKey) throws Exception;

    /**
     * Hash 타입에 문자열 형태로 저장하기
     *
     * @param redisKey 저장 키
     * @param pDTO     저장할 정보들
     * @retunr 저장성공 여부
     */
    int saveHash(String redisKey, RedisDTO pDTO) throws Exception;

    /**
     * Hash 타입 저장한 정보 가져오기
     *
     * @param redisKey 저장키
     * @return 저장된 정보
     */
    RedisDTO getHash(String redisKey) throws Exception;

    /**
     * Set 타입에 JSON 형태로 람다식을 이용하여 저장하기
     *
     * @param redisKey redis 저장 키
     * @param pList    저장할 정보
     * @return 저장성공 여부
     */
    int saveSetJSON(String redisKey, List<RedisDTO> pList) throws Exception;

    /**
     * set 타입에 JSON 형태로 람다식을 이용하여 저장된 값 가져오기
     *
     * @param redisKey 가져올 redisKey
     * @return 결과값
     */
    Set<RedisDTO> getSetJSON(String redisKey) throws Exception;

    /**
     * ZSet 타입에 jSON 형태로 저장하기
     *
     * @param redisKey redis 저장키
     * @param pList    저장할 정보들
     * @return 저장 성공 여부
     */
    int saveZSetJSON(String redisKey, List<RedisDTO> pList) throws Exception;

    /**
     * ZSet 타입에 JSON 형태로 저장된 값 가져오기
     *
     * @param redisKey 가져올 redisKey
     * @return
     */
    Set<RedisDTO> getZSetJSON(String redisKey) throws Exception;
}
