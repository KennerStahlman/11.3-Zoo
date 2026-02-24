import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Rat extends Animal {
    private int dirDx;
    private int dirDy;
    private int lastMoveTick;
    private int nextDirChangeAge;

    public Rat(String name, int x, int y) {
        super(name, x, y);
        this.hunger = 20;
        this.lastMoveTick = -5;
        pickNewDirection();
        scheduleNextDirChange();
        System.out.println("Rat " + name + " was born today!");
    }

    private void pickNewDirection() {
        int[] d = randomNonZeroDir8();
        dirDx = d[0];
        dirDy = d[1];
    }

    private void scheduleNextDirChange() {
        nextDirChangeAge = age + (40 + Zoo.rand.nextInt(21));
    }

    @Override
    public void tick(Zoo z) {
        age++;
        hunger++;
        if (hunger < 20) hunger = 20;

        if (age > 500) {
            double deathChance = sick ? 0.20 : 0.015;
            if (Zoo.rand.nextDouble() < deathChance) {
                alive = false;
                System.out.println("Rat " + name + " died of old age. R.I.P.");
                return;
            }
        }
        if (sick && Zoo.rand.nextDouble() < 0.01) {
            alive = false;
            System.out.println("Rat " + name + " died of sickness. R.I.P.");
            return;
        }

        if (age % 50 == 0) {
            if (Zoo.rand.nextDouble() < 0.10) {
                int[] d = randomNonZeroDir8();
                int nx = Zoo.wrap(x + d[0], Zoo.ZOO_COLS);
                int ny = Zoo.wrap(y + d[1], Zoo.ZOO_ROWS);
                z.add(new Rat("Rat", nx, ny));
            }
        }

        for (Entity e : z.at(x, y)) {
            if (e instanceof Food) {
                eat((Food) e);
            }
        }

        if (age >= nextDirChangeAge) {
            pickNewDirection();
            scheduleNextDirChange();
        }

        if (age - lastMoveTick >= 5) {
            move(z);
            lastMoveTick = age;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        g.drawString("🐀", Zoo.wrap(x, Zoo.ZOO_COLS) * Zoo.SCALE, Zoo.wrap(y, Zoo.ZOO_ROWS) * Zoo.SCALE + 23);
    }

    @Override
    public void eat(Food food) {
        if (food instanceof Cheese) {
            food.beEaten(this);
            hunger = Math.max(20, hunger);
            return;
        }

        if (hunger > 50) {
            food.beEaten(this);
            hunger = Math.max(20, hunger);
        }
    }

    @Override
    public void move(Zoo zoo) {
        int[] cheeseDir = null;
        for (int dx = -1; dx <= 1 && cheeseDir == null; dx++) {
            for (int dy = -1; dy <= 1 && cheeseDir == null; dy++) {
                if (dx == 0 && dy == 0) continue;
                int checkX = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
                int checkY = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
                for (Entity e : zoo.at(checkX, checkY)) {
                    if (e instanceof Cheese) {
                        cheeseDir = new int[]{dx, dy};
                        break;
                    }
                }
            }
        }

        int dx = (cheeseDir != null) ? cheeseDir[0] : dirDx;
        int dy = (cheeseDir != null) ? cheeseDir[1] : dirDy;

        x = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
        y = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
    }

    public void beEaten(Animal eater) {
        if (!alive) return;
        alive = false;
        eater.hunger = Math.max(0, eater.hunger - 10);
        System.out.println(eater.getClass().getSimpleName() + " " + eater.name + " ate " + name + " #" + id + ", gaining 10 nutrition!");
    }
}

