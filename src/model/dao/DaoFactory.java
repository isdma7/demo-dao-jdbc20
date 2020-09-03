package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	//tera operações estaticas para implementar os DAOS
	
	public static SellerDao createSellerDao() {//vai expor um metodo que retorna o tipo de interface mas internamente ela vai instanciar uma implementação
		return new SellerDaoJDBC(DB.getConnection()); //é uma maneira de nao expor a implementação, deixar somente a interface
	}

}
