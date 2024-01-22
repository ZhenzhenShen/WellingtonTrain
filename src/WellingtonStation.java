import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.AclEntryFlag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.SSLContext;

import ecs100.UI;

public class WellingtonStation {

	
	private HashMap<String, Station> stations = new HashMap<String, Station>();
	private HashMap<String, TrainLine> trainLines = new HashMap<String, TrainLine>();
	public WellingtonStation() {
		UI.initialise();
		UI.addButton("List all stations", this::listAllStations);
		UI.addButton("List all train lines", this::listAllTrainLines);
		UI.addButton("Search a station", this::searchAStation);
		UI.addButton("Search a train line",this::searchATrainLine);
		UI.addButton("Start - destination",this::whereToStations);
		UI.addButton("Coming Train Lines for A station", this::comingLinesForAStation);
		readData();
		
	}
	
	public void comingLinesForAStation() {
		UI.println("Enter a station and specified time to find out coming train lines");
		String whichStation = UI.askString("Enter a station: ");
		int whatTime = UI.askInt("Enter a specified  int time: ");
		
		for(TrainLine trln : stations.get(whichStation).getTrainLines()) {
			int index = trln.getStations().indexOf(stations.get(whichStation));
			for(TrainService trnService : trln.getTrainServices()) {
				if(trnService.getTimes().get(index) > whatTime) {
					UI.println("Train Line: " + trln.getName()+ "Arriving Time: " + trnService.getTimes().get(index));
				break;
				}		
			}		
		}	
	}
	
	public void whereToStations() {
		
		String station1 = UI.askString("Where to start: ");
		String station2 = UI.askString("Where to go");
		boolean flag = true;
		for(TrainLine trline : trainLines.values()) {
			List<Station> sts = trline.getStations();
			if((sts.contains(stations.get(station1)))&&(sts.contains(stations.get(station2)))&&
				(sts.indexOf(stations.get(station1)) < sts.indexOf(stations.get(station2)))) {
				UI.println(trline.getName());	
				flag = false;
			}
		}
		if(flag) {
			UI.println("Sorry, there are no train line connecting the two stations directly");
		}
	
	}
	
	public void listAllStations() {
		for(String station : stations.keySet()) {
			UI.println(station + "\t");
		}
	}
	public void listAllTrainLines() {
		for(String trainLine : trainLines.keySet()) {
			UI.println(trainLine);
		}
	}
	public void searchAStation() {
		String whichStation = UI.askString("Enter a station to find out "
				+ "all the train lines that go through:");
		//if I use containsKey, it is hard to use the ignoreCase.
		boolean flag = true;
		for(String stn : stations.keySet() ) {			
			if(stn.equalsIgnoreCase(whichStation)) {
				UI.println(stations.get(stn).getTrainLines());	
				flag = false;
			}		   
		}
		// the flag is used to judge whether the station exists.
		if(flag) {
			UI.println("Station does not exist, please re-enter.");
		}
	}
	public void searchATrainLine() {
		UI.println("Here are the train lines: \nJohnsonville_Wellington\r\n"
				+ "Wellington_Johnsonville\r\n"
				+ "Melling_Wellington\r\n"
				+ "Wellington_Melling\r\n"
				+ "Waikanae_Wellington\r\n"
				+ "Wellington_Waikanae\r\n"
				+ "Masterton_Wellington\r\n"
				+ "Wellington_Masterton\r\n"
				+ "Upper-Hutt_Wellington\r\n"
				+ "Wellington_Upper-Hutt");
		String whichTrline = UI.askString("Enter a train line to find the stations:");
		boolean flag = true;
		for(String trLine : trainLines.keySet() ) {			
			if(trLine.equalsIgnoreCase(whichTrline)) {
				UI.println(trainLines.get(trLine).getStations());	
				flag = false;
			}		   
		}		
		if(flag) {
			UI.println("Train line does not exist, please re-enter.");
		}
		
	}
	
