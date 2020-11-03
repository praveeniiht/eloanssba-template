package com.iiht.evaluation.eloan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;

import com.iiht.evaluation.eloan.dto.LoanDto;
import com.iiht.evaluation.eloan.model.ApprovedLoan;
import com.iiht.evaluation.eloan.model.LoanInfo;
import com.iiht.evaluation.eloan.model.User;

public class ConnectionDao {
	private static final long serialVersionUID = 1L;
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public ConnectionDao(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

	public  Connection connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
		return jdbcConnection;
	}

	public void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	public int registerLoan(LoanInfo loaninfo) throws SQLException {
		Connection conn = connect();
		String sql = "insert into loaninfo(applno,purpose,amtrequest,doa,bstructure,bindicator,address,email,mobile) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, loaninfo.getApplno());
		pst.setString(2, loaninfo.getPurpose());
		pst.setInt(3, loaninfo.getAmtrequest());
		pst.setString(4, loaninfo.getDoa());
		pst.setString(5, loaninfo.getBstructure());
		pst.setString(6, loaninfo.getBindicator());
		pst.setString(7, loaninfo.getAddress());
		pst.setString(8, loaninfo.getEmail());
		pst.setString(9, loaninfo.getMobile());
		int k = pst.executeUpdate();
	
		return k;
	}
	
	public String validateUser(User user) throws SQLException{
		System.out.println("inside the validate of conn dao");
		String displayPage=null;
		Connection connection = connect();
		System.out.println("after the connection");
		boolean isUser = false;
		try {
			
			String sql = "select * from user";
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()) {
				String tid = rs.getString(1);
				String tpwd = rs.getString(2);
				if(tid.equals(user.getUsername()) && tpwd.equals(user.getPassword())) {
					isUser=true;
					break;
				}
			}
			if(isUser) {
				if(user.getUsername().equals("admin") ) {
					System.out.println("adminpage selected");
					displayPage="adminhome1.jsp";
			}
				else {
					System.out.println("userpage selected");
					displayPage="userhome1.jsp";
				}
			}
			else {
				System.out.println("indexpage selected");
				displayPage="index.jsp";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return displayPage;
	}
	
	public int createUser(User user) throws SQLException {
		Connection connection = connect();
		System.out.println("Inside the dao ");
		String sql = "insert into user values (?,?)";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, user.getUsername());
		pst.setString(2, user.getPassword());
		int status= pst.executeUpdate();
		
		return status;
	}
	
	public LoanDto calculateEmi(String applno) throws SQLException {
		Connection connection = connect();
		String sql = "select applno, amtrequest from loaninfo where applno=?";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, applno);
		ResultSet rs = pst.executeQuery();
		rs.next();
		int emi= 4500;
		LoanDto loandto = new LoanDto(rs.getString(1), rs.getInt(1),emi);
		return loandto;
		
	}
	
	public List<LoanInfo> displayAllLoans() throws SQLException{
		Connection connection = connect();
		List<LoanInfo> loans = new ArrayList<LoanInfo>();
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select * from loaninfo");
		while(rs.next()) {
			String applno = rs.getString(1);
			 String purpose = rs.getString(2);
			 int amtrequest = rs.getInt(3);
			 String doa= rs.getString(4);
			 String bstructure=rs.getString(5);
			 String bindicator=rs.getString(6);
			 String address=rs.getString(7);
			 String email=rs.getString(8);
			 String mobile=rs.getString(9);
			 String status = rs.getString(10);
			LoanInfo loan = new LoanInfo(applno,purpose,amtrequest,doa,bstructure,
					bindicator,address,email,mobile,status);
			loans.add(loan);
		
		}
		return loans;

	}
	
	public void updateLoanStatus(ApprovedLoan apLoan) throws SQLException {
		
		Connection connection = connect();
		String sql = "insert into approvedloans values(?,?,?,?,?,?)";
		String sql1 = "update loaninfo set status='processed' where applno=?";
		PreparedStatement pst = connection.prepareStatement(sql);
		PreparedStatement pst1 = connection.prepareStatement(sql1);
		pst1.setString(1, apLoan.getApplno());
		pst.setString(1,apLoan.getApplno());
		pst.setInt(2, apLoan.getAmotsanctioned());
		pst.setInt(3, apLoan.getLoanterm());
		pst.setString(4, apLoan.getPsd());
		pst.setString(5, apLoan.getLcd());
		pst.setInt(6, apLoan.getEmi());
		pst.executeUpdate();
		pst1.executeUpdate();
	}
	
	public LoanInfo editLoan(String applno) throws SQLException {
		Connection connection = connect();
		System.out.println("inside the editloan dao");
		String sql = "select * from loaninfo where applno=?";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1,applno);
		ResultSet rs = pst.executeQuery();
		LoanInfo loanInfo = new LoanInfo();
		if(rs!=null) {
		rs.next();
		
		loanInfo.setApplno(rs.getString(1));
		loanInfo.setPurpose(rs.getString(2));
		loanInfo.setAmtrequest(rs.getInt(3));
		loanInfo.setDoa(rs.getString(4));
		loanInfo.setBstructure(rs.getString(5));
		loanInfo.setBindicator(rs.getString(6));
		loanInfo.setAddress(rs.getString(7));
		loanInfo.setEmail(rs.getString(8));
		loanInfo.setMobile(rs.getString(9));
		loanInfo.setStatus(rs.getString(10));
		}
		else
		{
			loanInfo.setApplno("10050");
			loanInfo.setPurpose("Renovation");
			loanInfo.setAmtrequest(150000);
			loanInfo.setDoa("Oct 2020");
			loanInfo.setBstructure("Organization");
			loanInfo.setBindicator("Non Salaried");
			loanInfo.setAddress("Hyderabad");
			loanInfo.setEmail("gs@gmail.com");
			loanInfo.setMobile("9948121217");
			loanInfo.setStatus("processing");
		}
		System.out.println("last line of editloan dao");
		return loanInfo;
	}
	
	
}
