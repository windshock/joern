package parsing;

import java.util.Iterator;
import java.util.List;
import java.util.Observer;


public class Parser
{
	ModuleParser parser = new ModuleParser();
	
	
	public void run(List<String> listOfFiles)
	{
		processListOfFiles(listOfFiles);
	}
	
	public void addObserver(Observer anObserver)
	{
		parser.addObserver(anObserver);
	}
	
	public void processListOfFiles(List<String> filenames)
	{
		parser.begin();
		
		Iterator<String> it = filenames.iterator();
		while(it.hasNext()){
			String filename = it.next();
			processSingleFile(filename);
		}
		
		parser.end();
	}
	
    private void processSingleFile(String filename)
    {
    	System.out.println(filename);
    	
    	try{
    		parser.parseAndWalkFile(filename);
    	}catch(ParserException ex){
    		System.err.println("Error processing file: " + filename);
    	}
    }
}
