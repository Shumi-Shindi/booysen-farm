package booysenfarm;

/**
 * Sheep class inheriting from Animal.
 * Represents Ovine livestock on the farm.
 */
public class Sheep extends Animal {

    public Sheep(String tagId, String breed, String gender, String status, double weight, String lastVac) {
        // Calls the constructor of the abstract Animal class
        super(tagId, "Sheep", breed, gender, status, weight, lastVac);
    }

    @Override
    public String getSpeciesCategory() {
        // Implementation of polymorphic method
        return "Ovine";
    }
}