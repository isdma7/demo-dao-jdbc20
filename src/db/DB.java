package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	// Connection é do JDBC
	private static Connection conn = null;

	// metodo para conectar ao BD
	public static Connection getConnection() {
		if (conn == null) {

			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");// dburl é o nome que definimos no db.properties

				// classe do BD do JDBC
				conn = DriverManager.getConnection(url, props); // conector com o bd é instanciar um objeto do tipo
																// conection, dá erro porque avisa que pode dar
																// exception entao fazemos um try para apanhar a
																// excepçºao
			} catch (SQLException e) {
				// se apanhar lanço minha excepção personalizada
				throw new DbException(e.getMessage());
			}
		}
		return conn;
	}

	// Metodo para fechar a coneção com o bd
	public static void closeConnection() {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}

	}

	// abrimos lemos os dados e guardamos num objeto tipo Properties
	private static Properties loadProperties() {// abrimos o arquivo com as propriedades proprias de concecção ao bd
												// mysql e que adicionamos na biblioteca adicionada manualmente por nos
												// MySQLCOnnector
		try (FileInputStream fs = new FileInputStream("db.properties")) {

			Properties props = new Properties();
			props.load(fs); // faz leitor do aquivo properties apontado pelo meu inputstream e guarda os
							// dados dentro do objeto props

			return props;

		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}

	}
	
	public static void closeStatment(Statement st) {
		if(st != null) {//se estiver instaciado
		try{
			st.close();
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		}
		
		
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {//se estiver instaciado
		try{
			rs.close();
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		}
		
		

		
	}

}
