package lab1.domain;

public class Debris {
    private final House originHouse;

    public Debris(House originHouse) {
        this.originHouse = originHouse;
    }

    public House getOriginHouse() {
        return originHouse;
    }
}
