/**
 * Interface representing a Plant entity.
 */
export interface Plant {
    /**
     * Unique identifier for the plant.
     */
    id: number;
  
    /**
     * Name of the plant.
     * This will be used to identify and display the plant in the app.
     */
    name: string;
  
    /**
     * Type of the plant, indicating its category.
     * Possible values: "Decorative", "Fruit", "Vegetable", "Flower", "Succulent", "Medicinal".
     */
    type: "Decorative" | "Fruit" | "Vegetable" | "Flower" | "Succulent" | "Medicinal";
  
    /**
     * Suggested experience level for caring for the plant.
     * Possible values: "Beginner", "Intermediate", "Expert".
     */
    expSuggested: "Beginner" | "Intermediate" | "Expert";
  
    /**
     * Water needs of the plant.
     * Possible values: "Low", "Medium", "High".
     */
    waterNeeds: "Low" | "Medium" | "High";
  
    /**
     * Luminosity (light) required for the plant.
     * Possible values: "Low", "Medium", "High".
     */
    luminosityNeeded: "Low" | "Medium" | "High";
  
    /**
     * URL or path to the plant's image.
     * This field will contain the image used for the plant in the application.
     */
    plantImage: string;
  }
  