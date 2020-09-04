package application;

import java.time.Instant;
import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Department obj = new Department(1, "Books");
	
		System.out.println(obj);
		
		Seller seller = new Seller(1, "Pedro", "pedro@gmail.com", new Date(), 1000.0, obj);
		
		System.out.println(seller);
		
		SellerDao sellerdao = DaoFactory.createSellerDao();
		//nao faço um new SellerDaoJDBC eu simplesmente chamo a fabrica, dessa forma o meu program nao conhece a implementação, apenas conhece a interface
		//maneira tambem de injetar dependencia sem explicitar a implementação
		
		System.out.println("===TEST 1: Seller FindById===");
		Seller seller2 = sellerdao.findById(3);
		
		System.out.println(seller2);
	
	}

}
