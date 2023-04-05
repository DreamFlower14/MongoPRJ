package kopo.poly.persistance.mongodb;


import kopo.poly.dto.MelonDTO;

import java.util.List;

public interface IMelonMapper {

    /**
     * 멜론 노래 리스트 저장하기
     *
     * @param plist 저장될 정보
     * @param colNm 저장할 컬렉션 이름
     * @return 저장 결과
     */
    int insertSong(List<MelonDTO> plist, String colNm) throws Exception;

    /**
     * 오늘 수집된 멜론 리스트 가져오기
     *
     * @param colNm 조회할 컬렉션 이름
     * @return 노래 리스트
     */
    List<MelonDTO> getSongList(String colNm) throws Exception;

    /**
     * 가수별 수집한 노래의 수 가져오기
     *
     * @param colNm 조회할 컬렉션 이름
     * @return 노래 리스트
     */
    List<MelonDTO> getSingerSongCnt(String colNm) throws Exception;

    /**
     * 가수 이름으로 조회하기
     *
     * @param colNm 조회할 컬렉션 이름
     * @param pDTO  가수명
     * @return 노래 리스트
     */
    List<MelonDTO> getSingerSong(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 컬렉션 삭제하기
     *
     * @param colNm 삭제할 컬렉션 이름
     * @return 저장 결과
     */
    int dropCollection(String colNm) throws Exception;

    /**
     * MongoDB insertMany 함수를 통해 멜로차트 저장하기
     * 한건 insert 대비 속도가 빠름
     *
     * @param colNm 저장할 컬렉션 이름
     * @param plist 저장될 정보
     * @return 저장결과
     */
    int insertManyField(String colNm, List<MelonDTO> plist) throws Exception;

    /**
     * 필드 값 수정하기
     * 예 : 가수의 이름 수정하기
     *
     * @param colNm 저장할 컬렉션 이름
     * @param pDTO  가수명
     * @return 노래 리스트
     */
    int updateField(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 수정된 가수 이름의 노래 가져오기
     *
     * @param colNm 조회할 컬렉션 이름
     * @param pDTO  가수명
     * @return 노래 리스트
     */
    List<MelonDTO> getUpdateSinger(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 필드 추가히기
     * 예 : 가수의 NICKNAME 필드 추가하기
     *
     * @param colNm 저장할 컬렉션 이름
     * @param pDTO  추가를 위해 검색할 가수이름, 추가할 서브 가수 이름
     * @return 저장 결과
     */
    int updateAddField(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수의 노래 가져오기
     *
     * @param colNm 조회할 컬렉션 이름
     * @param pDTO  가수명
     * @return 노래 리스트
     */
    List<MelonDTO> getSingerSongNickName(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수의 List 구조의 Member 필드 추가하기
     *
     * @param colNm 저장할 컬렉션 이름
     * @param pDTO  추가를 위해 검색할 가수 이름, 추가할 멤버 이름
     * @return 저장 결과
     */
    int updateAddListField(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수의 노래 가져오기 (멤버 포함 조회)
     *
     * @param colNm 조회할 컬렉션 이름
     * @param pDTO  가수명
     * @return 노래 리스트
     */
    List<MelonDTO> getSingerSongMember(String colNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수 이름, 노래 제목 수정 및 신규 필드 추가(복합 수정하기)
     *
     * @param pColNm 저장할 컬렉션 이름
     * @param pDTO  수정할 가수 이름, 수정될 노래 제목, 추가 필드 값
     * @return 저장 결과
     */
    int updateFieldAndAddField(String pColNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수의 노래 가져오기 (임의 추가한 필드 포함 조회)
     *
     * @param pColNm 조회할 컬렉션
     * @param pDTO
     * @return 노래 리스트
     */
    List<MelonDTO> getSingerSongAddData(String pColNm, MelonDTO pDTO) throws Exception;

    /**
     * 가수의 노래 삭제하기
     *
     * @param pColNm 저장할 컬렉션 이름
     * @param pDTO   삭제할 가수 이름
     * @return 저장 결과
     */
    int deleteDocument(String pColNm, MelonDTO pDTO) throws Exception;
}
