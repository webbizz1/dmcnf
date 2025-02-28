package webbizz.crm.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public abstract class JsonListConverter<T> implements AttributeConverter<List<T>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<T> attribute) {
        if (!ObjectUtils.isEmpty(attribute))
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException ignored) {}

        return "[]";
    }

    @Override
    public List<T> convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            Class<?> clazz = GenericTypeResolver.resolveTypeArgument(getClass(), JsonListConverter.class);
            if (clazz == null)
                throw new RuntimeException("Cannot resolve type argument: " + getClass().getName());

            try {
                return objectMapper.readValue(dbData,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (IOException ignored) {}
        }

        return new ArrayList<>();
    }

}
