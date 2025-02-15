package servlet.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDao;
import entity.User;

@WebServlet("/api/login")
public class LoginServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    @Override
    	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    		String username = req.getParameter("username");
    		String password = req.getParameter("password");
    		
    		User user = UserDao.findUserByUsername(username);
    		if(user == null) { // 에러 처리
    			responseLoginError(resp);
    			return;
    		}
    		
    		if(!BCrypt.checkpw(password, user.getPassword())) { // user.getPassword() 암호화된 비밀번호
    			responseLoginError(resp);
    			return;
    		}
    		
    		HttpSession session = req.getSession();
    		session.setAttribute("authentication", user);
    		resp.sendRedirect("/ssa/index");
    	}
    
    private void responseLoginError(HttpServletResponse resp) throws IOException { 
    	StringBuilder responsebody = new StringBuilder();
		responsebody.append("<script>");
		responsebody.append("alert('사용자 정보를 확인해주세요.');");
		responsebody.append("history.back();");
		responsebody.append("</script>");
		
		resp.setContentType("text/html");
		resp.getWriter().println(responsebody.toString());
    }
    
//    private void responseLoginSuccess(HttpServletResponse resp) throws IOException { 
//    	StringBuilder responsebody = new StringBuilder();
//		responsebody.append("<script>");
//		responsebody.append("location.replace('/ssa/index');");
//		responsebody.append("</script>");
//		
//		resp.setContentType("text/html");
//		resp.getWriter().println(responsebody.toString());
//    }

}
