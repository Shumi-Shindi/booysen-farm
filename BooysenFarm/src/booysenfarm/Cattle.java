package booysenfarm;

/**
 * Cattle class inheriting from Animal.
 * Represents Bovine livestock on the farm.
 */
public class Cattle extends Animal {

    public Cattle(String tagId, String breed, String gender, String status, double weight, String lastVac) {
        // Calls the constructor of the abstract Animal class
        super(tagId, "Cattle", breed, gender, status, weight, lastVac);
    }

    @Override
    public String getSpeciesCategory() {
        // Implementation of polymorphic method
        return "Bovine";
    }
}