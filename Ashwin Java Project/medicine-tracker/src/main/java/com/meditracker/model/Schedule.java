package com.meditracker.model;

public class Schedule {
    public enum TimeOfDay {
        MORNING,
        AFTERNOON,
        EVENING
    }

    public enum MealTiming {
        BEFORE_MEAL,
        AFTER_MEAL
    }

    private int id;
    private int userId;
    private int medicineId;
    private TimeOfDay timeOfDay;
    private MealTiming mealTiming;
    private boolean isTaken;

    public Schedule() { }

    // For convenience
    public Schedule(int userId, int medicineId, TimeOfDay timeOfDay, MealTiming mealTiming) {
        this.userId = userId;
        this.medicineId = medicineId;
        this.timeOfDay = timeOfDay;
        this.mealTiming = mealTiming;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public TimeOfDay getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(TimeOfDay timeOfDay) { this.timeOfDay = timeOfDay; }

    public MealTiming getMealTiming() { return mealTiming; }
    public void setMealTiming(MealTiming mealTiming) { this.mealTiming = mealTiming; }

    public boolean isTaken() { return isTaken; }
    public void setTaken(boolean taken) { isTaken = taken; }
}
