package Servlets;

import CustomExceptions.MissingParametersException;
import CustomExceptions.NoSuchCurrencyException;
import CustomExceptions.NoSuchExchangeRateException;
import Service.ExchangeRatesService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ExchangeResult;
import model.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/exchange")
public class GetExchangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        HashMap<String,String> params = new HashMap<>();
        params.put("BaseCurrencyCode", req.getParameter("from"));
        params.put("targetCurrencyCode", req.getParameter("to"));
        params.put("Amount",req.getParameter("amount"));

        try{
            ExchangeResult exchangeResult = ExchangeRatesService.CalculateTheExchange(params);
            out.println(gson.toJson(exchangeResult));
            resp.setStatus(200);

        } catch (NoSuchCurrencyException e) {
            out.print(gson.toJson(new Message(e.getMessage())));
            resp.setStatus(404);

        } catch (SQLException e) {
            out.print(gson.toJson(new Message(e.getMessage())));
            resp.setStatus(500);

        } catch (MissingParametersException e) {
            out.print(gson.toJson(new Message(e.getMessage())));
            resp.setStatus(400);

        }catch (NoSuchExchangeRateException e){
            out.print(gson.toJson(new Message("No such exchange rate")));
            resp.setStatus(404);

        }


    }
}
