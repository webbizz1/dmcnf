package webbizz.crm.service.exhibition;

import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.exhibition.ExhibitionRepository;
import webbizz.crm.domain.exhibition.dto.ExhibitionCondition;
import webbizz.crm.domain.exhibition.dto.ExhibitionDto;
import webbizz.crm.domain.exhibition.dto.ExhibitionSaveDto;
import webbizz.crm.domain.exhibition.entity.Exhibition;
import webbizz.crm.domain.exhibition.enumset.BannerType;
import webbizz.crm.domain.exhibition.enumset.ExhibitionType;
import webbizz.crm.domain.exhibition.enumset.LinkTarget;
import webbizz.crm.exception.ApiBadRequestException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.exception.BusinessException;
import webbizz.crm.service.attachment.AttachmentService;

import java.time.LocalTime;
import java.util.List;

@Service("exhibitionService")
@RequiredArgsConstructor
public class ExhibitionServiceImpl extends EgovAbstractServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    private final AttachmentService attachmentService;

    @Override
    @Transactional(readOnly = true)
    public ExhibitionDto searchBy(final Long id, final ExhibitionType type) {
        return exhibitionRepository.searchBy(id, type)
                .orElseThrow(() -> new BusinessException(404, "전시 정보를 찾을 수 없습니다."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExhibitionDto> searchAllByCondition(final ExhibitionCondition condition) {
        return exhibitionRepository.searchAllByCondition(condition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExhibitionDto> searchAllForActive() {
        return exhibitionRepository.searchAllForActive();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countExhibitionForActive(ExhibitionType type) {
        return exhibitionRepository.countExhibitionForActive(type);
    }

    @Override
    @Transactional
    public Long saveExhibition(final ExhibitionSaveDto requestDto, final HttpMethod httpMethod) {
        Exhibition exhibition;

        if (httpMethod == HttpMethod.POST) {
            exhibition = Exhibition.builder()
                    .type(requestDto.getType())
                    .build();
        }
        else if (httpMethod == HttpMethod.PUT && requestDto.getId() != null) {
            exhibition = exhibitionRepository.findBy(requestDto.getId(), requestDto.getType())
                    .orElseThrow(() -> new ApiNotFoundException("전시 정보를 찾을 수 없습니다."));
        }
        else throw new ApiBadRequestException("전시 정보가 없습니다.");

        // 상단 띠 배너의 경우에만 배너 유형을 선택할 수 있고 그 외에는 배너 유형을 이미지로 처리
        if (requestDto.getType() != ExhibitionType.TOP) {
            exhibition.setBannerType(BannerType.IMAGE);
        } else {
            exhibition.setBannerType(requestDto.getBannerType());
        }

        // 링크 타겟이 '링크 없음' 일 경우 링크 URL 을 null 처리
        if (requestDto.getLinkTarget() == LinkTarget.NONE) {
            exhibition.setLinkUrl(null);
        } else {
            exhibition.setLinkUrl(requestDto.getLinkUrl());
        }

        // 배너 유형이 '이미지' 일 경우 이미지를 저장
        if (requestDto.getBannerType() == BannerType.IMAGE) {
            exhibition.setPcImage(attachmentService.findAttachmentDtoByUUID(requestDto.getPcImageUUID()));
            exhibition.setMobileImage(attachmentService.findAttachmentDtoByUUID(requestDto.getMobileImageUUID()));

            if (exhibition.getPcImage() == null) {
                throw new ApiNotFoundException("PC 이미지를 다시 선택해 주세요.");
            }
            if (exhibition.getMobileImage() == null) {
                throw new ApiNotFoundException("Mobile 이미지를 다시 선택해 주세요.");
            }
        } else {
            exhibition.setPcImage(null);
            exhibition.setMobileImage(null);
        }

        // 분기 처리 없는 일반 필드
        exhibition.setTitle(requestDto.getTitle());
        exhibition.setLinkTarget(requestDto.getLinkTarget());
        exhibition.setViewStartAt(requestDto.getViewStartDate().atStartOfDay());
        exhibition.setViewEndAt(requestDto.getViewEndDate() != null
                ? requestDto.getViewEndDate().atTime(LocalTime.MAX)
                : null);
        exhibition.setViewYn(requestDto.getViewYn() != null ? requestDto.getViewYn() : YN.Y);

        exhibitionRepository.save(exhibition);

        return exhibition.getId();
    }

    @Override
    @Transactional
    public void deleteExhibition(final Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id)
                .orElseThrow(() -> new ApiNotFoundException(404, "전시 정보를 찾을 수 없습니다."));

        exhibition.setDelYn(YN.Y);
    }

}
