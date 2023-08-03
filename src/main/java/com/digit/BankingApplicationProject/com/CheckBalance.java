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

@WebServlet("/CheckBalance")
public class CheckBalance extends HttpServlet {
	private Connection con;
	private PreparedStatement pstmt;
	private int cust_id;
	private ResultSet rs;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session=req.getSession();
		int accno=(int)session.getAttribute("accno");


		String url = "jdbc:mysql://localhost:3306/project1";

		String user = "root";

		String pwd = "Anshu0705@";

		
		try {



			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			pstmt = con.prepareStatement("select balance from bankapp where accno=?");

			pstmt.setInt(1,accno);


			rs=pstmt.executeQuery();
			if(rs.next()==true) {
				session.setAttribute("balance", rs.getInt("balance"));
				//session.setAttribute("cust_name",rs.getString("cust_name"));
				resp.sendRedirect("/BankingApplication/Balance.jsp");
			}
			else {
				resp.sendRedirect("/BankingApplication/BalanceFail.jsp");
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
