package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	//tera opera��es estaticas para implementar os DAOS
	
	public static SellerDao createSellerDao() {//vai expor um metodo que retorna o tipo de interface mas internamente ela vai instanciar uma implementa��o
		return new SellerDaoJDBC(DB.getConnection()); //� uma maneira de nao expor a implementa��o, deixar somente a interface
	}

}
