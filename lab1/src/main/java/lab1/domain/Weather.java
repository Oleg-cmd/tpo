package lab1.domain;

public class Weather {
    private boolean cold = false;
    private boolean windy = false;
    private boolean rainy = false;
    

     public Weather() {
    }
    
    
    public Weather(boolean cold, boolean windy, boolean rainy) {
        this.cold = cold;
        this.windy = windy;
        this.rainy = rainy;
    }

    public void setCold(boolean cold) {
        this.cold = cold;
        System.out.println("It's getting cold.");
    }

    public void setWindy(boolean windy) {
        this.windy = windy;
        System.out.println("The wind is picking up.");
    }
     public void setRainy(boolean rainy) {
        this.rainy = rainy;
        System.out.println("It's starting to rain.");
    }

    public boolean isCold() {
        return cold;
    }

    public boolean isWindy() {
        return windy;
    }

    public boolean isRainy() {
        return rainy;
    }

}