package kopo.poly.controller;

import kopo.poly.dto.MelonDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMelonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
     * */
    @PostMapping(value = "getSingerSongCnt")
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt 시작!!");

        List<MelonDTO> rList = Optional.ofNullable(melonService.getSingerSongCnt()).orElseGet(ArrayList::new);

        log.info(this.getClass().getName() + ".getSingerSongCnt 끝!!");

        return rList;
    }
}
