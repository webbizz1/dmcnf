package webbizz.crm.service.member;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.authoritypattern.AuthorityPatternRepository;
import webbizz.crm.domain.authoritypattern.dto.AuthorityPatternDto;
import webbizz.crm.domain.member.enumset.MemberRole;

import java.util.List;

@Service("authorityService")
@RequiredArgsConstructor
public class AuthorityServiceImpl extends EgovAbstractServiceImpl implements AuthorityService {

    private final AuthorityPatternRepository authorityPatternRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorityPatternDto> searchAllByRole(final MemberRole role) {
        return authorityPatternRepository.searchAllByRole(role);
    }

}
