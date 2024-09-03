package DAO;

import CustomExceptions.NoSuchCurrencyException;
import CustomExceptions.NoSuchExchangeRateException;
import CustomExceptions.WasntAbleToUpdateException;
import Utils.DataSourceProvider;
import model.ExchangeRateEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    private static final DataSource dataSource = DataSourceProvider.dataSource();

    public static List<ExchangeRateEntity> getAllExchangeRates() throws SQLException {

        List<ExchangeRateEntity> rates = new ArrayList<>();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM ExchangeRates");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

                exchangeRateEntity.setID(rs.getInt(1));
                try{
                    exchangeRateEntity.setBaseCurrency(CurrenciesDao.getCurrencyByID(rs.getInt(2)));
                }catch (NoSuchCurrencyException e){
                    continue;
                }
                try{
                    exchangeRateEntity.setTargetCurrency(CurrenciesDao.getCurrencyByID(rs.getInt(3)));
                }catch (NoSuchCurrencyException e){
                    continue;
                }
                exchangeRateEntity.setRate(rs.getDouble(4));
                rates.add(exchangeRateEntity);
            }
        }

        connection.close();
        ps.close();

        return rates;


    }

    public static ExchangeRateEntity getExchangeRateByIdPair(int baseCurrencyId, int targetCurrencyId) throws NoSuchExchangeRateException, SQLException, NoSuchCurrencyException {

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?");

        ps.setInt(1, baseCurrencyId);
        ps.setInt(2, targetCurrencyId);

        try (ResultSet rs = ps.executeQuery()) {
            rs.next();

            exchangeRateEntity.setID(rs.getInt(1));
            exchangeRateEntity.setBaseCurrency(CurrenciesDao.getCurrencyByID(baseCurrencyId));
            exchangeRateEntity.setTargetCurrency(CurrenciesDao.getCurrencyByID(targetCurrencyId));
            exchangeRateEntity.setRate(rs.getDouble(4));

        }

        ps.close();
        connection.close();

        if (exchangeRateEntity.getID() == 0) {
            throw new NoSuchExchangeRateException("No such Exchange Rate with base currency: " + CurrenciesDao.getCurrencyByID(baseCurrencyId).getCode() + " and target currency: " + CurrenciesDao.getCurrencyByID(targetCurrencyId).getCode());
        }

        return exchangeRateEntity;
    }

    public static ExchangeRateEntity addExchangeRate(ExchangeRateEntity exchangeRateEntity) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO ExchangeRates(ID,BaseCurrencyId,TargetCurrencyId,Rate) values(?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setObject(1, null);
        ps.setInt(2, exchangeRateEntity.getBaseCurrency().getID());
        ps.setInt(3, exchangeRateEntity.getTargetCurrency().getID());
        ps.setDouble(4, exchangeRateEntity.getRate());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            exchangeRateEntity.setID(rs.getInt(1));
        }

        ps.close();
        connection.close();

        return exchangeRateEntity;


    }

    public static ExchangeRateEntity updateExchangeRate(Double Rate, Integer Id) throws SQLException, WasntAbleToUpdateException, NoSuchExchangeRateException, NoSuchCurrencyException {

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "UPDATE ExchangeRates SET Rate = ? WHERE ID = ?",
                PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setDouble(1, Rate);
        ps.setInt(2, Id);
        ps.executeUpdate();

        ExchangeRateEntity exchangeRateEntity = ExchangeRatesDao.getExchangeRateById(Id);

        if (exchangeRateEntity.getRate() != Rate) {
            throw new WasntAbleToUpdateException();
        }

        ps.close();
        connection.close();

        return exchangeRateEntity;

    }

    public static ExchangeRateEntity getExchangeRateById(Integer ID) throws SQLException, NoSuchExchangeRateException, NoSuchCurrencyException {

        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM ExchangeRates WHERE ID =?");

        ps.setInt(1, ID);

        try (ResultSet rs = ps.executeQuery()) {
            rs.next();

            exchangeRateEntity.setID(rs.getInt(1));
            exchangeRateEntity.setBaseCurrency(CurrenciesDao.getCurrencyByID(rs.getInt(2)));
            exchangeRateEntity.setTargetCurrency(CurrenciesDao.getCurrencyByID(rs.getInt(3)));
            exchangeRateEntity.setRate(rs.getDouble(4));

        }

        if (exchangeRateEntity.getID() == 0) {
            throw new NoSuchExchangeRateException();
        }

        ps.close();
        connection.close();

        return exchangeRateEntity;

    }


}
