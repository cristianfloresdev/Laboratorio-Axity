package com.dinopark.model;

import java.time.LocalDateTime;

public final class Ticket
{
    private final long id;
    private final int  touristId;
    private final double price;
    private final String category;
    private final LocalDateTime issuedAt;

    public Ticket(long id, int touristId, double price, String category)
    {
        this.id = id;
        this.touristId = touristId;
        this.price = price;
        this.category = category;
        this.issuedAt = LocalDateTime.now();
    }

    //*Getters
    public long getId()
    {
        return id;
    }
    public int getTouristId()
    {
        return touristId;
    }
    public double getPrice()
    {
        return price;
    }
    public String getCategory()
    {
        return category;
    }
    public LocalDateTime getIssuedAt()
    {
        return issuedAt;
    }

    @Override
    public String toString()
    {
        return "Ticket{" +
                "id=" + id +
                ", touristId=" + touristId +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", issuedAt=" + issuedAt +
                '}';
    }
}
