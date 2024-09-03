package Servlets;

import CustomExceptions.MissingParametersException;
import CustomExceptions.NoSuchCurrencyException;
import Service.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CurrencyEntity;
import model.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyWithCodeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();

        try{
            CurrencyEntity currency = CurrencyService.getCurrency(req.getPathInfo().substring(1));
            resp.setStatus(200);
            out.println(gson.toJson(currency));
            return;

        } catch (NoSuchCurrencyException e) {
            resp.setStatus(404);
            out.println(gson.toJson(new Message(e.getMessage())));
            return;

        } catch (SQLException e) {
            resp.setStatus(500);
            out.println(gson.toJson(new Message("Internal Server Error")));
            return;

        } catch (MissingParametersException e) {
            resp.setStatus(400);
            out.println(gson.toJson(new Message("Missing required parameters")));
            return;

        } catch (RuntimeException e){
            resp.setStatus(400);
            out.println(gson.toJson(new Message("Bad Request")));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(405);
        resp.getWriter().print("Method POST not allowed");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(405);
        resp.getWriter().print("Method PUT not allowed");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(405);
        resp.getWriter().print("Method DELETE not allowed");
    }
    
}
