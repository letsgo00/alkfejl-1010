package hu.alkfejl.dao;

import hu.alkfejl.model.Score;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScoreDAOImpl implements ScoreDAO {

//    private static ScoreDAOImpl instance = new ScoreDAOImpl();
//
//    public static ScoreDAOImpl getInstance() {
//        return instance;
//    }

    private Properties props = new Properties();
    private static String connectionUrl;
    public ScoreDAOImpl() {
        try {
            props.load(getClass().getResourceAsStream("/application.properties"));
            connectionUrl = props.getProperty("db.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Score> getTop10() {
        List<Score> result = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC"); //Hogy ne legyen driver not found error webnél
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection c = DriverManager.getConnection(connectionUrl);
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM SCORE ORDER BY score DESC LIMIT 10");
        ){
            int position = 1;
            while (rs.next()){
                Score score = new Score();
                score.setId(rs.getInt("id"));
                score.setName(rs.getString("name"));
                score.setScore(rs.getInt("score"));
                score.setPosition(position++);
                Date date = Date.valueOf(rs.getString("date"));
                score.setDate(date == null ? LocalDate.now() : date.toLocalDate());
                result.add(score);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public Score save(Score score) {
        try (Connection c = DriverManager.getConnection(connectionUrl);
             PreparedStatement stmt = c.prepareStatement("INSERT INTO SCORE (name, score, date) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setString(1 ,score.getName());
            stmt.setInt(2 ,score.getScore());
            stmt.setString(3,score.getDate().toString());
            stmt.executeUpdate();
            ResultSet gen = stmt.getGeneratedKeys();
            if(gen.next()) score.setId(gen.getInt(1));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return score;
    }
}
