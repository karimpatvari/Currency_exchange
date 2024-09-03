package Service;

import CustomExceptions.CurrencyAlreadyExistsException;
import CustomExceptions.MissingParametersException;
import CustomExceptions.NoSuchCurrencyException;
import DAO.CurrenciesDao;
import DTO.CurrencyDTO;
import model.CurrencyEntity;

import java.sql.SQLException;

public class CurrencyService {

    public static CurrencyEntity getCurrency(String pathInfo) throws SQLException, MissingParametersException, NoSuchCurrencyException {

        if (pathInfo == null) {
            throw new MissingParametersException();
        }else if (pathInfo.isEmpty()) {
            throw new MissingParametersException();
        }

        return CurrenciesDao.getCurrencyByCode(pathInfo);

    }


    public static CurrencyDTO addCurrency(CurrencyDTO currencyDTO) throws MissingParametersException, SQLException, CurrencyAlreadyExistsException {

        String name = currencyDTO.getFullName();
        String code = currencyDTO.getCode();
        String sign = currencyDTO.getSign();

        CurrencyEntity currencyEntity = new CurrencyEntity(null,code, name, sign);

        if (name == null || code == null || sign == null || name.isBlank() || code.isBlank() || sign.isBlank()) {
            throw new MissingParametersException("Full name, code or sign is missing");
        }

        try {
            CurrencyEntity currencyEntityWithID = CurrenciesDao.addCurrency(currencyEntity);
            currencyDTO.setId(currencyEntityWithID.getID());
            return currencyDTO;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19){
                throw new CurrencyAlreadyExistsException("Currency with code: "+ code + " already exists");
            }
            throw e;
        }

    }






}
