package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) { // assim tenho o conn sempre disponivel no SellerDaoJDBC
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {
				Department dp = instantiateDepartment(rs);

				Seller se = instantiateSeller(rs, dp);

				return se;
			}
			return null;// nao existia seller com este id
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		// Como eu estou a tratar a exceção ante de chamar esta função eu uso o throws
		// para propagar a exceção

		Department dp = new Department();

		dp.setId(rs.getInt("DepartmentId"));
		dp.setName(rs.getString("DepName"));
		return dp;
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller se = new Seller();

		se.setId(rs.getInt("Id"));
		se.setName(rs.getString("Name"));
		se.setEmail(rs.getString("Email"));
		se.setBirthDate(rs.getDate("BirthDate"));
		se.setBaseSalary(rs.getDouble("BaseSalary"));
		se.setDepartment(dep);

		return se;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> selist = new ArrayList<>();
			
			Map<Integer,Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) { //se der nulo é porque nao existe ainda
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				selist.add(instantiateSeller(rs, dep));

				
			}
			return selist;// nao existia seller com este id
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatment(st);
			DB.closeResultSet(rs);
		}

	}

}
