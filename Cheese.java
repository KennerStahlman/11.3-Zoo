import java.awt.Color;
import java.awt.Graphics;

public class Cheese extends Food{
    private boolean expired;
    private int bitesRemaining;

    public Cheese(String n, int x, int y){
        super(n, x, y, true, false, 10);
        this.expired = false;
        this.bitesRemaining = 3;
    }

    @Override
    public void draw(Graphics g){
        if (expired){
            g.setColor(new Color(200,255,0));
        }
        else{
            g.setColor(Color.YELLOW);
        }
        g.fillRect(Zoo.wrap(x,Zoo.ZOO_COLS)*Zoo.SCALE, Zoo.wrap(y,Zoo.ZOO_ROWS)*Zoo.SCALE, Zoo.SCALE, Zoo.SCALE);
    }

    @Override
    public void tick(Zoo z){
        age++;
        if (!expired && age >= 400 && Zoo.rand.nextDouble() <= 0.01){
            expired = true;
            System.out.println("Cheese #" + id + " got old and rotted away.");
        }
    }
    @Override
    public void beEaten(Animal eater){
        if (!alive){
            return;
        }

        int nutritionGained = 0;

        if (!expired){
            if (bitesRemaining == 3){
                nutritionGained = 10;
            } else if (bitesRemaining == 2){
                nutritionGained = 8;
            } else if (bitesRemaining == 1){
                nutritionGained = 5;
            }
            eater.hunger = Math.max(0, eater.hunger - nutritionGained);
        } else {
            if (!(eater instanceof Rat)){
                eater.sick = true;
                System.out.println(eater.getClass().getSimpleName() + " " + eater.name + " got food poisioning from eating " + name + " #" + id + ".");
            }
            nutritionGained = 0;
        }

        bitesRemaining--;
        System.out.println(eater.getClass().getSimpleName() + " " + eater.name + " ate " + name + " #" + id + ", gaining " + nutritionGained + " nutrition!");

        if (bitesRemaining <= 0){
            alive = false;
        }
    }
}
