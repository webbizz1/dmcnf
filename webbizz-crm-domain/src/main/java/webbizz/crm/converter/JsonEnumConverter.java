package webbizz.crm.converter;

import org.springframework.core.GenericTypeResolver;
import org.springframework.util.StringUtils;
import webbizz.crm.domain.BaseEnumSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public abstract class JsonEnumConverter<T extends Enum<T> & BaseEnumSet> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convertToEntityAttribute(String dbData) {
        if (StringUtils.hasText(dbData)) {
            Class<?> clazz = GenericTypeResolver.resolveTypeArgument(getClass(), JsonEnumConverter.class);
            if (clazz == null)
                throw new RuntimeException("Cannot resolve type argument: " + getClass().getName());

            return BaseEnumSet.of((Class<T>) clazz, dbData);
        }

        return null;
    }

}
