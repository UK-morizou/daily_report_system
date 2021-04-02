package controllers.reports;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class reportLikeServlet
 */
@WebServlet("/reports/likes")
public class ReportsLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsLikeServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    EntityManager em = DBUtil.createEntityManager();

    Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

    int page;
    try {
        page = Integer.parseInt(request.getParameter("page"));
    } catch(Exception e) {
        page = 1;
    }

    List<Like> likes = em.createNamedQuery("getLikes", Like.class)
                                     .setParameter("report", r)
                                     .setFirstResult(15 * (page - 1))
                                     .setMaxResults(15)
                                     .getResultList();

  long likes_count = (long)em.createNamedQuery("getLikesCount", Long.class)
  .setParameter("report", r)
  .getSingleResult();
//long likes_count = 1;



    em.close();

    request.setAttribute("report", r);
    request.setAttribute("likes", likes);
    request.setAttribute("likes_count", likes_count);
    request.setAttribute("page", page);

    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/likes.jsp");
    rd.forward(request,  response);
    }
}
