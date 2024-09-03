package DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDTO {
    private Integer ID;
    private String BaseCurrencyCode;
    private String TargetCurrencyCode;
    private String Rate;

}
