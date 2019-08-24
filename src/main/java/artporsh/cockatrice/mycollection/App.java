package artporsh.cockatrice.mycollection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import artporsh.cockatrice.mycollection.schema.CockatriceCarddatabase;
import artporsh.cockatrice.mycollection.schema.CockatriceCarddatabase.Cards.Card;

public class App {

	long starttime;
	
	CockatriceCarddatabase db;
	List<String> myCards;

	public static void main(String[] args) throws JAXBException, IOException {
		App app = new App();
		app.loadCockatriceDatabase();
		app.readMyCardsFromFile();
		app.logMyCardsWithIncorrectNames();
		app.createCockatriceDatabaseOfMyCards();
	}

	private void loadCockatriceDatabase() throws JAXBException, IOException {
		db = this.unmarshall();
	}

	public CockatriceCarddatabase unmarshall() throws JAXBException, IOException {
		this.startTime();
		JAXBContext context = JAXBContext.newInstance(CockatriceCarddatabase.class);
		CockatriceCarddatabase db = (CockatriceCarddatabase) context.createUnmarshaller()
				.unmarshal(new FileReader("cards.xml"));
		this.stopTime();
		return db;
	}

	public void readMyCardsFromFile() throws IOException {
		System.out.println("Loading my cards");
//		System.out.println(Paths.get("my-cards.txt").toAbsolutePath());
		myCards =  Files.readAllLines(Paths.get("my-cards.txt"));
	}

	private void logMyCardsWithIncorrectNames() {
		List<String> dbCardsUppercase = db.getCards().getCard().stream().map(e -> e.getName().toUpperCase()).collect(Collectors.toList());
		// cards from my list that are not present in Cockatrice DB - likely due to wrong spelling
		List<String> incorrectNamedCards = myCards.stream().filter(c -> !dbCardsUppercase.contains(c.toUpperCase().trim())).collect(Collectors.toList());
		
		if(incorrectNamedCards.isEmpty()) {
			System.out.println("All cards you specified are correct");
		} else {
			System.out.println("----------------------------------------------------");
			System.out.println("You specified cards with wrong names. Make sure they are present in Cockatrice's cards.xml");
			System.out.println();
			incorrectNamedCards.forEach(System.out::println);
			System.out.println();
			System.out.println("----------------------------------------------------");
		}
		
	}

	private void createCockatriceDatabaseOfMyCards() throws JAXBException {
		List<Card> myCardsFromDb = db.getCards().getCard().parallelStream().filter(c -> myCards.contains(c.getName()))
				.collect(Collectors.toList());
		
		db.getCards().getCard().clear();
		db.getCards().getCard().addAll(myCardsFromDb);
		
		this.marshal(db);
		System.out.println("Collection mycards.xml is created");
	}



	private void marshal(CockatriceCarddatabase db) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(CockatriceCarddatabase.class);
		Marshaller mar = context.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(db, new File("mycards.xml"));
	}


	public void startTime() {
		starttime = System.currentTimeMillis();
		System.out.println("Started Loading DB");
	}

	public void stopTime() {
		System.out.println("Finished Loafing DB, time taken: " + ((System.currentTimeMillis() - starttime) / 1000) + " seconds");
	}
}
