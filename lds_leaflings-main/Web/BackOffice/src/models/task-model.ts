/**
 * Interface representing a Task entity.
 */
export interface Task {
    /**
     * Unique identifier for the task.
     * This is used to differentiate tasks in the system.
     */
    id: number;
  
    /**
     * ID of the admin who created or manages the task.
     */
    adminId: number;
  
    /**
     * ID of the plant associated with the task.
     * This links the task to a specific plant.
     */
    plantId: number;
  
    /**
     * Name or title of the task.
     * A brief description of what the task involves.
     */
    taskName: string;
  
    /**
     * Detailed description of the task.
     * This provides more information about what needs to be done.
     */
    taskDescription: string;
  }
  
  /**
   * Interface for creating or updating a task.
   * This does not include `id`, `adminId`, or `plantId` since those are typically handled by the backend.
   */
  export interface TaskRequest {
    /**
     * Name or title of the task.
     * A brief description of what the task involves.
     */
    taskName: string;
  
    /**
     * Detailed description of the task.
     * This provides more information about what needs to be done.
     */
    taskDescription: string;
  }
  