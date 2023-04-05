package kopo.poly.persistance.mongodb.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import kopo.poly.dto.MelonDTO;
import kopo.poly.persistance.mongodb.IMelonMapper;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.mongodb.client.model.Updates.set;

@Slf4j
@Component
@RequiredArgsConstructor
public class MelonMapper extends AbstractMongoDBComon implements IMelonMapper {

    private final MongoTemplate mongodb;

    @Override
    public int insertSong(List<MelonDTO> plist, String colNm) throws Exception {
        log.info(this.getClass().getName() + ".insertSong 시작!!");

        int res = 0;

        if (plist == null) {    //
            plist = new LinkedList<>();
        }

        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, colNm, "collectTime");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection(colNm);

        for (MelonDTO pDTO : plist) {
            if (pDTO == null) {
                pDTO = new MelonDTO();
            }

            // 레코드 한개씩 저장하기
            col.insertOne(new Document(new ObjectMapper().convertValue(pDTO, Map.class)));

        }

        res = 1;

        log.info(this.getClass().getName() + ".insertSong 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getSongList(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getSongList 시작!!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(colNm);

        // 조회 결과 중 출력할 컬럼들 (SQL 의 Select 절과 From 절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("rank", "$rank");
        projection.append("song", "$song");
        projection.append("singer", "$singer");

        // MongoDB 는 무조건 ObjectId 가 자동생성되며, ObjectID 는 사용하지 않을 때, 조회할 필요가 없음
        // ObjectID 를 가져오지 않을 땐 실제 값자리에 0을 넣음
        projection.append("_id", 0);

        // MongoDB의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우, find 를 사용하고, 데이터양이 많은 경우 무조건 Aggregate 사용한다.
        FindIterable<Document> rs = col.find(new Document()).projection(projection).sort(new BasicDBObject("rank", 1));

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }

            // 조회 잘되나 로그 찍어보기
            int rank = Integer.parseInt(CmmUtil.nvl(String.valueOf(doc.getInteger("rank"))));
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            log.info("rank : " + rank);
            log.info("song : " + song);
            log.info("singer : " + singer);

            MelonDTO rDTO = new MelonDTO();

            rDTO.setRank(rank);
            rDTO.setSong(song);
            rDTO.setSinger(singer);

