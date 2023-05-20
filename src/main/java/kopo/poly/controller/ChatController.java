package kopo.poly.controller;

import kopo.poly.dto.ChatDTO;
import kopo.poly.service.IChatService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController(value = "/chat")
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final IChatService chatService;

    /**
     * 채팅방 초기화면
     */
    @GetMapping(value = "index")
    public String index() {

        String functionName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");


        log.info(this.getClass().getName() + "." + functionName + " 끝!!");

        return "chat/index";
    }

    /**
     * 채팅방 입장
     */
    @PostMapping(value = "intro")
    public String intro(HttpServletRequest request, HttpSession session) throws Exception {

        String functionName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // 기존 세션에 저장된 값 삭제하기
        session.setAttribute("SS_ROOM_NAME", "");
        session.setAttribute("SS_USER_NAME", "");

        String room_name = CmmUtil.nvl(request.getParameter("room_name"));
        String userNm = CmmUtil.nvl(request.getParameter("user_name"));

        log.info("room_name : " + room_name);
        log.info("userNm : " + userNm);

        // 세션에 값 저장하기
        session.setAttribute("SS_ROOM_NAME", room_name);
        session.setAttribute("SS_USER_NAME", userNm);

        // 입장 환영 멘트 저장하기
        ChatDTO pDTO = new ChatDTO();

        pDTO.setRoomKey("Chat_" + room_name);
        pDTO.setUserNm("관리자");
        pDTO.setMsg(userNm + "님! [" + room_name + "] 채팅장 입장을 환영합니다.");
        pDTO.setDateTime(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss"));

        // 채팅 멘트 저장하기
        chatService.insertChat(pDTO);

        log.info(this.getClass().getName() + "." + functionName + " 끝!!");

        return "chat/intro";
    }

    /**
     * 채팅방 전체 리스트 가져오기
     * */
    @PostMapping(value = "roomList")
    @ResponseBody
    public Set<String> roomList() throws Exception {

        String functionName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(this.getClass().getName() + "." + functionName + " 시작!!");

        // Java 8부터 제공하는 NPE 처리
        Set<String> rSet = Optional.ofNullable(chatService.getRoomList()).orElseGet(HashSet::new);

        log.info(this.getClass().getName() + "." + functionName +" 끝!!");

        return rSet;
    }
}

