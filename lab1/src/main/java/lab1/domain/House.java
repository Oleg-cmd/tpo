package lab1.domain;
import java.util.List;
import java.util.ArrayList;
public class House {
    private boolean destroyed = false;
    private Debris debris;
    private String address;
    private List<String> contents;

    public House(String address) {
        this.address = address;
        this.contents = new ArrayList<>();
    }

    public void destroy() {
        if (!destroyed) {
            this.destroyed = true;
            this.debris = new Debris(this);
            System.out.println("The house at " + address + " has been destroyed.");
        }
        else{
            throw new IllegalStateException("House already destroyed!");
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public Debris getDebris() {
        return debris;
    }

    public String getAddress() {
        return address;
    }

    public  void addContent(String item){
        this.contents.add(item);
    }
    public void removeContent(String item){
        this.contents.remove(item);
    }
    public List<String> getContents(){
        return  this.contents;
    }
}
