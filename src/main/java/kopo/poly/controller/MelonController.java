package kopo.poly.controller;

import kopo.poly.dto.MelonDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/melon")
@RestController
public class MelonController {

    private final IMelonService melonService;

    /**
     * 멜론 노래 리스트 저장하기
     */
    @PostMapping(value = "collectMelonSong")
    public MsgDTO collectMelonSong() throws Exception {

        log.info(this.getClass().getName() + ".collectMelonSong 시작!!");

        // 수집 결과 출력
        String msg = "";

        int res = melonService.collectMelonSong();

        if (res == 1) {
            msg = "멜론차트 수집 성공!";
        } else {
            msg = "멜론차트 수집 실패..";
        }

        // 전달할 결과 구조 만들기
        MsgDTO dto = new MsgDTO();
        dto.setResult(res);
        dto.setMsg(msg);

        log.info(this.getClass().getName() + ".collectMelonSong 끝!!");

        return dto;
    }

    /**
     * 오늘 수집된 멜론 노래 리스트 가져오기
     */
    @PostMapping(value = "getSongList")
    public List<MelonDTO> getSongList() throws Exception {

        log.info(this.getClass().getName() + ".getSongList 시작!!");

        //  Java 8 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.getSongList()).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSongList 끝!!");

        return rList;
    }


    /**
     * 가수별 수집된 노래의 수 가져오기
     */
    @PostMapping(value = "getSingerSongCnt")
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt 시작!!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSongCnt()).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSingerSongCnt 끝!!");

        return rList;
    }

    /**
     * 가수 이름으로 조회하기
     */
    @PostMapping(value = "getSingerSong")
    public List<MelonDTO> getSingerSong(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getSingerSong 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer")); // 가수

        log.info("singer : " + singer);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSong(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSingerSong 끝!!");

        return rList;
    }

    /**
     * 수집된 멜론 차트 컬렉션 삭제하기
     */
    @PostMapping(value = "dropCollection")
    public MsgDTO dropCollection() throws Exception {

        log.info(this.getClass().getName() + ".dropCollection 시작!!");

        // 삭제 결과 출력
        String msg = "";

        int res = melonService.dropCollection();

        if (res == 1) {
            msg = "멜론차트 삭제 성공!!";
        } else {
            msg = "멜론차트 삭제 실패..";
        }

        // 전달할 결과 구조 만들기
        MsgDTO dto = new MsgDTO();
        dto.setResult(res);
        dto.setMsg(msg);

        log.info(this.getClass().getName() + ".dropCollection 끝!!");

        return dto;
    }

    /**
     * 멜론 노래 리스트 저장하기
     */
    @PostMapping(value = "insertManyField")
    public List<MelonDTO> insertManyField() throws Exception {

        log.info(this.getClass().getName() + ".insertManyField 시작!!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.insertManyField()).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".insertManyField 끝!!");

        return rList;
    }

    /**
     * 가수 이름 수정하기
     * 예 : 방탄소년단을 BTS 로 변경하기
     */
    @PostMapping(value = "updateField")
    public List<MelonDTO> updateField(HttpServletRequest request) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + name + ". 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer")); // 수정될 가수
        String updateSinger = CmmUtil.nvl(request.getParameter("updateSinger"));

        log.info("updateSinger : " + updateSinger);
        log.info("Singer : " + singer);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);
        pDTO.setUpdateSinger(updateSinger);

        // java8 부터 제공되는 optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.updateField(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + name + " 끝!!");

        return rList;
    }

    /**
     * 가수 별명 추가하기
     * 예 : 방탄 소년단을 BTS 별명 추가하기
     */
    @PostMapping(value = "updateAddField")
    public List<MelonDTO> updateAddField(HttpServletRequest request) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer"));
        String nickname = CmmUtil.nvl(request.getParameter("nickname"));

        log.info("singer : " + singer);
        log.info("nickname : " + nickname);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);
        pDTO.setNickname(nickname);

        List<MelonDTO> rList = Optional.ofNullable(melonService.updateAddField(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rList;
    }

    /**
     * 가수 멤버 이름들 (List 구조 필드 ) 추가하기
     * */
    @PostMapping(value = "updateAddListField")
    public List<MelonDTO> updateAddListField(HttpServletRequest request) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer")); // 필드를 추가할 가수

        // 가수 그룹 이름은 여러명 입력될 수 있기에 배열로 받음
        // 배열로 받는 방법 : <input type = "text" name="member" /> 의 name 속성 값이 동일하면 배열로 받아짐
        String[] member = request.getParameterValues("member");

        // String[] 구조를 List<String> 구조로 변환가히
        List<String> memberList = Arrays.asList(member);

        log.info("singer : " + singer);
        log.info("member : " + member);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);
        pDTO.setMember(memberList);

        // java 8부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rlist = Optional.ofNullable(melonService.updateAddListField(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rlist;
    }

    /**
     * 가수 이름이 방탄소년단인 것을 BTS 로 변경 및 필드 추가하기
     * */
    @PostMapping(value = "updateFieldAndAddField")
    public List<MelonDTO> updateFieldAndAddField(HttpServletRequest request) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer")); // 필드를 추가할 가수
        String updateSinger = CmmUtil.nvl(request.getParameter("updateSinger")); // 필드를 추가할 가수
        String addData = CmmUtil.nvl(request.getParameter("addData")); // 필드를 추가할 가수

        log.info("singer : " + singer);
        log.info("updateSinger : " + updateSinger);
        log.info("addData : " + addData);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);
        pDTO.setUpdateSinger(updateSinger);
        pDTO.setAddFieldValue(addData);

        // java 8부터 제공되는 Optional 활용하여 NPE 처리
        List<MelonDTO> rlist = Optional.ofNullable(melonService.updateFieldAndAddField(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rlist;
    }

    /**
     * 가수 이름이 방탄소년단인 노래 삭제하기
     */
    @PostMapping(value = "deleteDocument")
    public List<MelonDTO> deleteDocument(HttpServletRequest request) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + name + ". 시작!!");

        String singer = CmmUtil.nvl(request.getParameter("singer")); // 수정될 가수

        log.info("Singer : " + singer);

        MelonDTO pDTO = new MelonDTO();
        pDTO.setSinger(singer);

        // java8 부터 제공되는 optional 활용하여 NPE 처리
        List<MelonDTO> rList = Optional.ofNullable(melonService.deleteDocument(pDTO)).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + name + " 끝!!");

        return rList;
    }

}
