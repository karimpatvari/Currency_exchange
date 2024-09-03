package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateEntity {

    private Integer ID;
    private CurrencyEntity BaseCurrency;
    private CurrencyEntity TargetCurrency;
    private double Rate;

}
