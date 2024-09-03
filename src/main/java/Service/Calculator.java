package Service;

import CustomExceptions.NoSuchCurrencyException;
import CustomExceptions.NoSuchExchangeRateException;
import DAO.ExchangeRatesDao;
import model.CurrencyEntity;
import model.ExchangeRateEntity;
import model.ExchangeResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class Calculator {

    public static ExchangeResult CountAB(CurrencyEntity BaseCurrency, CurrencyEntity TargetCurrency, BigDecimal amount) throws NoSuchExchangeRateException, NoSuchCurrencyException, SQLException {
        ExchangeRateEntity exchangeRate = ExchangeRatesDao.getExchangeRateByIdPair(BaseCurrency.getID(), TargetCurrency.getID());

        BigDecimal Rate = BigDecimal.valueOf(exchangeRate.getRate());

        BigDecimal convertedAmount = Rate.multiply(amount);

        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

        Rate = Rate.setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResult(
                BaseCurrency,
                TargetCurrency,
                Rate.doubleValue(),
                amount.doubleValue(),
                convertedAmount.doubleValue());
    }

    public static ExchangeResult CountBA(CurrencyEntity BaseCurrency, CurrencyEntity TargetCurrency, BigDecimal amount) throws NoSuchExchangeRateException, NoSuchCurrencyException, SQLException {

        ExchangeRateEntity exchangeRate = ExchangeRatesDao.getExchangeRateByIdPair(TargetCurrency.getID(), BaseCurrency.getID());

        BigDecimal Rate = new BigDecimal(String.valueOf(exchangeRate.getRate()));
        BigDecimal divisor = new BigDecimal(1);

        Rate = divisor.divide(Rate, 2, RoundingMode.HALF_UP);

        BigDecimal Amount = new BigDecimal(String.valueOf(amount));

        BigDecimal convertedAmount = Amount.multiply(Rate);

        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

        Rate = Rate.setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResult(
                BaseCurrency,
                TargetCurrency,
                Rate.doubleValue(),
                amount.doubleValue(),
                convertedAmount.doubleValue());

    }

    public static ExchangeResult CountUSDaUSDb(CurrencyEntity BaseCurrency, CurrencyEntity TargetCurrency, Double amount) throws SQLException, NoSuchExchangeRateException, NoSuchCurrencyException {

        BigDecimal usdToA = new BigDecimal(String.valueOf(getRateUSDtoA(BaseCurrency.getID())));
        BigDecimal usdToB = new BigDecimal(String.valueOf(getRateUSDtoA(TargetCurrency.getID())));

        BigDecimal rateAB = usdToB.divide(usdToA,RoundingMode.HALF_UP);

        BigDecimal Amount = new BigDecimal(String.valueOf(amount));
        BigDecimal convertedAmount = Amount.multiply(rateAB);

        convertedAmount = convertedAmount.setScale(2, RoundingMode.HALF_UP);

        rateAB = rateAB.setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResult(
                BaseCurrency,
                TargetCurrency,
                rateAB.doubleValue(),
                amount,
                convertedAmount.doubleValue());


    }

    private static BigDecimal getRateUSDtoA(Integer id) throws NoSuchExchangeRateException, NoSuchCurrencyException, SQLException {

        try{
            ExchangeRateEntity exchangeRate = ExchangeRatesDao.getExchangeRateByIdPair(2, id);
            BigDecimal Rate = new BigDecimal(exchangeRate.getRate());
            return Rate;
        }catch (Exception e){
            try{
                ExchangeRateEntity exchangeRate = ExchangeRatesDao.getExchangeRateByIdPair(id, 2);
                BigDecimal divisor = new BigDecimal(1);
                BigDecimal Rate = new BigDecimal(String.valueOf(exchangeRate.getRate()));
                Rate = divisor.divide(Rate);
                return Rate;
            }catch (Exception e2){
                throw new NoSuchExchangeRateException();
            }

        }



    }


}
