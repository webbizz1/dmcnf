package webbizz.crm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public abstract class PageCondition {

    /**
     * 페이지 번호
     */
    protected Integer page = 1;

    /**
     * 한 페이지에 보여줄 데이터 수
     */
    protected Integer size = 10;

    public Pageable pageable() {
        return PageRequest.of(this.getPage() - 1, this.getSize());
    }

}
