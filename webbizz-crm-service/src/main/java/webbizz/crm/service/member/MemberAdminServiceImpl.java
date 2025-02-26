package webbizz.crm.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.authoritypattern.AuthorityPatternRepository;
import webbizz.crm.domain.authoritypattern.entity.AuthorityPattern;
import webbizz.crm.domain.member.dto.MemberAdminDto;
import webbizz.crm.domain.member.dto.MemberAdminSaveDto;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberAuditorType;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.domain.memberauthority.MemberAuthorityRepository;
import webbizz.crm.domain.memberauthority.entity.MemberAuthority;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.service.member.dto.MemberAuditorEvent;
import webbizz.crm.util.PatternUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service("memberAdminService")
@RequiredArgsConstructor
public class MemberAdminServiceImpl extends AbstractMemberServiceImpl implements MemberAdminService {

    private final MemberAuthorityRepository memberAuthorityRepository;

    private final AuthorityPatternRepository authorityPatternRepository;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final SessionRegistry sessionRegistry;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Member member = memberRepository.findBy(username, MemberRole.ADMIN)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 일치하지 않습니다."));

        return new MemberUserDetails(member);
    }

    @Override
    public void updateMemberLastLoginAt(Authentication authentication) {
        super.updateMemberLastLoginAt(authentication);

        // 로그인 이벤트 발행
        applicationEventPublisher.publishEvent(new MemberAuditorEvent(null, MemberAuditorType.LOGIN));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberAdminDto searchMemberAdminById(final Long id) {
        MemberAdminDto memberAdminDto = memberRepository.searchMemberAdminById(id)
                .orElseThrow(() -> new BusinessException(404, "회원 정보를 찾을 수 없습니다."));

        // 권한 시퀀스 조회 후 및 DTO 에 설정
        memberAdminDto.getAuthorityPatternIds().addAll(memberAuthorityRepository.searchPatternIdsByMemberId(id));

        return memberAdminDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberAdminDto> searchAllMemberAdminByCondition(final MemberCondition condition) {
        condition.setRole(MemberRole.ADMIN);
        return memberRepository.searchAllMemberAdminByCondition(condition);
    }

    @Override
    @Transactional
    public Long saveMemberAdmin(final MemberAdminSaveDto requestDto, final HttpMethod httpMethod) {
        Member member;
        String encryptedPassword;

        // 회원 시퀀스로 생성·수정 구분
        if (httpMethod == HttpMethod.POST) {
            if (memberRepository.countByLoginId(requestDto.getLoginId(), MemberRole.ADMIN) > 0) {
                throw new ApiBadRequestException(String.format("이미 사용 중인 아이디입니다: '%s'", (requestDto.getLoginId())));
            }
            if (!StringUtils.hasText(requestDto.getPassword())) {
                throw new ApiBadRequestException("비밀번호를 입력해주세요.");
            }
            if (!ObjectUtils.nullSafeEquals(requestDto.getPassword(), requestDto.getPasswordConfirm())) {
                throw new ApiBadRequestException("비밀번호 확인이 일치하지 않습니다.");
            }

            member = Member.builder()
                    .loginId(requestDto.getLoginId())
                    .role(MemberRole.ADMIN)
                    .status(MemberStatus.NORMAL)
                    .build();

            encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        }
        else if (httpMethod == HttpMethod.PUT && requestDto.getId() != null) {
            if (memberRepository.countByLoginId(requestDto.getLoginId(), MemberRole.ADMIN) > 1) {
                throw new ApiBadRequestException(String.format("이미 사용 중인 아이디입니다: '%s'", (requestDto.getLoginId())));
            }

            member = memberRepository.findBy(requestDto.getId(), MemberRole.ADMIN)
                    .orElseThrow(() -> new ApiNotFoundException("회원 정보를 찾을 수 없습니다."));

            // 회원 정보 수정 시 비밀번호 변경 여부 확인
            if (StringUtils.hasText(requestDto.getPassword())) {
                if (!ObjectUtils.nullSafeEquals(requestDto.getPassword(), requestDto.getPasswordConfirm())) {
                    throw new ApiBadRequestException("비밀번호 확인이 일치하지 않습니다.");
                }

                encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
            } else {
                encryptedPassword = member.getPassword();
            }
        }
        else throw new ApiBadRequestException("회원 정보가 없습니다.");

        if (!requestDto.getAllowIpAddresses().isEmpty()
            && requestDto.getAllowIpAddresses().stream().noneMatch(
                ip -> ip.matches(PatternUtils.REGEX_IP_ADDRESS))) {
            throw new ApiBadRequestException("허용 IP 주소 형식이 올바르지 않습니다.");
        }

        member.setPassword(encryptedPassword);
        member.setRealName(requestDto.getRealName());
        member.setEmail(requestDto.getEmail());
        member.setTelephoneNumber(requestDto.getTelephoneNumber());
        member.setMobileNumber(requestDto.getMobileNumber());
        member.setAllowIpAddresses(requestDto.getAllowIpAddresses());
        member.setStatus(requestDto.getStatus());

        // 권한 설정 정보 삭제 (hard delete)
        memberAuthorityRepository.deleteByMemberId(member.getId());

        // 권한 패턴 조회
        List<AuthorityPattern> authorityPatterns = authorityPatternRepository.findAllByRole(MemberRole.ADMIN);

        // 권한 설정 정보 생성
        List<MemberAuthority> authorities = authorityPatterns.stream()
                .filter(authorityPattern -> requestDto.getAuthorityPatternIds().contains(authorityPattern.getId()))
                .map(authorityPattern -> MemberAuthority.builder()
                        .authorityPattern(authorityPattern)
                        .build())
                .collect(Collectors.toList());
        member.setAuthorities(authorities);

        if (authorities.isEmpty()) {
            throw new ApiBadRequestException("메뉴 권한을 찾을 수 없습니다.");
        }

        memberRepository.save(member);

        // 회원 정보 수정 이벤트 발행
        applicationEventPublisher.publishEvent(new MemberAuditorEvent(member, MemberAuditorType.MODIFICATION));

        // 메뉴 권한이 재설정 되었으므로 로그인 세션이 있으면 모두 만료 처리
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        allPrincipals.stream()
                .filter(principal -> {
                    if (!(principal instanceof UserDetails))
                        return false;

                    return ObjectUtils.nullSafeEquals(((UserDetails) principal).getUsername(), member.getLoginId());
                })
                .forEach(principal -> {
                    List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, false);
                    if (sessionInformations != null)
                        sessionInformations.forEach(SessionInformation::expireNow);
                });

        return member.getId();
    }

}
