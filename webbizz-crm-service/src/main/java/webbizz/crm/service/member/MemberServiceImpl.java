package webbizz.crm.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.member.dto.MemberCondition;
import webbizz.crm.domain.member.dto.MemberDto;
import webbizz.crm.domain.member.dto.MemberPasswordResetDto;
import webbizz.crm.domain.member.entity.Member;
import webbizz.crm.domain.member.entity.MemberUserDetails;
import webbizz.crm.domain.member.enumset.MemberRole;
import webbizz.crm.domain.member.enumset.MemberStatus;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.service.excel.ExcelFile;
import webbizz.crm.service.member.dto.MemberActiveExcelDto;
import webbizz.crm.service.member.dto.MemberWithdrawalExcelDto;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("memberService")
@RequiredArgsConstructor
public class MemberServiceImpl extends AbstractMemberServiceImpl implements MemberService {

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Member member = memberRepository.findBy(username, MemberRole.USER)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 정보가 일치하지 않습니다."));

        return new MemberUserDetails(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto searchMemberById(final Long id) {
        return memberRepository.searchMemberById(id)
                .orElseThrow(() -> new BusinessException(404, "회원 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto searchWithdrawalMemberById(final Long id) {
        MemberDto memberDto = memberRepository.searchMemberById(id)
                .orElseThrow(() -> new BusinessException(404, "탈퇴 회원 정보를 찾을 수 없습니다."));

        if (memberDto.getStatus() != MemberStatus.WITHDRAWAL) {
            throw new BusinessException(404, "이미 탈퇴한 회원입니다.");
        }

        return memberDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberDto> searchAllMemberByCondition(final MemberCondition condition) {
        condition.setRole(MemberRole.USER);
        condition.setDelYn(YN.N);
        return memberRepository.searchAllMemberByCondition(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberDto> searchAllWithdrawalMemberByCondition(final MemberCondition condition) {
        condition.setRole(MemberRole.USER);
        condition.setStatus(MemberStatus.WITHDRAWAL);
        condition.setDelYn(YN.Y);
        return memberRepository.searchAllMemberByCondition(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countMemberForActive() {
        return memberRepository.countMemberForActive();
    }

    @Override
    @Transactional
    public String resetPassword(final MemberPasswordResetDto requestDto) {
        Member member = memberRepository.findBy(requestDto.getId(), MemberRole.USER)
                .orElseThrow(() -> new ApiNotFoundException(404, "회원 정보를 찾을 수 없습니다."));

        if (member.getStatus() == MemberStatus.WITHDRAWAL) {
            throw new ApiBadRequestException(400, "탈퇴 회원은 비밀번호 초기화를 할 수 없습니다.");
        }

        String newPassword = String.format("%06d", new Random().nextInt(1000000));
        member.setPassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelFile<?> searchMemberForExcel(final MemberCondition condition) {
        condition.setPage(1);
        condition.setSize(Integer.MAX_VALUE);
        Page<MemberDto> page = this.searchAllMemberByCondition(condition);
        List<MemberDto> list = page.getContent();

        List<MemberActiveExcelDto> excelDtos = list.stream()
                .map(item -> new MemberActiveExcelDto(
                        page.getTotalElements() - (long) page.getNumber() * page.getSize() - list.indexOf(item),
                        item.getLoginId(),
                        item.getRealName(),
                        item.getEmail(),
                        item.getMobileNumber(),
                        item.getCreatedAt(),
                        item.getLastLoginAt()
                ))
                .collect(Collectors.toList());

        return new ExcelFile<>(excelDtos, "활동 회원 목록", MemberActiveExcelDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ExcelFile<?> searchWithdrawalMemberForExcel(final MemberCondition condition) {
        condition.setPage(1);
        condition.setSize(Integer.MAX_VALUE);
        Page<MemberDto> page = this.searchAllWithdrawalMemberByCondition(condition);
        List<MemberDto> list = page.getContent();

        List<MemberWithdrawalExcelDto> excelDtos = list.stream()
                .map(item -> new MemberWithdrawalExcelDto(
                        page.getTotalElements() - (long) page.getNumber() * page.getSize() - list.indexOf(item),
                        item.getLoginId(),
                        item.getRealName(),
                        item.getEmail(),
                        item.getMobileNumber(),
                        item.getCreatedAt(),
                        item.getWithdrawalAt(),
                        item.getWithdrawalReason()
                ))
                .collect(Collectors.toList());

        return new ExcelFile<>(excelDtos, "탈퇴 회원 목록", MemberWithdrawalExcelDto.class);
    }

}
