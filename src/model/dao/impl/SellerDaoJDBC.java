package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);// para retornar valor do obj
																						// criado

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());

			int rowsaffected = st.executeUpdate();

			if (rowsaffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);// para que o obj ja fique populado com o id do obj criado
				}
				DB.closeResultSet(rs);
			} // Se cair aqui é porque houve um erro na inserção entao podemos escrever
				// excepção
			else {
				throw new DbException("Unexpected Error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatment(st);

		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE seller " + 
					"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + 
					"WHERE Id = ?");
																					

			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatment(st);

		}

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

			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) { // se der nulo é porque nao existe ainda entao eu crio para que na proxima vez
									// ele va verificar ao map e veja que ja existe e entao não dará null na
									// pesquisa
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

	public List<Seller> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rs = st.executeQuery();

			List<Seller> selist = new ArrayList<>();

			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) { // se der nulo é porque nao existe ainda entao eu crio para que na proxima vez
									// ele va verificar ao map e veja que ja existe e entao não dará null na
									// pesquisa
									// se o dep existir entao aproveito ele, assim so teremos um objeto em memoria
									// com os mesmos dados
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
