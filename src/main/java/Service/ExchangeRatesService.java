package Service;

import CustomExceptions.*;
import DAO.CurrenciesDao;
import DAO.ExchangeRatesDao;
import DTO.ExchangeRateDTO;
import Utils.ExchangeRateMapper;
import model.CurrencyEntity;
import model.ExchangeRateEntity;
import model.ExchangeResult;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import static Service.Calculator.*;

public class ExchangeRatesService {

    static ExchangeRateMapper exchangeRateMapper = Mappers.getMapper(ExchangeRateMapper.class);

    public static ExchangeRateDTO getExchangeRateByCodes(String pathInfo) throws SQLException, NoSuchExchangeRateException, NoSuchCurrencyException, MissingParametersException {

        if (pathInfo.length() != 6) {
            throw new MissingParametersException("Missing parameters");
        }

        String firstCurrencyCode = pathInfo.substring(0, 3);
        String secondCurrencyCode = pathInfo.substring(3);

        int baseCurrencyId = CurrenciesDao.getCurrencyByCode(firstCurrencyCode).getID();
        int targetCurrencyId = CurrenciesDao.getCurrencyByCode(secondCurrencyCode).getID();

        ExchangeRateEntity exchangeRateEntity = ExchangeRatesDao.getExchangeRateByIdPair(baseCurrencyId, targetCurrencyId);

        return exchangeRateMapper.toExchangeRateDTO(exchangeRateEntity);
    }

    public static ExchangeRateDTO updateExchangeRate(Map<String,String> params) throws SQLException, WasntAbleToUpdateException, MissingParametersException, NoSuchExchangeRateException, NoSuchCurrencyException {

        String pathInfo = params.get("pathInfo");
        String Rate = params.get("rate");

        if (pathInfo == null || pathInfo.length() < 6) {
            throw new MissingParametersException("Missing Currency Codes");
        }

        String baseCurrencyCode = pathInfo.substring(0, 3);
        String targetCurrencyCode = pathInfo.substring(3);

        if (Rate == null || Rate.isEmpty()) {
            throw new MissingParametersException("Missing Rate");
        }

        Double rate = Double.parseDouble(Rate);

        Integer BaseCurrencyId = CurrenciesDao.getCurrencyByCode(baseCurrencyCode).getID();
        Integer TargetCurrencyId = CurrenciesDao.getCurrencyByCode(targetCurrencyCode).getID();

        Integer exchangeRateID = ExchangeRatesDao.getExchangeRateByIdPair(BaseCurrencyId, TargetCurrencyId).getID();

        ExchangeRateEntity exchangeRateEntity = ExchangeRatesDao.updateExchangeRate(rate, exchangeRateID);

        return exchangeRateMapper.toExchangeRateDTO(exchangeRateEntity);

    }

    public static ExchangeResult CalculateTheExchange(Map<String,String> params) throws MissingParametersException, NoSuchCurrencyException, SQLException, NoSuchExchangeRateException {

        String baseCurrencyCode = params.get("BaseCurrencyCode");
        String targetCurrencyCode = params.get("targetCurrencyCode");
        String Amount = params.get("Amount");

        if (baseCurrencyCode == null || targetCurrencyCode == null) {
            throw new MissingParametersException("No base or target currency");
        }

        if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty()) {
            throw new MissingParametersException("No base or target currency");
        }

        if (Amount == null || Double.parseDouble(Amount) < 0.1) {
            throw new MissingParametersException("Amount must be greater than zero");
        }

        CurrencyEntity BASE_CURRENCY = CurrenciesDao.getCurrencyByCode(baseCurrencyCode);
        CurrencyEntity TARGET_CURRENCY = CurrenciesDao.getCurrencyByCode(targetCurrencyCode);
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(Amount));

        try {
            return CountAB(BASE_CURRENCY, TARGET_CURRENCY, amount);

        } catch (NoSuchExchangeRateException e) {

            try {
                return CountBA(BASE_CURRENCY, TARGET_CURRENCY, amount);

            } catch (NoSuchExchangeRateException ex) {

                return CountUSDaUSDb(BASE_CURRENCY, TARGET_CURRENCY, amount.doubleValue());

            } catch (NoSuchCurrencyException | SQLException ex) {
                throw e;
            }

        } catch (NoSuchCurrencyException | SQLException e) {
            throw e;
        }


        //
    }

    public static ExchangeRateEntity addExchangeRate(ExchangeRateDTO exchangeRateDTO) throws MissingParametersException, NoSuchCurrencyException, SQLException, ExchangeRateAlreadyExistsException {

        String baseCurrencyCode = exchangeRateDTO.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateDTO.getTargetCurrencyCode();

        if (exchangeRateDTO.getRate() == null) {
            throw new MissingParametersException();
        }

        if (exchangeRateDTO.getRate().isEmpty()) {
            throw new MissingParametersException();
        }

        double rate = Double.parseDouble(exchangeRateDTO.getRate());

        if (baseCurrencyCode == null || targetCurrencyCode == null) {
            throw new MissingParametersException();
        }

        if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty()) {
            throw new MissingParametersException();
        }

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity(null,
                CurrenciesDao.getCurrencyByCode(baseCurrencyCode),
                CurrenciesDao.getCurrencyByCode(targetCurrencyCode),
                rate);

        try {
            ExchangeRateEntity exchangeRateEntityWithID = ExchangeRatesDao.addExchangeRate(exchangeRateEntity);
            return exchangeRateEntityWithID;
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                throw new ExchangeRateAlreadyExistsException();
            }

            throw e;
        }


    }

}
