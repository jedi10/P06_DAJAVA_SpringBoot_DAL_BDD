package com.paymybudy.transfer.models;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EnumTransacStatusConverter implements AttributeConverter<EnumTransacStatus, String> {

    @Override
    public String convertToDatabaseColumn(EnumTransacStatus enumTransacStatus) {
        if (enumTransacStatus == null){
            return null;
        }
        return enumTransacStatus.toString();
    }

    @Override
    public EnumTransacStatus convertToEntityAttribute(String label) {
        if (label == null) {
            return null;
        }
        return Stream.of(EnumTransacStatus.values())
                .filter(c -> c.toString().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}


//https://www.baeldung.com/jpa-persisting-enums-in-jpa