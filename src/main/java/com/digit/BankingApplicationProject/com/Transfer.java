package com.digit.BankingApplicationProject.com;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Transfer")
public class Transfer extends HttpServlet {

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private ResultSet rs1;
	private ResultSet res2;
	private ResultSet res3;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int cust_id = Integer.parseInt(req.getParameter("cust_id"));
		String bank_name = (req.getParameter("bank_name"));
		String ifsc = (req.getParameter("ifsc"));
		int accno = Integer.parseInt(req.getParameter("sender_accno"));
		String receiver_ifsc = (req.getParameter("receiver_ifsc"));
		int receiver_accno = Integer.parseInt(req.getParameter("receiver_accno"));
		int amount = Integer.parseInt(req.getParameter("amount"));
		int pin = Integer.parseInt(req.getParameter("pin"));
		String url = "jdbc:mysql://localhost:3306/project1";

		String user = "root";

		String pwd = "Anshu0705@";

		HttpSession session=req.getSession();

		try {


      Random ran=new Random();
      int transid=ran.nextInt(1000000);
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, pwd);
			pstmt = con.prepareStatement("select *from bankapp where cust_id=? and ifsc_code=? and accno=? and pin=?");

			pstmt.setInt(1,cust_id);
			pstmt.setString(2,ifsc);
			pstmt.setInt(3,accno);
			pstmt.setInt(4,pin);

			rs1=pstmt.executeQuery();
			//for first
			if(rs1.next()==true) {
				pstmt=con.prepareStatement("select *from bankapp where ifsc_code=? and accno=?");
				pstmt.setString(1, receiver_ifsc);
				pstmt.setInt(2, receiver_accno);
				res2=pstmt.executeQuery();

				//for second
				if(res2.next()==true) {
					pstmt=con.prepareStatement("select balance from bankapp where accno=?");

					pstmt.setInt(1, accno);

					res3=pstmt.executeQuery();
					res3.next();
					int bal=res3.getInt(1);

					//for third
					if(bal>amount) {

						pstmt=con.prepareStatement("update bankapp set balance=balance-? where accno=?");
						pstmt.setInt(1, amount);
						pstmt.setInt(2,accno);
						int x1=pstmt.executeUpdate();

						//for fourth
						if(x1>0) {
							pstmt=con.prepareStatement("update bankapp set balance=balance+? where accno=?");


							pstmt.setInt(1, amount);
							pstmt.setInt(2, receiver_accno);
							int x2=pstmt.executeUpdate();


							// for fifth
							if(x2>0) {
								pstmt=con.prepareStatement("Insert into transferstatus values(?,?,?,?,?,?,?,?)");

								pstmt.setInt(1, cust_id);
								pstmt.setString(2,bank_name);
								pstmt.setString(3, ifsc);
								pstmt.setInt(4,accno);
								pstmt.setString(5,receiver_ifsc);
								pstmt.setInt(6, accno);
								pstmt.setInt(7, amount);
								pstmt.setInt(8, transid);
								int x3=pstmt.executeUpdate();
								//for sixth
								if(x3>0) {
									resp.sendRedirect("/BankingApplication/Transfersuccess.html");
								}
								else {
									session.setAttribute("trans_error","Insertation Failed");
									resp.sendRedirect("/BankingApplication/TransferFail.jsp");
								}


							}
							else {
								session.setAttribute("trans_error","Receiver cannot receive the amount");
								resp.sendRedirect("/BankingApplication/TransferFail.jsp");
							}
						}
						else {
							session.setAttribute("trans_error","Balance cannot be duducted");
							resp.sendRedirect("/BankingApplication/TransferFail.jsp");
						}

					}
					else {
						session.setAttribute("trans_error","Insufficient Funds");
						resp.sendRedirect("/BankingApplication/TransferFail.jsp");
					}


				}
				else {
					session.setAttribute("trans_error","Incorrect Receiver details");
					resp.sendRedirect("/BankingApplication/TransferFail.jsp");
				}


			}
			else {
				session.setAttribute("trans_error","Incorrect Senders details");
				resp.sendRedirect("/BankingApplication/TransferFail.jsp");
			}


		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}

}
