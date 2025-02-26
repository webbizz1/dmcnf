package webbizz.crm.service.attachment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import webbizz.crm.config.properties.AttachmentProperties;
import webbizz.crm.domain.YN;
import webbizz.crm.domain.attachment.AttachmentRepository;
import webbizz.crm.domain.attachment.dto.AttachmentDto;
import webbizz.crm.domain.attachment.entity.Attachment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 파일 업로드/다운로드 서비스 구현체
 *
 * @apiNote 2.0 버전에서는 ConfigurationProperties 를 사용하여 타입 안정성을 확보
 * @since 2022-08-05
 * @author TAEROK HWANG
 * @version 2.0
 */
@Slf4j
@Service("attachmentService")
@EnableConfigurationProperties(AttachmentProperties.class)
@RequiredArgsConstructor
public class AttachmentServiceImpl extends EgovAbstractServiceImpl implements AttachmentService {

    private final AttachmentProperties properties;
//    private final MultipartProperties multipartProperties;

    private final AttachmentRepository attachmentRepository;

    public Attachment uploadFile(MultipartFile file, String source) throws IOException {
        // 랜덤 UUID 생성
        UUID uuid = UUID.randomUUID();
        // 일별로 구분 (예: 2022/08/05)
        String splitDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        // 원본 파일 이름
        String fileName = file.getOriginalFilename();
        // 원본 파일 확장자
        String fileExtension = AttachmentService.getExtension(fileName).toLowerCase();
        // 원본 파일 저장 경로
        File originalDir = new File(properties.getPath() + "/" + splitDir);
        // 원본 파일 저장 경로 (확장자 포함)
        File transferFile = new File(properties.getPath() + "/" + splitDir
                + "/" + uuid + (!fileExtension.isEmpty() ? "." + fileExtension : ""));

        // 파일 확장자 정책 적용
        String[] allowExtensions = properties.getAllowExtensions();

        if (Arrays.stream(allowExtensions).noneMatch(fileExtension::equalsIgnoreCase)) {
            if (fileExtension.isEmpty())
                throw new MultipartException("업로드 할 수 없는 파일입니다. (확장자가 없는 파일)");
            else
                throw new MultipartException("업로드 할 수 없는 파일입니다. (허용되지 않는 확장자: " + fileExtension + ")");
        }

        // 원본 파일을 저장할 디렉토리 생성
        if (!originalDir.exists() && !originalDir.mkdirs())
            throw new IOException("업로드 디렉토리를 생성할 수 없습니다.");

        // 업로드 파일 원본 디렉토리로 이동
        file.transferTo(transferFile);

        // Unix, Windows 경로 구분자 '/' 로 통일
        String path = transferFile.getCanonicalPath()
                .replace(new File(properties.getPath()).getCanonicalPath(), "")
                .replace("\\", "/");

        // DB Insert
        Attachment attachment = Attachment.builder()
                .uuid(uuid.toString())
                .uploadName(transferFile.getName())
                .originalName(fileName)
                .extension(fileExtension)
                .path(path)
                .realUrl(properties.getServiceDomain() + "/uploads" + path)
                .size(file.getSize())
                .source(source)
                .viewYn(YN.of(source != null && source.contains("editor")))
                .delYn(YN.N)
                .build();
        attachmentRepository.save(attachment);

        return attachment;
    }

    public List<Attachment> uploadFiles(List<MultipartFile> files, String source) throws IOException {

        if (files == null || files.isEmpty())
            throw new IOException("업로드 할 파일이 없습니다.");

        List<Attachment> attachments = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        for (MultipartFile file : files)
            try {
                attachments.add(this.uploadFile(file, source));
            } catch (IOException | MultipartException e) {
                log.error(e.getMessage());
                errorMessages.add(e.getMessage());
            }

        if (attachments.isEmpty())
            throw new IOException(errorMessages.size() == 1 ?
                    errorMessages.get(0)
                    :
                    String.format("파일 업로드에 실패했습니다. (실패한 파일 수: %d)", errorMessages.size()));

        return attachments;
    }

