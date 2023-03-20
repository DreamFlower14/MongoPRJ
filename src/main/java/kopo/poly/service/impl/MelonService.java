package kopo.poly.service.impl;

import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.service.IMelonService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MelonService implements IMelonService {

    private final IMelonMapper melonMapper; // MongoDB에 저장할 Mapper

    /**
     * 멜론 차트 크롤링 함수
     *
     * */
    private List<MelonDTO> doCollect() throws Exception {

        log.info(this.getClass().getName() + ".doCollect 시작!!");

        List<MelonDTO> pList = new LinkedList<>();

        // 멜론 TOP100 중 50위까지 정보 가져오는 페이지
        String url = "https://www.melon.com/chart/index.htm";

        // JSoup 라이브러리를 통해 사이트 접속되면 그 사이트의 전체 HTML 소스 저장할 변수
        Document doc = Jsoup.connect(url).get();
        log.info("전체 소스 : " + doc);

        // <div class="service_list_song"> 이 태그 내에서 있는 HTML 소스만 element 에 저장됨
        Elements element = doc.select("div.service_list_song");
        log.info("html 소스 : " + element);

        // Iterator 을 사용하여 멜론차트 정보를 가져오기
        for (Element songInfo : element.select("div.wrap_song_info")) {

            // 크롤링을 통해 데이터 저장하기
            String song = CmmUtil.nvl(songInfo.select("div.ellipsis.rank01 a").text());
            String singer = CmmUtil.nvl(songInfo.select("div.ellipsis.rank02 a").eq(0).text());

            log.info("song : " + song);
            log.info("singer : " + singer);

            // 가수와 노래 정보가 모두 수집되었다면 저장함
            if ((song.length() > 0) && (singer.length() > 0)) {

                MelonDTO pDTO = new MelonDTO();
                pDTO.setCollectTime(DateUtil.getDateTime("yyyyMMddhhmmss"));
                pDTO.setSong(song);
                pDTO.setSinger(singer);

                // 한번에 여러개의 데이터를 MongoDB 에 저장할 List 형태의 데이터 저장하기
                pList.add(pDTO);

            }

        }
        log.info(this.getClass().getName() + ".doCollect 끝!!");

        return pList;
    }


    @Override
    public int collectMelonSong() throws Exception {

        log.info(this.getClass().getName() + ".CollectMelonSong 시작!!");

        int res = 0;

        // 생성할 컬렉션명
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        // private 함수로 선언된 doCollect 함수를 호출하여 결과받기
        List<MelonDTO> rList = this.doCollect();

        // MongoDB 에 데이터 저장하기
        res = melonMapper.insertSong(rList, colNm);

        log.info(this.getClass().getName() + ".CollectMelonSong 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getSongList() throws Exception {

        log.info(this.getClass().getName() + ".getSongList 시작!!");

        // MongoDB 에 저장된 컬렉션 이름
        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = null;

        rList = melonMapper.getSongList(colNm);

        log.info(this.getClass().getName() + ".getSongList 끝!!");

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt() throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt 시작!!");

        String colNm = "MELON_" + DateUtil.getDateTime("yyyyMMdd");

        List<MelonDTO> rList = melonMapper.getSingerSongCnt(colNm);

        log.info(this.getClass().getName() + ".getSingerSongCnt 끝!!");

        return rList;
    }
}
