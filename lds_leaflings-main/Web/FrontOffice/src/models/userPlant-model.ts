import { Plant } from "./plant-model";

export interface UserPlant {
  id?: number;
  userId?: number;
  plantId: number;
  plant : Plant,
}
