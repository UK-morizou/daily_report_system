package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIineServlet
 */
@WebServlet("/reports/iine")
public class ReportsIineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIineServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int iine_count = r.getIine() + 1;
        r.setIine(iine_count);

        /*
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
         */
        Like l = new Like();

        l.setEmployee((Employee) request.getSession().getAttribute("login_employee"));
        l.setReport(r);

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);

        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();

        request.getSession().setAttribute("flush", "いいね しました。");

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}
