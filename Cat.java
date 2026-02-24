
import java.util.*;
import java.awt.*;

public class Cat extends Animal{
    private int lives;
    private int lastMoveTick;
    private boolean madeCat;
    public Cat(String name, int x, int y) {
        super(name, x, y);
        this.lives = 9;
        this.lastMoveTick = -10;
        madeCat = false;
        System.out.println("Cat " + name + id + " was born today");
    }

    @Override
    public void tick(Zoo z) {
        age++;
        
        if (age > 500) {
            double deathChance = sick ? 0.10 : 0.01;
            if (Zoo.rand.nextDouble() < deathChance) {
                lives--;
                if (lives > 0) {
                    alive = true;
                } else {
                    alive = false;
                    System.out.println("Cat " + name + id + " died of old age");
                    return;
                }
            }
        }
        
        if (sick && Zoo.rand.nextDouble() < 0.001) {
            lives--;
            if (lives > 0) {
                alive = true;
            } else {
                alive = false;
                System.out.println("Cat " + name + id + " died of sickness");
                return;
            }
        }
        
        
        int[][] adjacentDirs = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (int[] dir : adjacentDirs) {
            int checkX = Zoo.wrap(x + dir[0], Zoo.ZOO_COLS);
            int checkY = Zoo.wrap(y + dir[1], Zoo.ZOO_ROWS);
            ArrayList<Entity> entities = z.at(checkX, checkY);
            for (Entity e : entities) {
                if (e instanceof Cat && e != this && !madeCat) {
                    if (Zoo.rand.nextDouble() < 0.10) {
                        int newX = Zoo.wrap(x + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_COLS);
                        int newY = Zoo.wrap(y + Zoo.rand.nextInt(3) - 1, Zoo.ZOO_ROWS);
                        Cat newCat = new Cat("Cat", newX, newY);
                        z.add(newCat);
                        madeCat = true;
                    }
                }
            }
        }
        ArrayList<Entity> entitiesAtPosition = z.at(x, y);
        for (Entity e : entitiesAtPosition) {
            if (e instanceof Food && e != this) {
                eat((Food) e);
            }
            if (e instanceof Rat) {
                ((Rat) e).beEaten(this);
            }
        }
        
        // Move every 10 ticks
        if (age - lastMoveTick >= 10) {
            move(z);
            lastMoveTick = age;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        g.drawString("🐈", Zoo.wrap(x,Zoo.ZOO_COLS)*Zoo.SCALE, Zoo.wrap(y,Zoo.ZOO_ROWS)*Zoo.SCALE+25);

        //g.setColor(Color.DARK_GRAY);
        //g.setFont(new Font("Consolas", Font.PLAIN, 10));
        //g.drawString(" ^-^ ", Zoo.wrap(x,Zoo.ZOO_COLS)*Zoo.SCALE, Zoo.wrap(y,Zoo.ZOO_ROWS)*Zoo.SCALE+5);
        //g.drawString("/. .\\", Zoo.wrap(x,Zoo.ZOO_COLS)*Zoo.SCALE, Zoo.wrap(y,Zoo.ZOO_ROWS)*Zoo.SCALE+15);
        //g.drawString("\\_o_/", Zoo.wrap(x,Zoo.ZOO_COLS)*Zoo.SCALE, Zoo.wrap(y,Zoo.ZOO_ROWS)*Zoo.SCALE+25);
    }

    // TODO: override the eat method
    @Override
    public void eat(Food food) {
        if (hunger > 25 && food.isAnimalProduct()) {
            if (Zoo.rand.nextDouble() < 0.99) {
                food.beEaten(this);
            }
        }
    }
    
    // TODO: override the move method
    @Override
    public void move(Zoo zoo) {
        // prefer adjacent rats (even if not hungry), then adjacent food
        int[] dir = null;

        for (int dx = -1; dx <= 1 && dir == null; dx++) {
            for (int dy = -1; dy <= 1 && dir == null; dy++) {
                if (dx == 0 && dy == 0) continue;
                int checkX = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
                int checkY = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
                for (Entity e : zoo.at(checkX, checkY)) {
                    if (e instanceof Rat) {
                        dir = new int[]{dx, dy};
                        break;
                    }
                }
            }
        }

        if (dir == null) {
            dir = findAdjacentFoodDir(zoo);
        }

        int moveDx;
        int moveDy;

        if (dir != null){
            moveDx = dir[0];
            moveDy = dir[1];
        } else {
            int[] randDir = randomNonZeroDir8();
            moveDx = randDir[0];
            moveDy = randDir[1];
        }

        int newX = Zoo.wrap(x + moveDx, Zoo.ZOO_COLS);
        int newY = Zoo.wrap(y + moveDy, Zoo.ZOO_ROWS);

        // allow moving into rats (so we can eat them)
        boolean blockedByAnimal = false;
        for (Entity e : zoo.at(newX, newY)) {
            if (e instanceof Animal && e != this && !(e instanceof Rat)) {
                blockedByAnimal = true;
                break;
            }
        }

        if (blockedByAnimal) {
            int oppositeX = Zoo.wrap(x - moveDx, Zoo.ZOO_COLS);
            int oppositeY = Zoo.wrap(y - moveDy, Zoo.ZOO_ROWS);
            x = oppositeX;
            y = oppositeY;
        } else {
            x = newX;
            y = newY;
        }
        
    }


}
