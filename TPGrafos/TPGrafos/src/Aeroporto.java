public class Aeroporto {

    private double latitude;
    private double longitude;
    private String nome;
    private double x;
    private double y;

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setLong(double longitude){
        this.longitude = longitude;
    }

    public double getLong(){
        return this.longitude;
    }

    public void setLat(double latitude){
        this.latitude = latitude;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLat(){
        return this.latitude;
    }

    public String toString(){
        return this.nome + " | " + this.latitude + " | " + this.longitude + " | " + this.x + " | " + this.y;
    }

    Aeroporto(){
        setNome("");
        setLat(0);
        setLong(0);
        setX(0);
        setY(0);
    }

    Aeroporto(String nome, double latitude, double longitude, double x, double y){
        setNome(nome);
        setLat(latitude);
        setLong(longitude);
        setX(x);
        setY(y);
    }
}
