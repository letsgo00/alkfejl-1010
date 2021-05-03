import hu.alkfejl.dao.ScoreDAO;
import hu.alkfejl.dao.ScoreDAOImpl;
import hu.alkfejl.model.Score;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class LeaderBoardServlet extends HttpServlet {

    private ScoreDAO dao = new ScoreDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("<html><head><title>1010 Leaderboard</title></head><body>");
        response.getWriter().println("<h1>1010 Leaderboard</h1>");
        response.getWriter().println("<table>");
        response.getWriter().println("<tr><th>Position</th><th>Name</th><th>Score</th><th>Date</th></tr>");
        for (Score score : dao.getTop10()){
            response.getWriter().println("<tr><td>" + score.getPosition() + "</td><td>" + score.getName() + "</td><td>"
                    + score.getScore() + "</td><td>" + score.getDate().toString() + "</td></tr>");
        }
        response.getWriter().println("</table>");
        response.getWriter().println("</body></html>");
    }

}