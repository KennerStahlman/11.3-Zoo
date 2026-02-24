import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Dog extends Animal {
    private int preferredDx;
    private int preferredDy;
    private int lastMoveTick;
    private boolean spawnedOnDeath;

    public Dog(String name, int x, int y) {
        super(name, x, y);
        this.lastMoveTick = -15;
        this.spawnedOnDeath = false;
        int r = Zoo.rand.nextInt(4);
        if (r == 0) { preferredDx = 0; preferredDy = -1; }
        else if (r == 1) { preferredDx = 0; preferredDy = 1; }
        else if (r == 2) { preferredDx = -1; preferredDy = 0; }
        else { preferredDx = 1; preferredDy = 0; }

        System.out.println("Dog " + name + " was born today!");
    }

    @Override
    public void tick(Zoo z) {
        age++;
        hunger++;

        if (age > 1000) {
            double deathChance = sick ? 0.01 : 0.001;
            if (Zoo.rand.nextDouble() < deathChance) {
                alive = false;
                System.out.println("Dog " + name + " died of old age. R.I.P.");
                spawnOnDeath(z);
                return;
            }
        }

        for (Entity e : z.at(x, y)) {
            if (e instanceof Food) {
                eat((Food) e);
            }
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int checkX = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
                int checkY = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
                ArrayList<Entity> ents = z.at(checkX, checkY);
                for (Entity ent : ents) {
                    if (ent instanceof Dog && ent != this) {
                        if (Zoo.rand.nextDouble() < 0.25) {
                            Dog other = (Dog) ent;
                            preferredDx = other.preferredDx;
                            preferredDy = other.preferredDy;
                        }
                    }
                }
            }
        }

        if (age - lastMoveTick >= 15) {
            move(z);
            lastMoveTick = age;
        }
    }

    private void spawnOnDeath(Zoo z) {
        if (spawnedOnDeath) return;
        spawnedOnDeath = true;

        int spawnCount = 0;
        if (Zoo.rand.nextDouble() < 0.50) spawnCount++;
        if (Zoo.rand.nextDouble() < 0.25) spawnCount++;

        for (int i = 0; i < spawnCount; i++) {
            int[] dir = randomNonZeroDir8();
            int nx = Zoo.wrap(x + dir[0], Zoo.ZOO_COLS);
            int ny = Zoo.wrap(y + dir[1], Zoo.ZOO_ROWS);
            z.add(new Dog("Pup", nx, ny));
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(120, 80, 40));
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        g.drawString("🐕", Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE, Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE + 25);
    }

    @Override
    public void eat(Food food) {
        if (!food.isAnimalProduct()) {
            return;
        }

        double chance = (hunger > 50) ? 1.0 : 0.01;
        if (Zoo.rand.nextDouble() < chance) {
            food.beEaten(this);
        }
    }

    @Override
    public void move(Zoo zoo) {
        int dx;
        int dy;

        if (Zoo.rand.nextDouble() < 0.75) {
            dx = preferredDx;
            dy = preferredDy;
        } else {
            int r = Zoo.rand.nextInt(4);
            if (r == 0) { dx = 0; dy = -1; }
            else if (r == 1) { dx = 0; dy = 1; }
            else if (r == 2) { dx = -1; dy = 0; }
            else { dx = 1; dy = 0; }
        }

        x = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
        y = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
    }
}

