package Utils;

import com.google.gson.Gson;
import model.CurrencyEntity;
import model.ExchangeRateEntity;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static List<String> getJsonString(List<CurrencyEntity> currencyEntities){
        List<String> list = new ArrayList<>();

        for (CurrencyEntity currencyEntity : currencyEntities) {
            Gson gson = new Gson();
            list.add(gson.toJson(currencyEntity));
        }

        return list;
    }

    public static String getJsonString(CurrencyEntity currencyEntity){
        Gson gson = new Gson();
        return gson.toJson(currencyEntity);
    }

    public static List<String> getJsonStringFromExchangeRates(List<ExchangeRateEntity> ExchangeRatesEntities){
        List<String> list = new ArrayList<>();

        for (ExchangeRateEntity exchangeRateEntity : ExchangeRatesEntities) {
            Gson gson = new Gson();
            list.add(gson.toJson(exchangeRateEntity));
        }

        return list;
    }




}