    @Transactional(readOnly = true)
    public Attachment findByUUID(String uuid) {
        return this.findByUUID(uuid, YN.N);
    }

    @Transactional
    public Attachment findByUUID(String uuid, YN viewYn) {
        Attachment attachment = attachmentRepository.findByUUID(uuid).orElse(null);

        // 에디터가 아닌 일반 폼으로 업로드하면 viewYn=N 이므로 ID로 접근(저장 행동)했을 때 viewYn=Y 처리
        if (attachment != null && viewYn == YN.Y)
            attachment.setViewYn(YN.Y);

        return attachment;
    }

    @Transactional
    public AttachmentDto findAttachmentDtoByUUID(String uuid) {
        Attachment attachment = this.findByUUID(uuid, YN.Y);

        return attachment != null ? attachment.toDto() : null;
    }

    @Transactional
    public List<AttachmentDto> findAllAttachmentDtoByUUID(List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) return new ArrayList<>();

        return uuids.stream()
                .map(uuid -> {
                    Attachment attachment = this.findByUUID(uuid, YN.Y);

                    return attachment != null ? attachment.toDto() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteByUUID(String uuid) {
        Attachment attachment = this.findByUUID(uuid);
        if (attachment == null) return false;

        boolean isDeleted = false;
        File file = new File(properties.getPath() + attachment.getPath());

        if (file.exists() && file.isFile())
            isDeleted = file.delete();

        attachment.setDelYn(YN.Y);

        log.info("파일 삭제: UUID={}, path={}, isDeleted={}", uuid, file.getPath(), isDeleted);
        return isDeleted;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> readAttachment(String uuid, boolean isDownload, Integer thumbnailSize) {
        Attachment attachment = this.findByUUID(uuid);

        // DB에 파일 정보가 없거나 viewYn=Y 아니면 404
        if (attachment == null || attachment.getViewYn() != YN.Y)
            return ResponseEntity.notFound().build();

        Path path = Paths.get(properties.getPath() + attachment.getPath());

        // DB에 파일 정보는 있지만 실제 파일이 없는 경우 410 (Gone)
        if (!path.toFile().exists())
            return ResponseEntity.status(410).build();

        try {
            String contentType = Files.probeContentType(path);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();

            // MIME TYPE 를 찾는데 실패한 경우 기본 값 지정
            if (contentType == null)
                contentType = MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE;

            // 다운로드 요청일 경우
            if (isDownload) {
                bodyBuilder
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                ContentDisposition.builder("attachment")
                                        .filename(attachment.getOriginalName(), StandardCharsets.UTF_8)
                                        .build().toString())
                        .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                        .header(HttpHeaders.PRAGMA, "no-cache")
                        .header(HttpHeaders.EXPIRES, "0");
            } else { // 미리보기 요청
                // 이미지 또는 PDF 형식이 아니면 미리보기 불가 처리
                if (Arrays.stream(new String[]{"image/", "application/pdf"}).noneMatch(contentType::startsWith))
                    return ResponseEntity.status(415).build();

                bodyBuilder.cacheControl(CacheControl.maxAge(Duration.ofHours(1L)));
            }

            // 이미지 너비가 지정되어 있으면
            if (thumbnailSize != null && thumbnailSize > 0) {
                try {
                    // 크기에 맞춰 썸네일 생성
                    Thumbnails.of(path.toFile())
                            .width(thumbnailSize)
                            .outputFormat("JPEG")
                            .toOutputStream(outputStream);

                    bodyBuilder.contentType(MediaType.IMAGE_JPEG);
                } catch (IOException e) {
                    log.warn("썸네일 생성 실패: UUID={}, size={}", uuid, thumbnailSize);
                }
            }

            // 썸네일 요청 하지 않거나 썸네일 생성 실패했다면 원본 이미지로
            if (outputStream.size() < 1) {
                Files.copy(path, outputStream);
                bodyBuilder.contentType(MediaType.valueOf(contentType));
            }

            return bodyBuilder
                    .contentLength(outputStream.size())
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            log.warn("파일 처리 실패: {}, UUID={}", e.getMessage(), uuid);
            return ResponseEntity.internalServerError().build();
        }
    }

}
