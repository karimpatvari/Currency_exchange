package Servlets;

import CustomExceptions.ExchangeRateAlreadyExistsException;
import CustomExceptions.MissingParametersException;
import CustomExceptions.NoSuchCurrencyException;
import DAO.ExchangeRatesDao;
import DTO.ExchangeRateDTO;
import Service.ExchangeRatesService;
import Utils.JsonUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExchangeRateEntity;
import model.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try {
            List<ExchangeRateEntity> rates = ExchangeRatesDao.getAllExchangeRates();
            resp.setStatus(200);
            out.print(JsonUtils.getJsonStringFromExchangeRates(rates));

        } catch (SQLException e) {
            resp.setStatus(500);
            out.println(gson.toJson(new Message("Internal server error")));
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(null,
                req.getParameter("baseCurrencyCode"),
                req.getParameter("targetCurrencyCode"),
                req.getParameter("rate"));

        try {
            ExchangeRateEntity exchangeRateEntity = ExchangeRatesService.addExchangeRate(exchangeRateDTO);
            resp.setStatus(201);
            out.println(gson.toJson(exchangeRateEntity));
            return;

        } catch (MissingParametersException e) {
            resp.setStatus(400);
            out.println(gson.toJson(new Message("Missing required parameters")));
            return;

        } catch (NoSuchCurrencyException e) {
            resp.setStatus(404);
            out.println(gson.toJson(new Message(e.getMessage())));
            return;

        } catch (SQLException e) {
            resp.setStatus(500);
            out.println(gson.toJson(new Message("Internal server error")));
            return;

        } catch (ExchangeRateAlreadyExistsException e) {
            resp.setStatus(409);
            out.println(gson.toJson(new Message("Exchange rate already exists")));
            return;

        }


    }

}
