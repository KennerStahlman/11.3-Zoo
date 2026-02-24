
import javax.swing.*;
import java.awt.*;

import java.util.*;
public class Zoo extends JPanel {

    public static final int ZOO_ROWS = 30;
    public static final int ZOO_COLS = 40;
    public static final int SCALE = 30;

    public static Random rand = new Random();

    private int width, height;
    private ArrayList<ArrayList<LinkedList<Entity>>> grid;

    public Zoo(int w, int h) {
        grid = new ArrayList<>(h);

        for(int y = 0; y < h; y++) {
            ArrayList<LinkedList<Entity>> row = new ArrayList<>(w);
            for(int x = 0; x < w; x++) {
                row.add(new LinkedList<Entity>());
            }
            grid.add(row);
        }
        width = w;
        height = h;
    }

	public void paintComponent(Graphics g){
		super.paintComponent(g); 
		setBackground(Color.GREEN);

        g.setColor(new Color(0, 200, 0));
        for(int y = 0; y < height; y++) {
            g.drawLine(0, y * SCALE, width * SCALE, y * SCALE);
        }
        for(int x = 0; x < width; x++) {
            g.drawLine(x * SCALE, 0, x * SCALE, height * SCALE);
        }

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                for(Entity e : grid.get(y).get(x)) {
                    e.draw(g);
                }
            }
        }
	}

    public void tick() {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {

                for(int i = grid.get(y).get(x).size() - 1; i >= 0; i--) {

                    Entity e = grid.get(y).get(x).remove(i);

                    if(e.isAlive()) {
                        e.tick(this);
                        grid.get(wrap(e.getY(), height)).get(wrap(e.getX(), width)).add(e);
                    }
                }
            }
        }
    }

    public ArrayList<Entity> at(int x, int y) {
        int atX = wrap(x, width);
        int atY = wrap(y, height);
        return new ArrayList<Entity>(grid.get(atY).get(atX));
    }

    public void add(Entity e) {
        int atX = wrap(e.getX(), width);
        int atY = wrap(e.getY(), height);
        grid.get(atY).get(atX).add(e);
    }

    public static int wrap(int val, int thresh) {
        if(val >= 0) {
            return val % thresh;
        }
        else {
            return (thresh - val) % thresh;
        }
    }

    public static void main(String[] args) {
        Zoo zoo = new Zoo(ZOO_COLS, ZOO_ROWS);

        JFrame frame = new JFrame("Zoo");
		frame.setSize(ZOO_COLS * SCALE + SCALE/2, ZOO_ROWS * SCALE + SCALE/2 + 23);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(zoo);
		frame.setVisible(true);
        Animal cat1 = new Cat("bartholemew", 5, 5);
        zoo.add(cat1);
        Animal cat2 = new Cat("bartholemeow", 4, 5);
        zoo.add(cat2);
        Cheese cheese = new Cheese("cheese", 20, 20);
        zoo.add(cheese);
        Dog dog1 = new Dog("henry", 10, 10);
        zoo.add(dog1);
        Rat rat1 = new Rat("rat", 25, 25);
        zoo.add(rat1);
        Ham ham1 = new Ham("ham", 18, 18);
        zoo.add(ham1);

        int tickCount = 0;
        while(true) {
            try {
                Thread.sleep(100);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }

            if (tickCount%50 == 0 && tickCount != 0){
                zoo.add(new Cheese("cheese", rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            }
            if (tickCount%100 == 0 && tickCount != 0){
                zoo.add(new Ham("ham", rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            }
            if (tickCount%150 == 0 && tickCount != 0){
                zoo.add(new Dog("dog", rand.nextInt(ZOO_COLS), rand.nextInt(ZOO_ROWS)));
            }
            zoo.tick();

            zoo.revalidate();
            zoo.repaint();

            tickCount++;
        }
    }

}
