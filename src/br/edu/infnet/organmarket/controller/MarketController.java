package br.edu.infnet.organmarket.controller;

import br.edu.infnet.organmarket.dao.MarketDAO;
import br.edu.infnet.organmarket.entity.Organ;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/read", "/create", "/update", "/delete", "/filter"})
public class MarketController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private MarketDAO dao;

    @Override
    public void init() throws ServletException {
        dao = MarketDAO.getInstance();
    }

    @Override
    public void destroy() {
        try {
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        String resource = "/read.jsp";

        if (action.equals("/create")) {
            resource = "create.jsp";
        } else if (action.equals("/delete")) {
        	req.setAttribute("organ", dao.getOneById(new Long(req.getParameter("id"))));
            resource = "delete.jsp";
        } else if (action.equals("/update")) {
            req.setAttribute("organ", dao.getOneById(new Long(req.getParameter("id"))));
            resource = "update.jsp";
        } else {
            req.setAttribute("organs", dao.getAll());
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher(resource);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        String resource = "/read.jsp";

        Organ organ = new Organ();
        
        if (action.equals("/create")) {
        	resource = "create.jsp";
        	organ.setOrgan(req.getParameter("organ"));
            organ.setPrice(new BigDecimal(req.getParameter("price")));
            organ.setDonator(req.getParameter("donator"));
            dao.insert(organ);
        	req.setAttribute("organ", req.getParameter("organ"));
        	resp.getWriter().write("Organ: " + req.getParameter("organ") + " inserted.");
        } else {
        	if (action.equals("/update")) {
            	organ.setId(Long.valueOf(req.getParameter("id")));
            	organ.setOrgan(req.getParameter("organ"));
                organ.setPrice(new BigDecimal(req.getParameter("price")));
                organ.setDonator(req.getParameter("donator"));
            	dao.update(organ);
            } else if (action.equals("/delete")) {
            	dao.delete(Long.valueOf(req.getParameter("id")));
            }
        	req.setAttribute("organs", dao.getAll());
        	RequestDispatcher dispatcher = req.getRequestDispatcher(resource);
        	dispatcher.forward(req, resp);
        }

    }
    
    @Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	String action = req.getServletPath();
        String resource = "/read.jsp";

        if (action.equals("/update")) {
            Organ organ = new Organ();
            organ.setId(Long.valueOf(req.getParameter("id")));
            organ.setOrgan(req.getParameter("organ"));
            organ.setPrice(new BigDecimal(req.getParameter("price")));
            organ.setDonator(req.getParameter("donator"));
            dao.update(organ);
        }

        req.setAttribute("organs", dao.getAll());
        RequestDispatcher dispatcher = req.getRequestDispatcher(resource);
        dispatcher.forward(req, resp);
	}
    
    @Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resource = "/read.jsp";
    	
    	dao.delete(Long.valueOf(req.getParameter("id")));
		
    	req.setAttribute("organs", dao.getAll());
        RequestDispatcher dispatcher = req.getRequestDispatcher(resource);
        dispatcher.forward(req, resp);
	}
    
}
