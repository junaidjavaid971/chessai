package app.com.chess.ai.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import app.com.chess.ai.models.dbModels.Games;

@Dao
public interface GamesDao {

    @Query("SELECT * FROM Games where eventDate = :eventDate")
    List<Games> getDateBasedGames(String eventDate);

    @Query("SELECT * FROM Games where tournamentName = :tournamentName")
    List<Games> getNameBasedGames(String tournamentName);

    @Query("SELECT * FROM Games")
    List<Games> getAllGames();

    @Insert
    void insert(Games Games);

    @Delete
    void delete(Games Games);

    @Query("UPDATE Games SET tournamentName = :name, eventDate = :date, pgn = :pgn, fenList = :fenList, positionsList = :positionList")
    void update(String name, String date, String pgn, String fenList, String positionList);
}