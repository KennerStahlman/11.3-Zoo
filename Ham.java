import java.awt.Color;
import java.awt.Graphics;

public class Ham extends Food {
    public Ham(String n, int x, int y) {
        super(n, x, y, true, false, 15);
    }

    @Override
    public void tick(Zoo z) {
        age++;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(180, 80, 90));
        g.fillOval(Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE, Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE, Zoo.SCALE, Zoo.SCALE);
    }

    @Override
    public void beEaten(Animal eater) {
        if (!alive) {
            return;
        }

        boolean expired = age >= 200;
        int nutritionGained;

        if (expired) {
            eater.sick = true;
            eater.hunger += 5;
            nutritionGained = -5;
            System.out.println(eater.getClass().getSimpleName() + " " + eater.name + " got food poisioning from eating " + name + " #" + id + ".");
        } else {
            eater.hunger = Math.max(0, eater.hunger - 15);
            nutritionGained = 15;
        }

        alive = false;
        System.out.println(eater.getClass().getSimpleName() + " " + eater.name + " ate " + name + " #" + id + ", gaining " + nutritionGained + " nutrition!");
    }
}

