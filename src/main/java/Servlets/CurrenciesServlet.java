package Servlets;

import CustomExceptions.CurrencyAlreadyExistsException;
import CustomExceptions.MissingParametersException;
import DAO.CurrenciesDao;
import DTO.CurrencyDTO;
import Service.CurrencyService;
import Utils.CurrencyMapper;
import Utils.JsonUtils;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CurrencyEntity;
import model.Message;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    CurrencyMapper currencyMapper = Mappers.getMapper(CurrencyMapper.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();

        try {
            List<CurrencyEntity> currencies = CurrenciesDao.getAllCurrencies();
            resp.setStatus(200);
            out.print(JsonUtils.getJsonString(currencies));
            out.flush();

        } catch (SQLException e) {
            resp.setStatus(500);
            out.print("Internal Server Error");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        CurrencyEntity currencyEntity = new CurrencyEntity(
                null,
                req.getParameter("Code"),
                req.getParameter("FullName"),
                req.getParameter("Sign")
        );

        CurrencyDTO currencyDTO = currencyMapper.toCurrencyDTO(currencyEntity);

        try {
            CurrencyDTO currencyDTOWithID = CurrencyService.addCurrency(currencyDTO);
            resp.setStatus(201);
            out.println(gson.toJson(new Message("Currency added successfully"))+ ",");
            out.println(gson.toJson(currencyDTOWithID));
            return;

        } catch (SQLException e) {
            resp.setStatus(500);
            out.print(gson.toJson(new Message(e.getMessage())));
            return;

        } catch (MissingParametersException e) {
            resp.setStatus(400);
            out.print(gson.toJson(new Message("Missing required parameters")));
            return;
        } catch (CurrencyAlreadyExistsException e) {
            resp.setStatus(409);
            out.print(gson.toJson(new Message(e.getMessage())));
            return;
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(405);
        resp.getWriter().print("Method DELETE not allowed");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(405);
        resp.getWriter().print("Method PUT not allowed");
    }
}


