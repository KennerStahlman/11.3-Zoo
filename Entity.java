
import java.awt.Graphics;

public abstract class Entity {
    private static int lastID = 0;
    protected int id;
    protected int age;
    protected String name;
    protected boolean alive;
    protected int x, y;


    public Entity(String name, int x, int y) {
        this.id = lastID;
        lastID = lastID + 1;

        this.x = x;
        this.y = y;
        this.name = name;
        this.alive = true;
        this.age = 0;
    }

    public abstract void tick(Zoo z);
    public abstract void draw(Graphics g);


    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
