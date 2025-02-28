package webbizz.crm.service.member.dto;

import lombok.AllArgsConstructor;
import webbizz.crm.service.excel.annotation.ExcelColumn;
import webbizz.crm.service.excel.style.ExcelCellCenterStyle;
import webbizz.crm.service.excel.style.ExcelCellDateTimeStyle;
import webbizz.crm.service.excel.style.ExcelCellNumberStyle;

import java.time.LocalDateTime;

@AllArgsConstructor
public class MemberWithdrawalExcelDto {

    @ExcelColumn(
            columnPixel = 45,
            headerName = "번호",
            bodyStyle = ExcelCellNumberStyle.class
    )
    private Long id;

    @ExcelColumn(
            columnPixel = 70,
            headerName = "아이디"
    )
    private String loginId;

    @ExcelColumn(
            columnPixel = 70,
            headerName = "이름"
    )
    private String realName;

    @ExcelColumn(
            columnPixel = 150,
            headerName = "이메일"
    )
    private String email;

    @ExcelColumn(
            columnPixel = 90,
            headerName = "휴대폰 번호",
            bodyStyle = ExcelCellCenterStyle.class
    )
    private String mobileNumber;

    @ExcelColumn(
            columnPixel = 120,
            headerName = "가입 일시",
            bodyStyle = ExcelCellDateTimeStyle.class
    )
    private LocalDateTime createdAt;

    @ExcelColumn(
            columnPixel = 120,
            headerName = "탈퇴 일시",
            bodyStyle = ExcelCellDateTimeStyle.class
    )
    private LocalDateTime withdrawalAt;

    @ExcelColumn(
            columnPixel = 200,
            headerName = "탈퇴 사유"
    )
    private String withdrawalReason;

}
