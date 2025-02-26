package webbizz.crm.service.terms;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.terms.TermsRepository;
import webbizz.crm.domain.terms.dto.TermsCondition;
import webbizz.crm.domain.terms.dto.TermsDto;
import webbizz.crm.domain.terms.dto.TermsSaveDto;
import webbizz.crm.domain.terms.entity.Terms;
import webbizz.crm.domain.terms.enumset.TermsType;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.util.XssFilterUtils;

import java.util.List;

@Service("termsService")
@RequiredArgsConstructor
public class TermsServiceImpl extends EgovAbstractServiceImpl implements TermsService {

    private final TermsRepository termsRepository;

    @Override
    @Transactional(readOnly = true)
    public TermsDto searchBy(final Long id, final TermsType type) {
        return termsRepository.searchBy(id, type)
                .orElseThrow(() -> new BusinessException(404, "약관 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public TermsDto searchByForActive(final TermsType type) {
        return termsRepository.searchByForActive(type)
                .orElseThrow(() -> new BusinessException(404, "활성화된 약관이 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TermsDto> searchAllByCondition(final TermsCondition condition) {
        return termsRepository.searchAllByCondition(condition);
    }

    @Override
    @Transactional
    public Long saveTerms(final TermsSaveDto requestDto, final HttpMethod httpMethod) {
        Terms terms;

        if (httpMethod == HttpMethod.POST) {
            // 기존 약관 비활성화
            termsRepository.findAllByType(requestDto.getType()).forEach(subTerms -> subTerms.setActiveYn(YN.N));

            terms = Terms.builder()
                    .type(requestDto.getType())
                    .activeYn(YN.Y) // 신규 약관은 등록 시 활성화
                    .build();
        }
        else if (httpMethod == HttpMethod.PUT) {
            terms = termsRepository.findById(requestDto.getId())
                    .orElseThrow(() -> new ApiNotFoundException(404, "약관 정보를 찾을 수 없습니다."));
        }
        else throw new ApiBadRequestException("약관 정보가 없습니다.");

        terms.setContent(XssFilterUtils.filter(requestDto.getContent()));

        termsRepository.save(terms);

        return terms.getId();
    }

    @Override
    @Transactional
    public void activeTerms(final Long id) {
        Terms terms = termsRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(404, "약관 정보를 찾을 수 없습니다."));

        // 기존 약관 비활성화
        termsRepository.findAllByType(terms.getType()).forEach(subTerms -> subTerms.setActiveYn(YN.N));

        // 요청한 약관 활성화
        terms.setActiveYn(YN.Y);
    }

}
