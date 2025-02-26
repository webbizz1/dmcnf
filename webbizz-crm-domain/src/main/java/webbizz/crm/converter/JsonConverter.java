package webbizz.crm.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter
public abstract class JsonConverter<T> implements AttributeConverter<T, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonConverter() {
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (!ObjectUtils.isEmpty(attribute))
            try {
                return objectMapper.writeValueAsString(attribute);
            } catch (JsonProcessingException ignored) {}

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            Class<?> clazz = GenericTypeResolver.resolveTypeArgument(getClass(), JsonConverter.class);
            if (clazz == null)
                throw new RuntimeException("Cannot resolve type argument: " + getClass().getName());

            try {
                return (T) objectMapper.readValue(dbData, clazz);
            } catch (IOException ignored) {}
        }

        return null;
    }

}
