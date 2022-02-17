package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author Selena Li
 * @version 16 Feb 2022
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<String, SoccerPlayer>();
    SoccerPlayer newPlayer;

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        if (database.containsKey(firstName + " " + lastName)) {
            return false;
        }
        else {
            newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(firstName + " " + lastName, newPlayer);
            return true;
        }

    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        if (database.containsKey(firstName + " " + lastName)) {
            database.remove(firstName + " " + lastName);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        if (database.containsKey(firstName + " " + lastName)) {
            return database.get(firstName + " " + lastName);
        }
        else {
            return null;
        }
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        if(database.containsKey(firstName + " " + lastName)) {
            database.get(firstName + " " + lastName).bumpGoals();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        return false;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        return false;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        int returnValue = 0;

        if (teamName == null) {
            return database.size();
        } else {
            int players = 0;
            for (Map.Entry<String, SoccerPlayer> entry : database.entrySet()) {
                if (entry.getValue().getTeamName().equals(teamName)) {
                    returnValue++;
                }
            }
        }
        return returnValue;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        int iterator = 0;
        if(teamName != null) {
            for (Map.Entry<String, SoccerPlayer> entry : database.entrySet()) {
                if(entry.getValue().getTeamName().equals(teamName)) {
                    if (iterator == idx) {
                        return entry.getValue();
                    }
                    iterator++;
                }
            }
            return null;
        }

        else {
            for(Map.Entry<String,SoccerPlayer> entry: database.entrySet()) {
                if(iterator == idx) {
                    return entry.getValue();
                }
                iterator++;
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        try {
            Scanner inputStream = new Scanner(file);

            while (inputStream.hasNextLine()) {
                String firstName = inputStream.nextLine();
                String lastName = inputStream.nextLine();
                String teamName = inputStream.nextLine();
                String uniform = inputStream.nextLine();
                String goals = inputStream.nextLine();
                String yellowCard = inputStream.nextLine();
                String redCard = inputStream.nextLine();

                int uniformNum = Integer.parseInt(uniform);
                int goalsNum = Integer.parseInt(goals);
                int yellowCardNum = Integer.parseInt(yellowCard);
                int redCardNum = Integer.parseInt(redCard);

                SoccerPlayer player = new SoccerPlayer(firstName,lastName,uniformNum,teamName);
                String name = firstName + "##" + lastName;

            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {
        String fileName = file.toString();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            for(Map.Entry<String,SoccerPlayer> entry: database.entrySet()) {
                String info = "\n" + entry.getValue().getFirstName() + "\n" + entry.getValue().getLastName() +
                        "\n" + entry.getValue().getTeamName() + "\n" + entry.getValue().getUniform() + "\n" + entry.getValue().getGoals() +
                        "\n"+ entry.getValue().getYellowCards() + "\n" + entry.getValue().getRedCards();
                System.out.println(logString(info));
            }
            outputStream.close();
            return true;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        //Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        HashSet teamNames = new HashSet<String>();
        for(Map.Entry<String,SoccerPlayer> entry: database.entrySet()) {
            teamNames.add(entry.getValue().getTeamName());
        }
        return teamNames;
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
