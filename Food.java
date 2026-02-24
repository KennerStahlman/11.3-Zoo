public abstract class Food extends Entity{
    protected boolean animal;
    protected boolean plant;
    protected int nutrition;

    public Food(String name, int x, int y, boolean animal, boolean plant, int nutrition){
        super(name, x, y);
        this.animal = animal;
        this.plant = plant;
        this.nutrition = nutrition;
    }

    public abstract void beEaten(Animal eater);

    public boolean isAnimalProduct() {
        return animal;
    }

    public boolean isPlantProduct() {
        return plant;
    }

    public int getNutrition() {
        return nutrition;
    }

}
