/**
 * Interface representing an Advertisement.
 */
export interface Ad {
    /**
     * Unique identifier for the advertisement.
     */
    id: number;
  
    /**
     * ID of the admin who created or owns the advertisement.
     */
    adminID: number;
  
    /**
     * Indicates whether the advertisement is currently active.
     */
    isActive: boolean;
  
    /**
     * The date when the advertisement starts.
     * This field defines the start date of the ad's validity.
     */
    startDate: Date;
  
    /**
     * The date when the advertisement ends.
     * This field defines the end date of the ad's validity.
     */
    endDate: Date;
  
    /**
     * URL or path to the advertisement's image file.
     * This could be used for displaying the ad's visual content.
     */
    adFile: string;
  }
  