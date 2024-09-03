package Utils;

import DTO.CurrencyDTO;
import model.CurrencyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CurrencyMapper {

    @Mapping(target = "id",source = "ID")
    CurrencyDTO toCurrencyDTO(CurrencyEntity currency);

    @Mapping(target = "ID", source = "id")
    CurrencyEntity toCurrencyEntity(CurrencyDTO currencyDTO);

}
