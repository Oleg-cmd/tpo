package lab1.domain;

import java.util.ArrayList;
import java.util.List;

public class Arthur {
    private House targetHouse;
    private boolean noticing = true;
    private boolean focusedOnBulldozer;

    public void runTo(House house) {
        this.targetHouse = house;
        System.out.println("Arthur is running to his house.");
    }

    public void reactToWeather(Weather weather) {
        if (weather.isCold() || weather.isWindy() || weather.isRainy()) {
            System.out.println("Arthur is starting to notice the bad weather.");
            this.noticing = false; // Arthur начинает замечать погоду
        }
    }
    
     public void reactToHouseDestruction() {
        System.out.println("Arthur is shocked by the destruction of his house!");
        this.noticing = false;  // Arthur определенно замечает разрушение дома
    }
    
    public void reactToDestruction(House house, Bulldozer bulldozer) {
        if (house.isDestroyed() && bulldozer != null) {
            this.focusedOnBulldozer = true;
        }
    }

    public House getTargetHouse() {
        return targetHouse;
    }

    public void setNoticing(boolean noticing) {
        this.noticing = noticing;
    }

    public boolean isFocusedOnBulldozer() {
       return !noticing && targetHouse != null && targetHouse.isDestroyed();
    }

    public boolean isNoticing() {
        return noticing;
    }
}