	public void testLog() {
        System.out.println("| ========= Test Log =========\n");
        for (Station i : stations.values()) {
            System.out.println("Station: " + i);
            Set<TrainLine> lines = i.getTrainLines();
            for (TrainLine j : lines) {
                System.out.println("| --- the line: " + j);
            }
            System.out.println("| ------------------------------------------------------------------------\n");
        }
        System.out.println("\n| ========= Test end =========\n");
    }
	
	public void testLog1() {
        System.out.println("| ========= Test Log =========\n");
        for (Station s : stations.values()) {
            System.out.println("Station: " + s + "\n|");
            Set<TrainLine> lines = s.getTrainLines();
            for (TrainLine l : lines) {
                System.out.println("| --- the line: " + l);
                List <Station> allSOfLine = l.getStations();
                System.out.println("| ");
                for (int k = 0; k < allSOfLine.size(); k++) {
                    System.out.println("| --- --- --- --- station " + (k + 1) + ": " + allSOfLine.get(k).getName());
                }
                System.out.println("| ");
            }
            System.out.println("| ------------------------------------------------------------------------\n");
        }
        System.out.println("\n| ========= Test end =========\n");
    }
	
	
	
	public void readData()  {
		
		
		try {
			Scanner scan = new Scanner(new File("./Train network data/stations.data"));
			 
			while(scan.hasNextLine()) {
				String s = scan.nextLine();
				Scanner sc = new Scanner(s);
				sc.useDelimiter(" ");
				String name = sc.next();
				int zone = sc.nextInt();
				double distance = sc.nextDouble();
				Station station = new Station(name, zone, distance);
				stations.put(name, station);
				sc.close();
			}
			scan.close();
			} catch (IOException e) {
			 UI.println("A file-reading error: " + e);
			} 
		
		try {
			Scanner scan = new Scanner(new File("./Train network data/train-lines.data"));
				 
			while(scan.hasNextLine()) {
				String name = scan.nextLine();
				TrainLine trainLine = new TrainLine(name);
				trainLines.put(name, trainLine);
			}
			scan.close();
			} catch (IOException e) {
			 UI.println("A file-reading error: " + e);
			} 

		for(String  trln : trainLines.keySet()) {
			String fileName = "./Train network data/"+ trln + "-stations.data";
			File file = new File(fileName);
			if (file.exists()) {
				try {
					 Scanner scan = new Scanner(file);
				     while(scan.hasNextLine()) {
				    	 String station = scan.nextLine();				    	 
				    	 trainLines.get(trln).addStation(stations.get(station));
				    	 stations.get(station).addTrainLine(trainLines.get(trln));
				     }
					 scan.close();
					} catch (IOException e) {
					 UI.println("A file-reading error: " + e);
					} 				
			} else {
				System.out.println("stations.data of :"+fileName+ "not found." );
			}			
		}
		
		for(String  s : trainLines.keySet()) {
			String fileName = "./Train network data/"+ s + "-services.data";
			File file = new File(fileName);
			if (file.exists()) {
				try {
					 Scanner scan = new Scanner(file);
				     while(scan.hasNextLine()) {
				    	
				    	 Scanner sc = new Scanner(scan.nextLine()) ;			    	 
				    	 
				    	 TrainService trainService = new TrainService(trainLines.get(s));
				    	 
				    	 int timeStart = sc.nextInt();	
				    	 trainService.addTime(timeStart, true);
				    	 
				    	 while(sc.hasNextInt()) {
				    		 int time = sc.nextInt();
				    		 trainService.addTime(time, false);
				    	 }	
				    
				    	 sc.close();

				    	 trainLines.get(s).addTrainService(trainService);
				
				     }		

				     
					 scan.close();
					} catch (IOException e) {
					 UI.println("A file-reading error: " + e);
					} 				
			} else {
				System.out.println("services.data of :"+fileName+ "not found." );
			}	
			
		}
		

		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new WellingtonStation();
	}

}
