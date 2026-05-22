package com.dinopark.enums;

public enum ExperienceType
{
    BASIC(1, 3, 50),
    PREMIUM(2, 4, 100),
    VIP(3, 5, 200);

    private final int minScore;
    private final int maxScore;
    private final double price;

    ExperienceType(int minScore, int maxScore, double price)
    {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.price = price;
    }

    public int generateScore(java.util.Random random)
    {
        return random.nextInt(maxScore - minScore + 1) + minScore;
    }

    public double getPrice()
    {
        return price;
    }
}
