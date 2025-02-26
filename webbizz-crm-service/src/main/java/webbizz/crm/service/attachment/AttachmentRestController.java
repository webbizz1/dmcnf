package webbizz.crm.service.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.attachment.entity.Attachment;
import webbizz.crm.exception.ApiInternalServerErrorException;
import webbizz.crm.exception.ApiNotFoundException;
import webbizz.crm.response.ApiResponse;

import java.util.List;
import java.util.stream.Stream;

/**
 * 파일 업로드/다운로드 REST API
 *
 * @apiNote 2.0 버전에서는 Service 클래스만 주입받아서 사용하도록 변경
 * @since 2022-08-05
 * @author TAEROK HWANG
 * @version 2.0
 */
@RestController
@RequiredArgsConstructor
public class AttachmentRestController {

    private final AttachmentService attachmentService;

    /**
     * 파일 업로드 <br />
     * 모든 파일 업로드가 성공했을 경우 200 (OK) message="OK" <br />
     * 일부 파일 업로드가 실패했을 경우 200 (OK) message="PARTIAL SUCCESS" <br />
     * 파일 업로드에 실패했을 경우 500 (Internal Server Error) <br />
     * 파일 업로드 성공 여부는 요청 List size 와 반환 List size 를 비교하여 확인한다.
     *
     * @param files  multipart/form-data 로 전송된 파일 스트림
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 view=Y 로 설정된다.
     * @return ApiResponse 업로드 결과 정보
     * @author TAEROK HWANG
     */
    @PostMapping("/api/v1/attachment")
    public ApiResponse<Stream<AttachmentDto>> attachmentPostV1(@RequestParam("files") List<MultipartFile> files,
                                                               @RequestParam(required = false) String source) {

        try {
            if (files.isEmpty()) throw new ApiNotFoundException("NO FILES");

            List<Attachment> uploadFileLists = attachmentService.uploadFiles(files, source);
            String message = files.size() == uploadFileLists.size() ? "SUCCESS" : "PARTIAL SUCCESS";

            // DTO 로 변환하여 반환
            return ApiResponse.ok(message, uploadFileLists.stream().map(Attachment::toDto));
        } catch (Exception e) {
            throw new ApiInternalServerErrorException(e.getMessage());
        }
    }

    /**
     * 파일 미리보기 (이미지) <br />
     * MIME-Type 이 image/* 또는 application/pdf 인 경우에만 미리보기 가능 (브라우저에서 지원하는 경우)
     *
     * @param uuid 미리보기 할 파일의 uuid
     * @param size 이미지일 때 출력하기 위한 썸네일의 너비 <br />
     * @return DB에 파일 정보가 없거나 viewYn=Y 아니면 404 (Not Found) <br />
     * DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone) <br />
     * 이미지 또는 PDF 형식이 아니면 미리보기 불가 415 (Unsupported Media Type) <br />
     * 성공하면 200 (OK) 와 함께 파일 스트림 출력
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/attachment/{uuid}")
    public ResponseEntity<byte[]> attachmentView(@PathVariable("uuid") String uuid,
                                                 @RequestParam(required = false) Integer size) {

        return attachmentService.readAttachment(uuid, false, size);
    }

    /**
     * 파일 다운로드
     *
     * @param uuid 다운로드 할 파일의 uuid
     * @return DB에 파일 정보가 없거나 viewYn=Y 아니면 404 (Not Found)<br />
     * DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)<br />
     * 성공하면 200 (OK) 와 함께 파일 스트림 출력 (Content-Disposition: attachment; filename="파일명")
     * @author TAEROK HWANG
     */
    @GetMapping("/api/v1/attachment/{uuid}/download")
    public ResponseEntity<byte[]> attachmentDownload(@PathVariable("uuid") String uuid) {
        return attachmentService.readAttachment(uuid, true, null);
    }

    /**
     * 파일 삭제
     *
     * @param uuid 삭제할 파일의 uuid
     * @return ApiResponse 삭제 결과 정보 (true / false)
     * @author TAEROK HWANG
     */
    @DeleteMapping("/api/v1/attachment/{uuid}")
    public ApiResponse<?> attachmentDelete(@PathVariable("uuid") String uuid) {
        return ApiResponse.ok(attachmentService.deleteByUUID(uuid));
    }

}
