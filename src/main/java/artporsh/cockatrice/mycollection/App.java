package artporsh.cockatrice.mycollection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import artporsh.cockatrice.mycollection.schema.CockatriceCarddatabase;
import artporsh.cockatrice.mycollection.schema.CockatriceCarddatabase.Cards.Card;

/**
 * Hello world!
 *
 */
public class App 
{
	
	long starttime;
	
    public static void main( String[] args ) throws JAXBException, IOException
    {
    	App app = new App();
    	CockatriceCarddatabase db = app.unmarshall();
    	System.out.println(String.format("card db loaded[%s]",db));
    	
    	List<String> myCards = app.readCardNamesFromFile();
//    	db.getCards().getCard().parallelStream().filter(c -> myCards.contains(c.getName())).forEach(mc -> System.out.println("mycard::"+mc.getName()));
    	List<Card> mycards = db.getCards().getCard().parallelStream().filter(c -> myCards.contains(c.getName())).collect(Collectors.toList());
    	db.getCards().getCard().clear();
    	db.getCards().getCard().addAll(mycards);
    	
    	app.marshal(db);
    }
    

	public CockatriceCarddatabase unmarshall() throws JAXBException, IOException {
    	this.startTime();
        JAXBContext context = JAXBContext.newInstance(CockatriceCarddatabase.class);
        CockatriceCarddatabase db =  (CockatriceCarddatabase) context.createUnmarshaller()
        		.unmarshal(new FileReader("cards.xml"));
        this.stopTime();
        return db;
    }
	
	private void marshal(CockatriceCarddatabase db) throws JAXBException {
	    JAXBContext context = JAXBContext.newInstance(CockatriceCarddatabase.class);
	    Marshaller mar= context.createMarshaller();
	    mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    mar.marshal(db, new File("mycards.xml"));
	}
    
    public List<String> readCardNamesFromFile() throws IOException{
    	System.out.println("My Cards ---------------------------------------------");
    	System.out.println(Paths.get("my-cards.txt").toAbsolutePath());
    	return Files.readAllLines(Paths.get("my-cards.txt"));
    }
    
    public void startTime() {
    	starttime = System.currentTimeMillis();
    	System.out.println("Started Loading DB");
    }
    
    public void stopTime() {
    	System.out.println("Finished Loafing DB, time taken: " + ((System.currentTimeMillis() - starttime)/1000));
    }
}
