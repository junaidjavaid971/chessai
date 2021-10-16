package app.com.chess.ai.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import app.com.chess.ai.interfaces.GamesDao;
import app.com.chess.ai.models.dbModels.Games;

@Database(entities = {Games.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GamesDao gameDao();
}