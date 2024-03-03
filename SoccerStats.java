/**
Date: 11/27/2023
Course: CSCI 2073
Description: This program reads informations from a file iterating each line and then stores, updates and modify the goal, points and other instance 
later to determine the win or loss by a team. This program holds all the stats of a team with ability to modify at any time. It returns the bestTeam teamm and the high scoring team as well.


On my honor, I have neither given nor received unauthorized help while
completing this assignment.

Name and CWID.         Diwakar Mahato Sudi    30155595

*/




import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



/**
This class represents the teamStatistics of the team 
it contains constructers for teamStatistics 

it has all the information about the team performance 

Initialization for new instance variables are made for future update to have record of win, loss or drawa in the game 


*/
class statsOfTeam {
    public int wins;
    public int draws;
    public int losses;
    public int goalsFor;
    public int goalsAgainst;

    public statsOfTeam() {
        wins = 0;
        draws = 0;
        losses = 0;
        goalsFor = 0;
        goalsAgainst = 0;
    }

  /**
  This method calculates the points team has 
  
  @returns   it returns the total points collected by a team based on wins and draws they had
  */   
    public int getPoints() {
        return wins * 3 + draws;
    }
    
    
    
    
  /**
  This methods calculates the goal difference
  
  @return   it returns the goal difference as an integer value
  */  
    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }
    
    
    

/**
This methods updates the team goals
it updates the wins, losses and drawa based on the goalFor and goalAgainst

@param acheived    The total number of goals acheived  by the team
@param surrended  The total number of goals allowed by the team to the opposing team 

*/    
    public void upgradeStats(int acheived , int surrended) {
        goalsFor += acheived ;
        goalsAgainst += surrended;
        if (acheived  > surrended) {
            wins++;
        } else if (acheived  == surrended) {
            draws++;
        } else {
            losses++;
        }
    }


   
   
/**
This method returns a string that includes the team's wins (W), draws (D), 
 losses (L), goals for (GF), goals surrended (GA), and total points (PTS).
 
 @returns  It returns a String with all the details win of a winning team

*/    
    @Override
    public String toString() {
        return "W: " + wins + " D: " + draws + " L: " + losses + " GF: " + goalsFor + " GA: " + goalsAgainst + " PTS: " + getPoints();
    }
}




/**
This class reads soccergame data from the csv file and holds the track of stats.
it stores all the data in the map and keep track on the map


*/
public class SoccerStats {
    private Map<String, statsOfTeam> mapStats = new HashMap<>();

/**
@param csvLocation  it contains the path to the csv file to read all the information from
*/
    public SoccerStats(String csvLocation) {
        gameData(csvLocation);
    }

/**
This method reads data from csv file and updates team statistics for every game 

@param csvLocation  it contains the path to the csv file(File having all the game data) to read all the information from
*/

   private void gameData(String csvLocation) {
    try (Scanner scan = new Scanner(new File(csvLocation))) {
        scan.useDelimiter(",");

        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            Scanner lineScan = new Scanner(line);
            lineScan.useDelimiter(",");

            if (lineScan.hasNext()) {
                String homeTeam = lineScan.next();
                if (lineScan.hasNext()) {
                    String awayTeam = lineScan.next();
                    if (lineScan.hasNextInt()) {
                        int homeGoals = lineScan.nextInt();
                        if (lineScan.hasNextInt()) {
                            int awayGoals = lineScan.nextInt();
                            modifyTeamStats(homeTeam, awayTeam, homeGoals, awayGoals);
                        }
                    }
                }
            } else {
                System.out.println("Invalid format of file.... try with other file");
            }
            lineScan.close();
        }
    } catch (FileNotFoundException e) {
        System.out.println("File not found. Please try again with another file.......");
    }
}
    
    
    
    
/**
This methods modifys the statistics for the team both home team and the away team 
based on the result of a single game held 

@param home  The name of home team 

@param   The name of the opposing team 

@param homeGoals   The goals acheived by the home team

@param awayGoals    The number of goals scored by the opposig team 
*/   
    private void modifyTeamStats(String home, String away, int homeGoals, int awayGoals) {
    statsOfTeam homeTeamStats = mapStats.get(home);
    if (homeTeamStats == null) {
        homeTeamStats = new statsOfTeam();
        mapStats.put(home, homeTeamStats);
    }
    homeTeamStats.upgradeStats(homeGoals, awayGoals);

    statsOfTeam awayTeamStats = mapStats.get(away);
    if (awayTeamStats == null) {
        awayTeamStats = new statsOfTeam();
        mapStats.put(away, awayTeamStats);
    }
    awayTeamStats.upgradeStats(awayGoals, homeGoals);
    }
    
    
    
    
    
