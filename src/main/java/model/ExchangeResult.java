package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResult {
    private CurrencyEntity baseCurrency;
    private CurrencyEntity targetCurrency;
    private Double rate;
    private Double amount;
    private Double convertedAmount;

}
