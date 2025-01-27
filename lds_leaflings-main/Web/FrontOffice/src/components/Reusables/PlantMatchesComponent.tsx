import React from "react";
import { Plant } from "../../models/plant-model";
import PlantCardComponent from "./PlantCardComponent";
import { User } from "../../models/user-model";



/**
 * Props for the PlantMatchesComponent.
 *
 * @interface PlantMatchesComponentProps
 * @property {Object} matches - An object containing categorized plant matches.
 * @property {Plant[]} matches.perfectMatches - Plants with a perfect match score (3/3).
 * @property {Plant[]} matches.averageMatches - Plants with an average match score (2/3).
 * @property {Plant[]} matches.weakMatches - Plants with a weak match score (1/3).
 * @property {Plant[]} matches.noMatches - Plants with no match score (0/3).
 * @property {User | undefined} userData - Information about the current user.
 */
interface PlantMatchesComponentProps {
  matches: {
    perfectMatches: Plant[];
    averageMatches: Plant[];
    weakMatches: Plant[];
    noMatches: Plant[];
  };
  userData : User | undefined;
}

/**
 * PlantMatchesComponent
 *
 * This component displays categorized matches of plants based on their compatibility
 * with the user's preferences or resources. Each category (Perfect Matches, Average Matches,
 * Weak Matches, No Matches) is displayed as a separate section with a corresponding list of plants.
 *
 * @param {PlantMatchesComponentProps} props - Props passed to the component.
 * @returns {JSX.Element} A functional React component rendering categorized plant matches.
 */
const PlantMatchesComponent: React.FC<PlantMatchesComponentProps> = ({
  matches, userData
}) => {
  /**
   * Categories of plant matches with their titles and corresponding data.
   */
  const categories = [
    { title: "Perfect Matches (3/3)", data: matches.perfectMatches },
    { title: "Average Matches (2/3)", data: matches.averageMatches },
    { title: "Weak Matches (1/3)", data: matches.weakMatches },
    { title: "No Matches (0/3)", data: matches.noMatches },
  ];

  return (
    <div className="space-y-8">
      {categories.map(({ title, data }) => (
        <div key={title} className="bg-white shadow-lg rounded-lg p-6">
          <h3 className="text-xl font-bold mb-4 text-green-600">{title}</h3>
          {data.length > 0 ? (
            <div className="grid grid-cols-2 sm:grid-cols-2 lg:grid-cols-3 gap-6">
              {data.map((plant) => {
                return <PlantCardComponent plant = {plant} userData={ userData } />;
              })}
            </div>
          ) : (
            <p className="text-gray-500">No plants found in this category.</p>
          )}
        </div>
      ))}
    </div>
  );
}  
export default PlantMatchesComponent;
