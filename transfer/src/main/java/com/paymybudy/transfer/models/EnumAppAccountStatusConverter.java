package com.paymybudy.transfer.models;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class EnumAppAccountStatusConverter implements AttributeConverter<EnumAppAccountStatus, String> {

    @Override
    public String convertToDatabaseColumn(EnumAppAccountStatus enumAppAccountStatus) {
        if (enumAppAccountStatus == null){
            return null;
        }
        return enumAppAccountStatus.toString();
    }

    @Override
    public EnumAppAccountStatus convertToEntityAttribute(String label) {
        if (label == null){
            return null;
        }
        return Stream.of(EnumAppAccountStatus.values())
                .filter(e-> e.toString().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
