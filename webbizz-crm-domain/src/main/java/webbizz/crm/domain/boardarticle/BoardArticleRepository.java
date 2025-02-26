package webbizz.crm.domain.boardarticle;

import org.springframework.data.jpa.repository.JpaRepository;
import webbizz.crm.domain.boardarticle.entity.BoardArticle;

public interface BoardArticleRepository extends
        JpaRepository<BoardArticle, Long>,
        BoardArticleRepositoryCustom {
}
