package ru.yandex.practicum.sht.commerce.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.sht.commerce.ia.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    PaymentDto mapToPaymentDto(Payment payment);
}
