package sni.common.models.entities.converters;

import sni.common.models.enums.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String>
{
    @Override
    public String convertToDatabaseColumn(Role role)
    {
        if(role == null)
        {
            return null;
        }
        return role.name();
    }

    @Override
    public Role convertToEntityAttribute(String s)
    {
        if(s == null)
        {
            return null;
        }
        return Role.valueOf(s);
    }
}
