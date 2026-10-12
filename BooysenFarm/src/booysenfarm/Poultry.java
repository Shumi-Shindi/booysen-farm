package booysenfarm;

/**
 * Poultry class inheriting from Animal.
 * Represents Avian livestock (Chickens, etc.) on the farm.
 */
public class Poultry extends Animal {

    public Poultry(String tagId, String breed, String gender, String status, double weight, String lastVac) {
        // Calls the constructor of the abstract Animal class
        super(tagId, "Poultry", breed, gender, status, weight, lastVac);
    }

    @Override
    public String getSpeciesCategory() {
        // Implementation of polymorphic method
        return "Avian";
    }
}