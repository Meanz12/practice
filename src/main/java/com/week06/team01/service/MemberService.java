package com.week06.team01.service;

import com.week06.team01.controller.request.LoginRequestDto;
import com.week06.team01.controller.request.MemberRequestDto;
import com.week06.team01.controller.request.MemberSignDto;
import com.week06.team01.controller.request.TokenDto;
import com.week06.team01.controller.response.MemberResponseDto;
import com.week06.team01.controller.response.Message;
import com.week06.team01.controller.response.ResponseDto;
import com.week06.team01.controller.response.StatusEnum;
import com.week06.team01.domain.Member;
import com.week06.team01.jwt.TokenProvider;
import com.week06.team01.repository.MemberRepository;
import com.week06.team01.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.security.Key;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
  public static String AUTHORIZATION_HEADER = "Authorization";
  public static String BEARER_PREFIX = "Bearer ";
  private final TokenProvider tokenProvider;
  private final MemberRepository memberRepository;
  private final PostService postService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public ResponseEntity<Message> createMember(MemberRequestDto requestDto) {
    HttpHeaders headers = new HttpHeaders();

    if (!requestDto.getPassword().equals(requestDto.getPwdCheck())) {
      Message message = new Message();
      message.setStatus(StatusEnum.PASSWORDS_NOT_MATCHED);
      message.setMessage("??????????????? ???????????? ????????? ???????????? ????????????.");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }
    Member member = Member.builder()
            .username(requestDto.getUsername())
            .nickname(requestDto.getNickname())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .build();
    memberRepository.save(member);
    Message message = new Message(member);
    return new ResponseEntity<>(message,headers,HttpStatus.OK);
  }

  @Transactional
  public ResponseEntity<Message> login(LoginRequestDto requestDto, HttpServletResponse response) {
    Member member = isPresentMember(requestDto.getUsername());
    HttpHeaders headers = new HttpHeaders();

    if (null == member) {
      Message message = new Message();
      message.setStatus(StatusEnum.MEMBER_NOT_FOUND);
      message.setMessage("???????????? ?????? ??? ????????????.");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }
    if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
      Message message = new Message();
      message.setStatus(StatusEnum.INVALID_MEMBER);
      message.setMessage("???????????? ?????? ??? ????????????.");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }
    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
    tokenToHeaders(tokenDto, response);
    Message message = new Message(MemberResponseDto.builder()
            .id(member.getId())
            .username(member.getUsername())
            .nickname(member.getNickname())
            .build()
    );
    return new ResponseEntity<>(message,headers,HttpStatus.OK);
  }


  public ResponseEntity<Message> logout(HttpServletRequest request) {
    Member member = tokenProvider.getMemberFromAuthentication();
    HttpHeaders headers = new HttpHeaders();
    if (!tokenProvider.validateToken(request.getHeader("Authorization").substring(7))) {
      tokenProvider.deleteRefreshToken(member);
      Message message = new Message();
      message.setStatus(StatusEnum.INVALID_TOKEN);
      message.setMessage("Refresh-Token??? ????????????");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }
    if (null == member) {
      Message message = new Message();
      message.setStatus(StatusEnum.MEMBER_NOT_FOUND);
      message.setMessage("????????? ??????????????? ????????????");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }

    Message message = new Message(MemberResponseDto.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .username(member.getUsername()).build());
    message.setMessage("Success");
    message.setStatus(StatusEnum.OK);
    tokenProvider.deleteRefreshToken(member);
    return new ResponseEntity<>(message,headers,HttpStatus.OK);
  }

  @Transactional(readOnly = true)
  public Member isPresentMember(String username) {
    Optional<Member> optionalMember = memberRepository.findByUsername(username);
    return optionalMember.orElse(null);
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

  public ResponseEntity<Message> info(HttpServletRequest request) {
    Member member = postService.validateMember(request);
    Message message = new Message(MemberResponseDto.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .username(member.getUsername()).build());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
    return new ResponseEntity<>(message,headers,HttpStatus.OK);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Transactional
  public ResponseEntity<Message> idCheck(MemberSignDto memberSignDto) {
    Optional<Member> member = memberRepository.findByUsername(memberSignDto.getUsername());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

    if(member.isPresent()){
      Message message = new Message();
      message.setStatus(StatusEnum.ID_DUPLICATION);
      message.setMessage("????????? ??????????????????.");
      return new ResponseEntity<>(message,headers,HttpStatus.BAD_REQUEST);
    }
    else{
      Message message = new Message(memberSignDto);
      message.setStatus(StatusEnum.OK);
      message.setMessage("Success");
      return new ResponseEntity<>(message,headers,HttpStatus.OK);
    }
  }
}
