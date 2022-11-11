package com.week06.team01.service;

import com.week06.team01.domain.Member;
import com.week06.team01.domain.UserDetailsImpl;
import com.week06.team01.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByUsername(username).orElseThrow(
            () -> new RuntimeException("NOT_FOUND_MEMBER")
    );
    UserDetailsImpl userDetails = new UserDetailsImpl();
    userDetails.setMember(member);
    return userDetails;
  }
}
