package DAO;

import CustomExceptions.NoSuchCurrencyException;
import Utils.DataSourceProvider;
import model.CurrencyEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    private static final DataSource dataSource = DataSourceProvider.dataSource();

    public static List<CurrencyEntity> getAllCurrencies() throws SQLException {

        List<CurrencyEntity> currencies = new ArrayList<>();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Currencies");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            CurrencyEntity currency = new CurrencyEntity();

            currency.setID(rs.getInt("ID"));
            currency.setCode(rs.getString("Code"));
            currency.setFullName(rs.getString("FullName"));
            currency.setSign(rs.getString("Sign"));

            currencies.add(currency);
        }

        rs.close();
        connection.close();
        ps.close();

        return currencies;

    }

    public static CurrencyEntity addCurrency(CurrencyEntity currency) throws SQLException {

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "Insert into Currencies(ID,Code,FullName,Sign) values(?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setObject(1, null);
        ps.setString(2, currency.getCode());
        ps.setString(3, currency.getFullName());
        ps.setString(4, currency.getSign());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            currency.setID(rs.getInt(1));
        }

        rs.close();
        connection.close();
        ps.close();

        return currency;


    }

    public static CurrencyEntity getCurrencyByID(int id) throws SQLException, NoSuchCurrencyException {

        CurrencyEntity currency = new CurrencyEntity();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Currencies WHERE ID = ?",
                PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setInt(1, id);


        ResultSet rs = ps.executeQuery();

        currency.setID(rs.getInt("ID"));
        currency.setCode(rs.getString("Code"));
        currency.setFullName(rs.getString("FullName"));
        currency.setSign(rs.getString("Sign"));


        if (currency.getID() == 0) {
            throw new NoSuchCurrencyException();
        }

        ps.close();
        connection.close();

        return currency;

    }

    public static CurrencyEntity getCurrencyByCode(String code) throws SQLException, NoSuchCurrencyException {

        CurrencyEntity currency = new CurrencyEntity();

        Connection connection = dataSource.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Currencies WHERE Code = ?",
                PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, code);


        try (ResultSet rs = ps.executeQuery()) {

            currency.setID(rs.getInt("ID"));
            currency.setCode(rs.getString("Code"));
            currency.setFullName(rs.getString("FullName"));
            currency.setSign(rs.getString("Sign"));

        }

        if (currency.getID() == null || currency.getID() == 0) {
            throw new NoSuchCurrencyException("Currency with code '" + code + "' not found");
        }

        ps.close();
        connection.close();

        return currency;

    }


}
