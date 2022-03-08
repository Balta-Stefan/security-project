package sni.common.models.entities.converters;

import sni.common.models.enums.Operation;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OperationConverter implements AttributeConverter<Operation, Character>
{
    @Override
    public Character convertToDatabaseColumn(Operation operation)
    {
        if(operation == null)
        {
            return null;
        }
        return operation.operation;
    }

    @Override
    public Operation convertToEntityAttribute(Character character)
    {
        if(character == null)
        {
            return null;
        }

        return Stream.of(Operation.values())
                .filter(o -> o.operation.equals(character))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
