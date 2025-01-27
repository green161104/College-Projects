import { LightLevel } from "./enums/LightLevel";
import { WaterLevels } from "./enums/WaterLevels";
import { PlantType } from "./enums/PlantType";
import { ExperienceLevels } from "./enums/ExperienceLevels";

export interface Plant {
  id: number;
  name: string;
  type: PlantType ;
  expSuggested: ExperienceLevels;
  waterNeeds: WaterLevels;
  luminosityNeeded: LightLevel;
  description: string;
  plantImage: string;
}