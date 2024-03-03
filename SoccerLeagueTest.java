public class SoccerLeagueTest
/**
   Program to test the basic functionality of the LeagueStats class.
*/
{
   public static void main(String [] args)
	{
      SoccerStats round1 = new SoccerStats("games1-8.csv");
      String results = round1.getStats("trans.txt");
      System.out.println(results);
      System.out.println();
      SoccerStats round1_3 = new SoccerStats("games1-24.csv");
      results = round1_3.getStats("trans.txt");
      System.out.println(results);
	}
}