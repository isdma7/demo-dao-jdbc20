package application;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);

		Department obj = new Department(1, "Books");
	
		System.out.println(obj);
		
		Seller seller = new Seller(1, "Pedro", "pedro@gmail.com", new Date(), 2000.0, obj);
		
		System.out.println(seller);
		
		SellerDao sellerdao = DaoFactory.createSellerDao();
		//nao faço um new SellerDaoJDBC eu simplesmente chamo a fabrica, dessa forma o meu program nao conhece a implementação, apenas conhece a interface
		//maneira tambem de injetar dependencia sem explicitar a implementação
		
		System.out.println("===TEST 1: Seller FindById===");
		Seller seller2 = sellerdao.findById(3);
		
		System.out.println(seller2);
		
		System.out.println("\n===TEST 2: Seller FindByDepartment===");
		Department dep = new Department(2, null);
		List<Seller> selist = sellerdao.findByDepartment(dep);
		
		for(Seller se : selist) {
			System.out.println(se + "\n");
		}
		
		System.out.println("\n===TEST 3: Seller FindAll===");
		selist = sellerdao.findAll(); //reaproveito variavel ja que dados serão novos
		
		for(Seller se : selist) {
			System.out.println(se + "\n");
		}
		
		System.out.println("\n===TEST 4: Seller Insert===");
		
		Seller newse = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, dep);
		
		sellerdao.insert(newse);
		
		System.out.println("Inserted! New Id= " + newse.getId());
		
		
		System.out.println("\n===TEST 5: Seller Update===");
		
		seller = sellerdao.findById(1);
		
		seller.setName("Marta");
		seller.setEmail("marta@gmail.com");
		sellerdao.update(seller);
		System.out.println("Updated Completed");
		
		System.out.println("\n===TEST 6: Seller Delete===");
		
		System.out.println("Enter id for delete test: ");
		
		int id = sc.nextInt();
		
		sellerdao.deleteById(id);
		
		System.out.println("Delete Completed with id= " + id);
		
		sc.close();
		
	}

}