/**
This method reads the file at the specified location, processes it and then returns it as a String 
If line starts with Stats it processes it with processStats method
Lines with BEST or HSCORING call methods for specific team Statistics  

@param statsLocation  The location of the file containing the statistics.   

@ return  it returns the related stats, as in the form of String. Each statistics is seperated by a new line.

@throws FileNotFoundException  if the file is not found at the specified location
*/
    public String getStats(String statsLocation) {
    String result = "";
    File file = new File(statsLocation);

    try (Scanner scan = new Scanner(file)) {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.startsWith("STATS")) {
                int startingIndex = 5; 
                
                while (startingIndex < line.length() && line.charAt(startingIndex) == ' ') {
                    startingIndex++;
                }
                if (startingIndex < line.length()) {
                    result += processStats(line.substring(startingIndex)) + "\n";
                }
            } else if ("BEST".equals(line)) {
                result += bestTeamTeam() + "\n\n";
            } else if ("HSCORING".equals(line)) {
                result += highScoreHolder() + "\n\n";
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Your File is not found Please upload correct File ");
        return "";
    }

    return result;
}




/**
This method process the stats for the given team 
It uses team name to look upon the statistics and then return the specified results.
If the team is not found it indicates it as team not found.
If the team is found team with all of its record as its stats is returned

@param teamName    The designated name of team to find the statistics for 

@return   returns A string with the team details or not found is team is not found 

*/

    private String processStats(String teamName) {
    statsOfTeam stats = mapStats.get(teamName);
    
    if (stats != null) {
        return "TEAM: " + teamName + " " + stats.toString();
    } else {
        return "TEAM: " + teamName + " NOT FOUND";
    }
}
    
    
    
    
    
/**
This method iterates over map and finds the bestTeam team according to the goal difference ,
highest points and goal for 


@return    A string containing the name of the bestTeam team with all of its stats
            returns BEST NONE if bestTeam team is not found

*/

private String bestTeamTeam() {
    String bestTeamTeamName = "BEST: NONE";
    statsOfTeam statsOfBestTeam = null;

    for (String teamName : mapStats.keySet()) {
        statsOfTeam statsOfCurrentTeam = mapStats.get(teamName);

        if (statsOfBestTeam == null || isBetterThan(statsOfCurrentTeam, statsOfBestTeam)) {
            statsOfBestTeam = statsOfCurrentTeam;
            bestTeamTeamName = "BEST: " + teamName + " " + statsOfCurrentTeam.toString();
        }
    }

    return bestTeamTeamName;
}

/**
It compares two teams , it compares currentTeamteam with bestTeamteam found so far
comparision is made on the basis of no of points,goals and goalsdifference

@param currentTeam      currentTeam  statistics
@param bestTeam          bestTeam  statistics found so far

@returns    returns true if currentTeam team is better than bestTeamteam otherwise returns false
*/


private boolean isBetterThan(statsOfTeam currentTeam, statsOfTeam bestTeam) {
    if (currentTeam.getPoints() > bestTeam.getPoints()) {
        return true;
    } else if (currentTeam.getPoints() == bestTeam.getPoints()) {
        if (currentTeam.getGoalDifference() > bestTeam.getGoalDifference()) {
            return true;
        } else if (currentTeam.getGoalDifference() == bestTeam.getGoalDifference()) {
            return currentTeam.goalsFor > bestTeam.goalsFor;
        }
    }
    return false;
}

    
    
    
    
    
/**
This method identifies high scoring  team in the statistics based on goals , goal difference and points expect the bestTeam team

It finds the goals of bestTeam team and then list other team with high score rather than bestTeam team

@returns     it returns a String containg the high scoring team stastics 
             if team not found it returns HIGH SCORERS: NONE message

*/    

private String highScoreHolder() {
String bestTeam = bestTeamTeam();
    String bestTeamTeamName = bestTeam.substring(6);
    bestTeamTeamName = bestTeamTeamName.substring(0,bestTeamTeamName.indexOf(':')-2);
    
    int bestTeamTeamGoal;

    if (bestTeamTeamName.isEmpty()) {
        bestTeamTeamGoal = 0;
    } else {
        bestTeamTeamGoal = mapStats.get(bestTeamTeamName).goalsFor;
    }

    String highScorers = "";

    for (Map.Entry<String, statsOfTeam> entry : mapStats.entrySet()) {
        if (entry.getValue().goalsFor > bestTeamTeamGoal) {
            if (!highScorers.isEmpty()) {
                highScorers += ", ";
            }
            highScorers += entry.getKey();
        }
    }

    String result = "HIGH SCORERS: ";
    if (highScorers.length() > 0) {
        result = result + highScorers;
    } else {
        result = result + "NONE";
    }

    return result;
}




}