            // 레코드 결과를 List 에 저장하기
            rList.add(rDTO);
        }

        log.info(this.getClass().getName() + ".getSongList 끝!!");

        return rList;
    }

    @Override
    public List<MelonDTO> getSingerSongCnt(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".getSingerSongCnt 시작!!");

        List<MelonDTO> rlist = new LinkedList<>();

        List<? extends Bson> pipeline = Arrays.asList(new Document().append("$group", new Document().append("_id", new Document().append("singer", "$singer")).append("COUNT(singer)", new Document().append("$sum", 1))), new Document().append("$project", new Document().append("singer", "$_id.singer").append("singerCnt", "$COUNT(singer)").append("_id", 0)), new Document().append("$sort", new Document().append("singerCnt", -1)));

        MongoCollection<Document> col = mongodb.getCollection(colNm);
        AggregateIterable<Document> rs = col.aggregate(pipeline).allowDiskUse(true);

        for (Document doc : rs) {

            if (doc == null) {
                doc = new Document();
            }

            String singer = doc.getString("singer");
            int singerCnt = doc.getInteger("singerCnt", 0);

            log.info("singer : " + singer);
            log.info("singerCnt : " + singerCnt);

            MelonDTO rDTO = new MelonDTO();
            rDTO.setSinger(singer);
            rDTO.setSingerCnt(singerCnt);

            rlist.add(rDTO);

            rDTO = null;
            doc = null;

        }

        Iterator<Document> cursor = null;
        rs = null;
        col = null;
        pipeline = null;

        log.info(this.getClass().getName() + ".getSingerSongCnt 끝!!");

        return rlist;
    }

    @Override
    public List<MelonDTO> getSingerSong(String pColNm, MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getSingerSong 시작!!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        // 조회할 조건 (SQL 의 WHERE 역할 / SELECT song, Singer From Melon_20230323 where singer = "방탄")
        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.getSinger()));

        // 조회 결과 중 출력할 컬럼들(SQL 의 SELECT 와 FROM 절 가운데 컬럼들과 유사함)
        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");

        // MongoDB 는 무조건 ObjectID 가 자동생성되며 ObjectID는 사용하지 않을때, 조회할 필요가 없음
        // objectId 를 가지고 오지 않을 때 사용
        projection.append("_id", 0);

        // MongoDB 의 Find 명령어를 통해 조회할 경우 사용
        // 조회하는 데이터의 양이 적은 경우, find 를 사용하고 , 데이터가 많을 경우 무조건 Aggregate 사용
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }

            // 조회 잘되나 출력
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            log.info("song  : " + song);
            log.info("singer : " + singer);

            MelonDTO rDTO = new MelonDTO();

            rDTO.setSong(song);
            rDTO.setSinger(singer);

            // 레코드 결과를 LIST 에 저장하기
            rList.add(rDTO);

        }

        log.info(this.getClass().getName() + ".getSingerSong 끝!!");

        return rList;
    }

    @Override
    public int dropCollection(String colNm) throws Exception {

        log.info(this.getClass().getName() + ".dropCollection 시작!!");

        int res = 0;

        super.dropCollection(mongodb, colNm);

        res = 1;

        log.info(this.getClass().getName() + ".dropCollection 끝!!");

        return res;
    }

    @Override
    public int insertManyField(String colNm, List<MelonDTO> plist) throws Exception {
        log.info(this.getClass().getName() + ".insertManyField 시작!!");

        int res = 0;

        if (plist == null) {
            plist = new LinkedList<>();
        }

        // 데이터를 저장할 컬렉션 생성
        super.createCollection(mongodb, colNm, "collectTime");

        // 저장할 컬렉션 객체 생성
        MongoCollection<Document> col = mongodb.getCollection(colNm);

        List<Document> list = new ArrayList<>();

        // 람다식 활용 stream 과 -> 사용
        plist.parallelStream().forEach(melon -> list.add(new Document(new ObjectMapper().convertValue(melon, Map.class))));

        // 레코드 리스트 단위로 한번에 저장하기
        col.insertMany(list);

        res = 1;

        log.info(this.getClass().getName() + ".insertManyField 끝!!");

        return res;
    }

    @Override
    public int updateField(String pColNm, MelonDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateField 시작!!");

        int res = 0;

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        String singer = CmmUtil.nvl(pDTO.getSinger());
        String updateSinger = CmmUtil.nvl(pDTO.getUpdateSinger());

        log.info("pColNm : " + pColNm);
        log.info("singer : " + singer);
        log.info("updateSinger : " + updateSinger);

        // 조회할 조건(SQL 의 Where 역할 / SELECT * FROM MELON_20230321 where singer = "방탄소년단")
        Document query = new Document();
        query.append("singer", singer);

        // MongoDB 데이터 수정은 반드시 컬렉션을 조회하고, 조회된 objectID 를 기반으로 데이터를 수정함
        // MongoDB 환경은 분산환경(sharding)으로 구성될 수 있기 때문에 정확한 PK
        FindIterable<Document> rs = col.find(query);

        // 람다식 활용하여 컬렉션에 조회된 데이터들을 수정하기
        rs.forEach(doc -> col.updateOne(doc, new Document("$set", new Document("singer", updateSinger))));

        res = 1;

        log.info(this.getClass().getName() + ".updateField 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getUpdateSinger(String pColNm, MelonDTO pDTO) throws Exception {
        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        // 조회할 조건(sql 의 where 역할 / select song, singer from melon_20230321 where singer = "방탄소년단")
        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.getUpdateSinger()));

        // 조회할 조건(SQL 의 Select 절과 From 절 가운데 컬럼들과 유사함
        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");

        // MongoDB 는 무조건 ObjectId 가 자동생성되면, ID 는 사용하지 않을 때 조회할 필요가 없음
        // id 를 가지고 오지 않을 때 사용함
        projection.append("_id", 0);

        // MongoDB 의 find 명령어를 통해 조회할 경우 사용함
        // 조회하는 데이터의 양이 적은 경우 find 를 사용하고 데이터양이 많은 경우 무조건 aggregate 사용한다.
        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();

            }

            // MongoDB 조회결과를 MElonDTO 에 저장하기 위해 변수에 저장
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));

            log.info("song : " + song);
            log.info("singer : " + singer);

            MelonDTO rDTO = new MelonDTO();
            rDTO.setSong(song);
            rDTO.setSinger(singer);

            // 레코드 결과를 list 에 저장하기
            rList.add(rDTO);
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rList;
    }

    @Override
    public int updateAddField(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        String singer = CmmUtil.nvl(pDTO.getSinger());
        String nickname = CmmUtil.nvl(pDTO.getNickname());

        log.info("pColNm : " + pColNm);
        log.info("singer : " + singer);
        log.info("nickname : " + nickname);

        // 조회할 조건
        Document query = new Document();
        query.append("singer", singer);

        FindIterable<Document> rs = col.find(query);

        // 람다식 활용하여 컬렉션에 조회된 데이터들을 수정하기
        // Mongodb driver 는 mongodb 의 set 함수를 대신할 자바 함수를 구현함
        rs.forEach(doc -> col.updateOne(doc, set("nickname", nickname)));

        res = 1;


        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getSingerSongNickName(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 조회결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.getSinger()));

        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");
        projection.append("nickname", "$nickname");

        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs) {
            if (doc == null) {
                doc = new Document();
            }

            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));
            String nickname = CmmUtil.nvl(doc.getString("nickname"));

            log.info("song : " + song);
            log.info("singer : " + singer);
            log.info("nickname : " + nickname);

            MelonDTO rDTO = new MelonDTO();
            rDTO.setSong(song);
            rDTO.setSinger(singer);
            rDTO.setNickname(nickname);

            rList.add(rDTO);
        }

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return rList;
    }

    @Override
    public int updateAddListField(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        String singer = CmmUtil.nvl(pDTO.getSinger());
        List<String> member = pDTO.getMember();

        log.info("pcolNm : " + pColNm);
        log.info("singer : " + singer);
        log.info("member : " + member);

        // 조회할 조건 (SQl 의 WHERE 역할 / select * From MELON_20230403 where singer = '방탄소년단')
        Document query = new Document();
        query.append("singer", singer);

        // MongoDB 데이터 삭제는 반드시 컬렉션을 조회하고, 조회된 Object ID 를 기반으로 데이터를 삭제함
        // MongoDb 환경은 분산환경으로 구성될 수 있기 때문에 정확한 PK 에 매핑하기 위해서임
        FindIterable<Document> rs = col.find(query);

        // 람다식 활용하여 컬렉션에 조회된 데이터들을 수정하기
        // list 구조는 String 구조와 동일하게 set 에 List 객체를 저장하면 된다.
        // MongoDB 의 저장단위는 Document 객체는 자바의 map 을 상속받아 구현한 것이며, Map 특징인 값은 모든 객체가 저장 가능하다.
        rs.forEach(doc -> col.updateOne(doc, set("member", member)));

        res = 1;

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getSingerSongMember(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        // 조회할 조건
        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.getSinger()));

        // 조회 결과 중 출력할 컬럼들
        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");
        projection.append("member", "$member");

        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs ) {
            if (doc == null) {
                doc = new Document();
            }

            // MongoDB 조회 결과를 MelonDTO 에 저장하기 위해 변수에 저장
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));
            List<String> member = doc.getList("member", String.class, new ArrayList<>());

            log.info("song : " + song);
            log.info("singer : " + singer);
            log.info("member : " + member);

            MelonDTO rDTO = new MelonDTO();

            rDTO.setSong(song);
            rDTO.setSinger(singer);
            rDTO.setMember(member);

            rList.add(rDTO);
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rList;
    }

    @Override
    public int updateFieldAndAddField(String pColNm, MelonDTO pDTO) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        String singer = CmmUtil.nvl(pDTO.getSinger());
        String updateSinger = CmmUtil.nvl(pDTO.getUpdateSinger());
        String addFieldValue = CmmUtil.nvl(pDTO.getAddFieldValue());

        log.info("pcolNm : " + pColNm);
        log.info("singer : " + singer);
        log.info("updateSinger : " + updateSinger);
        log.info("addFieldValue : " + addFieldValue);

        // 조회할 조건 (SQl 의 WHERE 역할 / select * From MELON_20230403 where singer = '방탄소년단')
        Document query = new Document();
        query.append("singer", singer);

        // MongoDB 데이터 삭제는 반드시 컬렉션을 조회하고, 조회된 Object ID 를 기반으로 데이터를 삭제함
        // MongoDb 환경은 분산환경으로 구성될 수 있기 때문에 정확한 PK 에 매핑하기 위해서임
        FindIterable<Document> rs = col.find(query);

        // 한줄로 append 해서 수정할 필드 추가해도 되지만, 가독성이 떨어져 줄마다 append 함
        Document updateDoc = new Document();
        updateDoc.append("singer", updateSinger); // 기존 필드 수정
        updateDoc.append("addData", addFieldValue); // 신규 필드 추가
        
        // 람다식 활용하여 컬렉션에 조회된 데이터들을 수정하기
        // list 구조는 String 구조와 동일하게 set 에 List 객체를 저장하면 된다.
        // MongoDB 의 저장단위는 Document 객체는 자바의 map 을 상속받아 구현한 것이며, Map 특징인 값은 모든 객체가 저장 가능하다.
        rs.forEach(doc -> col.updateOne(doc, new Document("$set", updateDoc)));

        res = 1;

        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return res;
    }

    @Override
    public List<MelonDTO> getSingerSongAddData(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        // 조회 결과를 전달하기 위한 객체 생성하기
        List<MelonDTO> rList = new LinkedList<>();

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        // 조회할 조건
        Document query = new Document();
        query.append("singer", CmmUtil.nvl(pDTO.getUpdateSinger()));

        // 조회 결과 중 출력할 컬럼들
        Document projection = new Document();
        projection.append("song", "$song");
        projection.append("singer", "$singer");
        projection.append("addData", "$addData");

        projection.append("_id", 0);

        FindIterable<Document> rs = col.find(query).projection(projection);

        for (Document doc : rs ) {
            if (doc == null) {
                doc = new Document();
            }

            // MongoDB 조회 결과를 MelonDTO 에 저장하기 위해 변수에 저장
            String song = CmmUtil.nvl(doc.getString("song"));
            String singer = CmmUtil.nvl(doc.getString("singer"));
            String addData = CmmUtil.nvl(doc.getString("addData"));

            log.info("song : " + song);
            log.info("singer : " + singer);
            log.info("member : " + addData);

            MelonDTO rDTO = new MelonDTO();

            rDTO.setSong(song);
            rDTO.setSinger(singer);
            rDTO.setAddFieldValue(addData);

            rList.add(rDTO);
        }

        log.info(this.getClass().getName() + "." + name +" 끝!!");

        return rList;
    }

    @Override
    public int deleteDocument(String pColNm, MelonDTO pDTO) throws Exception {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + name + " 시작!!");

        int res = 0;

        MongoCollection<Document> col = mongodb.getCollection(pColNm);

        String singer = CmmUtil.nvl(pDTO.getSinger());

        log.info("pColNm : " + pColNm);
        log.info("singer : " + singer);

        // 조회할 조건
        Document query = new Document();
        query.append("singer", singer);

        FindIterable<Document> rs = col.find(query);

        // 람다식 활용하여 컬렉션에 조회된 데이터들을 수정하기
        // Mongodb driver 는 mongodb 의 set 함수를 대신할 자바 함수를 구현함
        rs.forEach(doc -> col.deleteOne(doc));

        res = 1;


        log.info(this.getClass().getName() + "." + name + " 끝!!");

        return res;
    }


}
