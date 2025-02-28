package lab1.domain;

public class Bulldozer {
    public void moveOver(Debris debris) {
        System.out.println("The bulldozer is moving over the debris.");
    }
     public void destroy(House house) {
         if (house == null) throw new IllegalArgumentException(); // Добавить
        if (!house.isDestroyed()) {
            house.destroy();
            System.out.println("The bulldozer destroyed the house.");
        } else {
            System.out.println("The house is already destroyed.");
        }
    }
}