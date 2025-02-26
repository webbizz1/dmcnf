package webbizz.crm.domain.survey;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webbizz.crm.domain.YN;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SurveyRepositoryTest {

    @Autowired
    SurveyRepository surveyRepository;

    @Test
    @DisplayName("save")
    void save() throws Exception {

        for (int i = 0; i < 500; i++) {
            surveyRepository.save(Survey.builder()
                    .title("title" + i)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusDays(365))
                    .viewYn(YN.Y)
                    .build());
        }

    }

    @Test
    @DisplayName("findAll")
    void findAll () throws Exception {
        surveyRepository.findAll().forEach(survey -> {
            assertThat(survey.getDelYn()).isEqualTo(YN.N);
        });
    }


    @Test
    @DisplayName("softDelete")
//    @Transactional
    void softDelete () throws Exception {
        // given
        final Survey survey = surveyRepository.findById(2L).orElseThrow(RuntimeException::new);

//         when
        surveyRepository.delete(survey);
//
//         then
//        assertThat(survey.getDelYn()).isEqualTo(YN.Y);
    }
}
