package Utils;

import CustomExceptions.NoSuchCurrencyException;
import DAO.CurrenciesDao;
import DTO.ExchangeRateDTO;
import model.CurrencyEntity;
import model.ExchangeRateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.SQLException;

@Mapper
public interface ExchangeRateMapper {

    @Mapping(target = "baseCurrencyCode", expression = "java(this.getCurrencyCode(exchangeRateEntity.getBaseCurrency()))")
    @Mapping(target = "targetCurrencyCode", expression = "java(this.getCurrencyCode(exchangeRateEntity.getTargetCurrency()))")
    ExchangeRateDTO toExchangeRateDTO(ExchangeRateEntity exchangeRateEntity);

    default String getCurrencyCode(CurrencyEntity currencyEntity) {
        return currencyEntity.getCode();
    }

    @Mapping(target = "baseCurrency", expression = "java(this.getCurrencyEntityFromCode(exchangeRateDTO.getBaseCurrencyCode()))")
    @Mapping(target = "targetCurrency", expression = "java(this.getCurrencyEntityFromCode(exchangeRateDTO.getTargetCurrencyCode()))")
    ExchangeRateEntity toExchangeRateEntity(ExchangeRateDTO exchangeRateDTO);

    default CurrencyEntity getCurrencyEntityFromCode(String currencyCode) {
        try {
            return CurrenciesDao.getCurrencyByCode(currencyCode);
        } catch (SQLException | NoSuchCurrencyException e) {
            throw new RuntimeException(e);
        }
    }



}
