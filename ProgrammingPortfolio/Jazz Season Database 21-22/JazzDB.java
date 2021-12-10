import java.sql.*;
import java.util.*;
import java.io.IOException;
import java.nio.file.*;

/**
 * A simple Java application to display and add information about Utah Jazz games and high scores in the 2021-22 Season
 **/
public class JazzDB implements AutoCloseable {

   // Default connection information (most can be overridden with command-line arguments)
   private static final String DB_NAME = "oma0126_jazz";
   private static final String DB_USER = "token_1ebb";
   private static final String DB_PASSWORD = "F3QgpKAifOv659FK";

   // The query that will be executed
   private static final String GET_PS_COMPLETED_GAMES = "SELECT date, homeOrAway, jazzScore, opponentScore, opponentID FROM Game WHERE date < '2021-10-20' AND jazzScore IS NOT NULL AND opponentScore IS NOT NULL;";
   private static final String GET_RS_COMPLETED_GAMES = "SELECT date, homeOrAway, jazzScore, opponentScore, opponentID FROM Game WHERE date > '2021-10-13' AND jazzScore IS NOT NULL AND opponentScore IS NOT NULL;";
   private static final String GET_HIGH_SCORERS = "SELECT Game.date, Game.homeOrAway, Game.opponentID, Player.name, MostScored.playerPoints FROM Game INNER JOIN MostScored ON Game.date = MostScored.gameDate INNER JOIN Player ON MostScored.jerseyNum = Player.jerseyNum;";
   private static final String GET_UPCOMING_GAMES = "SELECT date, homeOrAway, Opponent.teamName FROM Game INNER JOIN Opponent ON Game.opponentID = Opponent.id WHERE jazzScore IS NULL AND opponentScore IS NULL;";
   private static final String ADD_POINTS = "UPDATE Game SET jazzScore = ?, opponentScore = ? WHERE date = ?;";
   private static final String GET_PLAYERS = "SELECT jerseyNum, name FROM Player;";
   private static final String ADD_HIGH_SCORER = "INSERT INTO MostScored (gameDate, jerseyNum, playerPoints) VALUES (?,?,?);";
   private static final String GET_ALL_GAMES = "SELECT date, homeOrAway, opponentID FROM Game;";
   
   // Connection information to use
   private final String dbHost;
   private final int dbPort;
   private final String dbName;
   private final String dbUser, dbPassword;

   // The database connection and prepared statement (query)
   private Connection connection;
   private PreparedStatement query1, query2, query3, query4, query5, query6, query7, query8;

   /**
    * Creates an {@code IMDbEpisodeQuery} with the specified connection information.
    * @param sshKeyfile the filename of the private key to use for ssh
    * @param dbName the name of the database to use
    * @param dbUser the username to use when connecting
    * @param dbPassword the password to use when connecting
    * @throws SQLException if unable to connect
    */
   public JazzDB(String dbHost, int dbPort, String dbName,
           String dbUser, String dbPassword) throws SQLException {
      this.dbHost = dbHost;
      this.dbPort = dbPort;
      this.dbName = dbName;
      this.dbUser = dbUser;
      this.dbPassword = dbPassword;
   
      connect();
   }

   private void connect() throws SQLException {
      // URL for connecting to the database: includes host, port, database name,
      // user, password
      final String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s",
             dbHost, dbPort, dbName,
             dbUser, dbPassword
         );
   
      // Attempt to connect, returning a Connection object if successful
      this.connection = DriverManager.getConnection(url);
   
