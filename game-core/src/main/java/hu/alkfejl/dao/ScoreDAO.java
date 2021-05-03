package hu.alkfejl.dao;

import hu.alkfejl.model.Score;

import java.util.List;

public interface ScoreDAO {
    List<Score> getTop10();

    Score save(Score score);

}
