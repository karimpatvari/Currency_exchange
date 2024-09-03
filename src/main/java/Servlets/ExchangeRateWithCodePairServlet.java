package Servlets;

import CustomExceptions.MissingParametersException;
import CustomExceptions.NoSuchCurrencyException;
import CustomExceptions.NoSuchExchangeRateException;
import CustomExceptions.WasntAbleToUpdateException;
import DTO.ExchangeRateDTO;
import Service.ExchangeRatesService;
import Utils.ExchangeRateMapper;
import Utils.PatchHttpServletRequestWrapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExchangeRateEntity;
import model.Message;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/exchangeRate/*")
public class ExchangeRateWithCodePairServlet extends HttpServlet {

    static ExchangeRateMapper exchangeRateMapper = Mappers.getMapper(ExchangeRateMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try{
            ExchangeRateDTO exchangeRateDTO = ExchangeRatesService.getExchangeRateByCodes(req.getPathInfo().substring(1));
            ExchangeRateEntity exchangeRateEntity = exchangeRateMapper.toExchangeRateEntity(exchangeRateDTO);
            resp.setStatus(200);
            out.println(gson.toJson(exchangeRateEntity));
            return;

        } catch (NoSuchExchangeRateException | NoSuchCurrencyException e) {
            resp.setStatus(404);
            out.print(gson.toJson(new Message(e.getMessage())));
            return;

        } catch (SQLException e) {
            resp.setStatus(500);
            out.print(gson.toJson(new Message("Internal server error")));
            return;

        } catch (MissingParametersException e) {
            resp.setStatus(400);
            out.print(gson.toJson(new Message(e.getMessage())));
        }




    }

    @Override //PATCH
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {

            PatchHttpServletRequestWrapper wrappedRequest = new PatchHttpServletRequestWrapper(req);

            PrintWriter out = resp.getWriter();
            Gson gson = new Gson();

            try{

                Map<String, String> params = new HashMap<>();
                params.put("pathInfo", wrappedRequest.getPathInfo().substring(1));
                params.put("rate", wrappedRequest.getParameter("rate"));

                ExchangeRateDTO exchangeRateDTO = ExchangeRatesService.updateExchangeRate(params);

                ExchangeRateEntity exchangeRateEntity = exchangeRateMapper.toExchangeRateEntity(exchangeRateDTO);

                out.println(gson.toJson(new Message("Successfully updated exchange rate")));
                out.println(gson.toJson(exchangeRateEntity));
                resp.setStatus(200);

            } catch (NoSuchExchangeRateException | NoSuchCurrencyException e) {
                out.print(gson.toJson(new Message(e.getMessage())));
                resp.setStatus(404);

            } catch (SQLException | WasntAbleToUpdateException e) {
                out.print(gson.toJson(new Message(e.getMessage())));
                resp.setStatus(500);

            } catch (MissingParametersException e) {
                out.print(gson.toJson(new Message(e.getMessage())));
                resp.setStatus(400);

            }


        } else {
            super.service(req, resp);
        }
    }

}
