package webbizz.crm.service.attachment;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.attachment.entity.Attachment;

import java.io.IOException;
import java.util.List;

/**
 * 파일 업로드/다운로드 서비스 인터페이스
 */
public interface AttachmentService {

    /**
     * 단일 파일 업로드 및 DB에 저장
     *
     * @param file multipart/form-data 로 전송된 파일 스트림
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 viewYn=Y 로 설정된다.
     * @return 업로드 파일 Entity
     * @throws IOException 파일 업로드 실패 시
     * @author TAEROK HWANG
     */
    Attachment uploadFile(MultipartFile file, String source) throws IOException;

    /**
     * 다중 파일 업로드 및 DB에 저장<br />
     * 업로드 실패한 파일은 예외 처리되고, 업로드 파일 Entity 리스트에 포함되지 않는다.<br />
     * 요청 List size 와 반환 List size 를 비교하여 업로드 실패한 파일이 있는지 확인할 수 있다.
     *
     * @param files multipart/form-data 로 전송된 파일 스트림 리스트
     * @param source 업로드 파일의 출처, "editor" 를 넣으면 에디터에서 사용하는 파일로 인식하여 viewYn=Y 로 설정된다.
     * @return 업로드 파일 Entity 리스트
     * @throws IOException 파일 업로드 실패 시
     * @see #uploadFile(MultipartFile, String)
     * @author TAEROK HWANG
     */
    List<Attachment> uploadFiles(List<MultipartFile> files, String source) throws IOException;

    /**
     * UUID 문자열로 DB 에서 업로드 파일 정보를 찾기 (viewYn: N)
     *
     * @param uuid 업로드 파일 UUID
     * @return Attachment Entity
     * @author TAEROK HWANG
     */
    @Transactional(readOnly = true)
    Attachment findByUUID(String uuid);

    /**
     * UUID 문자열로 DB 에서 업로드 파일 정보를 찾기
     *
     * @param uuid 업로드 파일 UUID
     * @param viewYn viewYn=Y 이면 API 에서 파일을 호출 가능
     * @return Attachment Entity
     * @author TAEROK HWANG
     */
    @Transactional
    Attachment findByUUID(String uuid, YN viewYn);

    /**
     * UUID 문자열로 DB에 업로드 파일 정보를 저장하기 위한 AttachmentDto 반환 (단일)
     *
     * @param uuid 업로드 파일 UUID
     * @return AttachmentDto (id, uuid, uploadName, originalName, extension, path, realUrl, size, createdAt)
     * @author TAEROK HWANG
     */
    @Transactional
    AttachmentDto findAttachmentDtoByUUID(String uuid);

    /**
     * UUID 문자열로 DB에 업로드 파일 정보를 저장하기 위한 AttachmentDto 반환 (다중)
     *
     * @param uuids 업로드 파일 UUID ArrayList
     * @return AttachmentDto ArrayList (id, uuid, uploadName, originalName, extension, path, realUrl, size, createdAt)
     * @author TAEROK HWANG
     */
    @Transactional
    List<AttachmentDto> findAllAttachmentDtoByUUID(List<String> uuids);

    /**
     * UUID 문자열로 DB 에서 정보를 조회하여 파일 삭제 및 viewYn=Y <br />
     * 업로드 된 파일을 삭제하지만 DB 에는 rows 를 삭제하지 않고 viewYn=N 으로 변경하여 이력을 남긴다.
     *
     * @param uuid 삭제할 파일의 uuid
     * @return 파일 삭제 성공 여부 (true: 삭제 성공, false: 삭제 실패)
     * @author TAEROK HWANG
     */
    @Transactional
    boolean deleteByUUID(String uuid);

    /**
     * 파일 미리보기 (이미지, PDF) 및 다운로드 <br />
     * MIME-Type 이 image/* 또는 application/pdf 인 경우에만 미리보기 가능 (브라우저에서 지원하는 경우) <br />
     *
     * @param uuid 파일 UUID
     * @param isDownload 다운로드 여부
     * @param thumbnailSize 이미지일 때 출력하기 위한 썸네일의 너비
     * @return 파일 스트림 출력 ResponseEntity <br />
     * DB에 파일 정보가 없거나 viewYn=Y 아니면 404 (Not Found) <br />
     * DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone) <br />
     * 이미지 또는 PDF 형식이 아니면 미리보기 불가 415 (Unsupported Media Type) <br />
     * 성공하면 200 (OK) 와 함께 파일 스트림 출력 <br />
     * @author TAEROK HWANG
     */
    @Transactional(readOnly = true)
    ResponseEntity<byte[]> readAttachment(String uuid, boolean isDownload, Integer thumbnailSize);

    /**
     * 파일 확장자 구하기 (FilenameUtils 사용하지 않기 위함)
     *
     * @param fileName 파일 이름
     * @return 파일 확장자, 없으면 빈 문자열 반환
     * @see <a href="https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java">...</a>
     */
    static String getExtension(String fileName) {
        String extension = "";

        if (fileName == null) return extension;

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p)
            extension = fileName.substring(i + 1);

        return extension;
    }

    static String getReadableFileSize(long size) {
        if (size <= 0) return "0B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.1f%s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

}
