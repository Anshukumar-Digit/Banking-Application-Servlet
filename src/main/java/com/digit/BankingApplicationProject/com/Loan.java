package com.digit.BankingApplicationProject.com;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Loan")
public class Loan extends HttpServlet {

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int lid = Integer.parseInt(req.getParameter("lid"));

		String url = "jdbc:mysql://localhost:3306/project1";

		String user = "root";

		String pwd = "Anshu0705@";

		HttpSession session=req.getSession();
		try {



			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			pstmt = con.prepareStatement("select *from loan where lid=?");

			pstmt.setInt(1,lid);
			
			rs=pstmt.executeQuery();
			if(rs.next()==true) {
				session.setAttribute("interest", rs.getInt("interest"));
				session.setAttribute("description",rs.getString("description"));
				resp.sendRedirect("/BankingApplication/LoanDetails.jsp");
			}
			else {
				resp.sendRedirect("/BankingApplication/LoanDetails.html");
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
