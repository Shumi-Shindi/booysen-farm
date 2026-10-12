package booysenfarm;

public abstract class Animal {
    private String tagId, species, breed, gender, status, lastVac;
    private double weight;

    public Animal(String tagId, String species, String breed, String gender, String status, double weight, String lastVac) {
        this.tagId = tagId; this.species = species; this.breed = breed;
        this.gender = gender; this.status = status; this.weight = weight; this.lastVac = lastVac;
    }

    public String getTagId() { return tagId; }
    public String getSpecies() { return species; }
    public String getBreed() { return breed; }
    public String getGender() { return gender; }
    public String getStatus() { return status; }
    public double getWeight() { return weight; }
    public String getLastVac() { return lastVac; }

    public abstract String getSpeciesCategory(); 
}