      // Prepare the statement(s) (query) that we will execute
      this.query1 = this.connection.prepareStatement(GET_PS_COMPLETED_GAMES);
      this.query2 = this.connection.prepareStatement(GET_RS_COMPLETED_GAMES);
      this.query3 = this.connection.prepareStatement(GET_HIGH_SCORERS);
      this.query4 = this.connection.prepareStatement(GET_UPCOMING_GAMES);
      this.query5 = this.connection.prepareStatement(ADD_POINTS);
      this.query6 = this.connection.prepareStatement(GET_PLAYERS);
      this.query7 = this.connection.prepareStatement(ADD_HIGH_SCORER);
      this.query8 = this.connection.prepareStatement(GET_ALL_GAMES);
   }

   public void runApp() throws SQLException {
      Scanner in = new Scanner(System.in);
      System.out.println("Hello, welcome to the Utah Jazz 2021-22 Database! What would you like to do?");
      while (true) {
         System.out.print("\nMAIN MENU:\n"
            + "1: See completed games\n"
            + "2: Show highest scoring players per game\n"
            + "3: Add points to a game\n"
            + "4: Add high scorers to game(s)\n");
         System.out.print("Enter option (1, 2, 3, or 4), then press Enter (or hit Enter to quit): ");
         String line = in.nextLine();
         if (line.isBlank())
            break;
         else if (line.equals("1"))
            getGames();
         else if (line.equals("2"))
            getHighScorers();
         else if (line.equals("3"))
            addPoints();
         else if (line.equals("4"))
            addHighScorers();
      }
   }

   /**
    * @param name the name of the TV series
    * @throws SQLException if an error occurs while querying the database
    */ 
   private void getGames() throws SQLException {
      System.out.println("\nShowing completed games...");
      ResultSet psResults = query1.executeQuery();
      char gameResult;
      int gamesWon = 0;
      int gamesLost = 0;
      System.out.println("---PRESEASON---");
      while (psResults.next()){
         String date = psResults.getString("Game.date");
         String homeOrAway = psResults.getString("Game.homeOrAway");
         int jazzScore = psResults.getInt("Game.jazzScore");
         int opponentScore = psResults.getInt("Game.opponentScore");
         if (jazzScore > opponentScore)
            gameResult = 'W';
         else
            gameResult = 'L';
         String opponentID = psResults.getString("Game.opponentID");
         System.out.printf("%s %s\tUTA: %d\t%s: %d\t(%s)\n", date, homeOrAway, jazzScore, opponentID, opponentScore, gameResult);
      }
      
      ResultSet rsResults = query2.executeQuery();
      System.out.println("---REGULAR SEASON---");
      while (rsResults.next()){
         String date = rsResults.getString("Game.date");
         String homeOrAway = rsResults.getString("Game.homeOrAway");
         int jazzScore = rsResults.getInt("Game.jazzScore");
         int opponentScore = rsResults.getInt("Game.opponentScore");
         if (jazzScore > opponentScore) {
            gameResult = 'W';
            gamesWon++;
         }
         else {
            gameResult = 'L';
            gamesLost++;
         }
         String opponentID = rsResults.getString("Game.opponentID");
         System.out.printf("%s\t%s\tUTA: %d\t%s: %d\t(%s)\n", date, homeOrAway, jazzScore, opponentID, opponentScore, gameResult);
      }
      
      System.out.printf("\nCurrent regular season record: %d-%d\n", gamesWon, gamesLost);
   }
   
   private void getHighScorers() throws SQLException {
      System.out.println("\nGetting players' high scores...");
      ResultSet results = query3.executeQuery(); 
      char homeOrAwaySymbol;
      while (results.next()) {
         String date = results.getString("Game.date");
         String homeOrAway = results.getString("Game.homeOrAway");
         if (homeOrAway.equals("Home"))
            homeOrAwaySymbol = 'V';
         else
            homeOrAwaySymbol = '@';
         String opponentID = results.getString("Game.opponentID");
         String playerName = results.getString("Player.name");
         int playerPoints = results.getInt("MostScored.playerPoints");
         System.out.printf("%s\t%s\t%s\t%s scored %d points\n", date, homeOrAwaySymbol, opponentID, playerName, playerPoints);
      }
   }
   
   private void addPoints() throws SQLException {
      Scanner in = new Scanner(System.in);
      ResultSet upcomingGames = query4.executeQuery();
      int i = 0;
      while (upcomingGames.next()) {
         String date = upcomingGames.getString("Game.date");
         String homeOrAway = upcomingGames.getString("Game.homeOrAway");
         String opponent = upcomingGames.getString("Opponent.teamName");
         System.out.printf("%s (%s) against the %s\n", date, homeOrAway, opponent);
         i++;
      }
      if (i > 0) {
         System.out.print("Enter the date for the game you wish to add: ");
         String date = in.nextLine();
         System.out.print("What was the Jazz score?: ");
         int jazzScore = in.nextInt();
         System.out.print("What was the opponent's score?: ");
         int opponentScore = in.nextInt();
         System.out.println("Adding game scores...");
         query5.setInt(1, jazzScore);
         query5.setInt(2, opponentScore);
         query5.setString(3, date);
         query5.execute();
         System.out.println("Game scores added!");
         
      } else
         System.out.println("No games to add scores to.");
   }
   
   private void addHighScorers() throws SQLException {
      Scanner in = new Scanner(System.in);
      ResultSet allGames = query8.executeQuery();
      while (allGames.next()){
         String date = allGames.getString("Game.date");
         String homeOrAway = allGames.getString("Game.homeOrAway");
         String opponentID = allGames.getString("Game.opponentID");
         System.out.printf("%s\t%s\t%s\n", date, homeOrAway, opponentID);
      }
      System.out.print("Select date for game to add high scorers: ");
      String date = in.nextLine();
      ResultSet players = query6.executeQuery();
      while (players.next()){
         String jerseyNum = players.getString("Player.jerseyNum");
         String name = players.getString("Player.name");
         System.out.printf("%s\t%s\n", jerseyNum, name);
      }
      System.out.printf("Enter the player jersey number of the Jazz player who scored the most points: ");
      int mostPointsJersey = in.nextInt();
      System.out.print("What was the highest score from this Jazz player?: ");
      int mostPoints = in.nextInt();
      System.out.println("Adding Jazz high score and player...");
      query7.setString(1, date);
      query7.setInt(2, mostPointsJersey);
      query7.setInt(3, mostPoints);
      query7.execute();
      System.out.println("Highest points added!");
   }
   
   /**
    * Closes the connection to the database.
    */
   @Override
   public void close() throws SQLException {
      connection.close();
   }

   /**
    * Entry point of the application. Uses command-line parameters to override database
    * connection settings, then invokes runApp().
    */
   public static void main(String... args) {
      // Default connection parameters (can be overridden on command line)
      Map<String, String> params = new HashMap<>(Map.of(
         "dbname", "" + DB_NAME,
         "user", DB_USER,
         "password", DB_PASSWORD
         ));
   
      boolean printHelp = false;
   
      // Parse command-line arguments, overriding values in params
      for (int i = 0; i < args.length && !printHelp; ++i) {
         String arg = args[i];
         boolean isLast = (i + 1 == args.length);
      
         switch (arg) {
            case "-h":
            case "-help":
               printHelp = true;
               break;
         
            case "-dbname":
            case "-user":
            case "-password":
               if (isLast)
                  printHelp = true;
               else
                  params.put(arg.substring(1), args[++i]);
               break;
         
            default:
               System.err.println("Unrecognized option: " + arg);
               printHelp = true;
         }
      }
   
      // If help was requested, print it and exit
      if (printHelp) {
         printHelp();
         return;
      }
   
      // Connect to the database. This use of "try" ensures that the database connection
      // is closed, even if an exception occurs while running the app.
      try (DatabaseTunnel tunnel = new DatabaseTunnel();
          JazzDB app = new JazzDB(
             "localhost", tunnel.getForwardedPort(), params.get("dbname"),
             params.get("user"), params.get("password")
         )) {
         // Run the interactive mode of the application.
         app.runApp();
      } catch (IOException ex) {
         System.err.println("Error setting up ssh tunnel.");
         ex.printStackTrace();
      } catch (SQLException ex) {
         System.err.println("Error communicating with the database (see full message below).");
         ex.printStackTrace();
         System.err.println("\nParameters used to connect to the database:");
         System.err.printf("\tSSH keyfile: %s\n\tDatabase name: %s\n\tUser: %s\n\tPassword: %s\n\n",
                params.get("sshkeyfile"), params.get("dbname"),
                params.get("user"), params.get("password")
            );
         System.err.println("(Is the MySQL connector .jar in the CLASSPATH?)");
         System.err.println("(Are the username and password correct?)");
      }
      
   }

   private static void printHelp() {
      System.out.println("Accepted command-line arguments:");
      System.out.println();
      System.out.println("\t-help, -h          display this help text");
      System.out.println("\t-dbname <text>     override name of database to connect to");
      System.out.printf( "\t                   (default: %s)\n", DB_NAME);
      System.out.println("\t-user <text>       override database user");
      System.out.printf( "\t                   (default: %s)\n", DB_USER);
      System.out.println("\t-password <text>   override database password");
      System.out.println();
   }
}