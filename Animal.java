import java.util.*;
public abstract class Animal extends Entity{
    protected int hunger;
    protected boolean sick;

    public Animal(String name, int x, int y){
        super(name, x, y);
        this.hunger=0;
        this.sick=false;
    }

    public abstract void eat(Food food);

    public abstract void move(Zoo zoo);
    protected int[] findAdjacentFoodDir(Zoo zoo){
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (dx == 0 && dy == 0){
                    continue;
                }
                int checkX = Zoo.wrap(x + dx, Zoo.ZOO_COLS);
                int checkY = Zoo.wrap(y + dy, Zoo.ZOO_ROWS);
                ArrayList<Entity> entities = zoo.at(checkX, checkY);
                for (Entity e : entities){
                    if (e instanceof Food){
                        return new int[]{dx, dy};
                    }
                }
            }
        }
        return null;
    }

    protected int[] randomNonZeroDir8(){
        int dx, dy;
        do{
            dx = Zoo.rand.nextInt(3) - 1;
            dy = Zoo.rand.nextInt(3) - 1;
        } while(dx == 0 && dy == 0);
        return new int[]{dx, dy};
    }

    protected boolean hasOtherAnimalAt(Zoo zoo, int gridX, int gridY){
        ArrayList<Entity> entitiesAtDest = zoo.at(gridX, gridY);
        for (Entity e : entitiesAtDest){
            if (e instanceof Animal && e != this){
                return true;
            }
        }
        return false;
    }
}
