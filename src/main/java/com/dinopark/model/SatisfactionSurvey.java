package com.dinopark.model;

public final class SatisfactionSurvey
{
    private final int touristId;
    private final String enclosureName;
    private final int score;

    public SatisfactionSurvey(int touristId, String enclosureName, int score)
    {
        if (score < 1 || score > 5)
        {
            throw new IllegalArgumentException("Score must be between 1 and 5, got: " + score);
        }

        this.touristId = touristId;
        this.enclosureName = enclosureName;
        this.score = score;
    }

    //*Getters
    public int getTouristId()
    {
        return touristId;
    }

    public String getEnclosureName()
    {
        return enclosureName;
    }

    public int getScore()
    {
        return score;
    }

    @Override
    public String toString()
    {
        return "SatisfactionSurvey{" +
                "touristId=" + touristId +
                ", enclosureName='" + enclosureName + '\'' +
                ", score=" + score +
                '}';
    }
}
