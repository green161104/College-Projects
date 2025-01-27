/**
 * Component: MyPreferences
 *
 * This component allows users to view and update their plant care preferences.
 * 
 * Props:
 * - `userData`: A `User` object containing the user's current preferences.
 * - `onUpdate`: Callback function to re-fetch user data when preferences are updated.
 * 
 * Features:
 * - Displays user preferences for water availability, luminosity availability, and care experience.
 * - Enables users to modify preferences through a form and submit changes.
 * - Displays success or error messages after form submission.
 */

import { useState } from "react";
import { User, UserPreferences } from "../../models/user-model";
import { updateUserPreferences } from "../../services/http/user/user-service";

interface MyPreferencesProps {
  userData: User | undefined;
  onUpdate: () => void;
}
export default function MyPreferences({
  userData,
  onUpdate,
}: MyPreferencesProps) {

   // State variables for error, success messages, and form data
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [formData, setFormData] = useState<UserPreferences>({
    waterAvailability: userData?.waterAvailability || "Low", // default values
    luminosityAvailability: userData?.luminosityAvailability || "Low",
    careExperience: userData?.careExperience || "Beginner",
  });

  /**
   * Handles input changes in the form and updates the form data state.
   * @param event - The change event triggered by input or select elements.
   */
  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

   /**
   * Submits the form data to update the user's preferences.
   * @param event - The form submission event.
   */
  const handleSubmission = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    try {
      await updateUserPreferences(formData);
      onUpdate(); // Call the callback function to re-fetch user data on the parent component
      setSuccess("User preferences updated successfully");
    } catch {
      setError("An error occurred while updating user preferences");
    }
  };

  return (
    <div className="p-6 bg-white rounded-lg shadow-md">
      <h5 className="text-xl font-semibold mb-4">My Preferences</h5>
      {userData ? (
        <form onSubmit={handleSubmission}>
          <div className="mb-4">
            <label className="block text-gray-700 font-medium mb-2">
              Water Availability:
            </label>
            <select
              name="waterAvailability"
              defaultValue={userData.waterAvailability}
              onChange={handleChange}
              required
              className="block w-full mt-1 p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="Low">Low</option>
              <option value="Medium">Medium</option>
              <option value="High">High</option>
            </select>
          </div>
          <div className="mb-4">
            <label className="block text-gray-700 font-medium mb-2">
              Luminosity Availability:
            </label>
            <select
              name="luminosityAvailability"
              defaultValue={userData.luminosityAvailability}
              onChange={handleChange}
              required
              className="block w-full mt-1 p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="Low">Low</option>
              <option value="Medium">Medium</option>
              <option value="High">High</option>
            </select>
          </div>
          <div className="mb-4">
            <label className="block text-gray-700 font-medium mb-2">
              Care Experience:
            </label>
            <select
              name="careExperience"
              defaultValue={userData.careExperience}
              onChange={handleChange}
              required
              className="block w-full mt-1 p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="Beginner">Beginner</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Expert">Expert</option>
            </select>
          </div>

          <button
            type="submit"
            className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
          >
            Update Preferences
          </button>
        </form>
      ) : (
        <p className="text-gray-500">No preferences found.</p>
      )}

      {error && <p className="text-red-500 mt-4">{error}</p>}
      {success && <p className="text-green-500 mt-4">{success}</p>}
    </div>
  );
}
