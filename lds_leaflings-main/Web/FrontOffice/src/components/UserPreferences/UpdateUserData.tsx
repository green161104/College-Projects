/**
 * Component: UserDataUpdate
 *
 * This component displays and allows updating of user information, including username, location, and contact details.
 *
 * Props:
 * - `userData`: A `User` object representing the current user information.
 * - `onUpdate`: A callback function triggered after successful user data update, passing the updated user data.
 *
 * Features:
 * - Displays user details in a non-editable format.
 * - Provides an "Edit Profile" button to toggle to editable mode.
 * - Allows users to update their username, location, and contact information.
 * - Handles form submission and error display.
 */

import { useState } from "react";
import { User } from "../../models/user-model";
import { updateUserInformation } from "../../services/http/user/user-service"; // Adjust the import path

interface UserDataUpdateProps {
  userData: User | undefined;
  onUpdate: (updatedUser: User) => void;
}

export default function UserDataUpdate({ userData, onUpdate }: UserDataUpdateProps) {
   // State for editing mode, form data, and error messages
  const [isEditing, setIsEditing] = useState(false);
  const [username, setUsername] = useState(userData?.username || "");
  const [location, setLocation] = useState(userData?.location || "");
  const [contact, setContact] = useState(userData?.contact || "");
  const [error, setError] = useState<string | null>(null);

  /**
   * Handles the save operation, calling the service to update user information
   * and triggering the `onUpdate` callback with the updated data.
   */
  const handleSave = async () => {
    if (!userData) return;

    try {
      // Call the service to update the username, location, and contact
      await updateUserInformation(username, location, contact);

      // Trigger parent update with the updated user data
      onUpdate({
        ...userData, // Keep the existing data
        username: username,
        location: location,
        contact: contact,
      });

      // Exit editing mode and reset the error state
      setIsEditing(false);
      setError(null);
    } catch (err) {
      setError("Failed to update user information.");
      console.error(err);
    }
  };
  
 // Return null if no user data is available
  if (!userData) return null;

  return (
    <div className="p-4">
      {!isEditing ? (
        <div className="grid grid-cols-2 gap-4 items-center">
          <div className="bg-gray-50 p-3 rounded-lg">
            <strong className="text-gray-600 block mb-1">Username</strong>
            <p className="font-semibold">{userData.username}</p>
          </div>
          <div className="bg-gray-50 p-3 rounded-lg">
            <strong className="text-gray-600 block mb-1">Location</strong>
            <p className="font-semibold">{userData.location}</p>
          </div>
          <div className="bg-gray-50 p-3 rounded-lg">
            <strong className="text-gray-600 block mb-1">Contact</strong>
            <p className="font-semibold">{userData.contact}</p>
          </div>
          <div className="bg-gray-50 p-3 rounded-lg">
            <strong className="text-gray-600 block mb-1">Account Type</strong>
            <p className="font-semibold">{userData?.rolePaid ? "Premium" : "Free"}</p>
          </div>
          <button
            onClick={() => setIsEditing(true)}
            className="col-span-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            Edit Profile
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          <div>
            <label className="block mb-2">Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full p-2 border rounded"
            />
          </div>
          <div>
            <label className="block mb-2">Location</label>
            <input
              type="text"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              className="w-full p-2 border rounded"
            />
          </div>
          <div>
            <label className="block mb-2">Contact</label>
            <input
              type="text"
              value={contact}
              onChange={(e) => setContact(e.target.value)}
              className="w-full p-2 border rounded"
            />
          </div>
          {error && <p className="text-red-500">{error}</p>}
          <div className="flex space-x-2">
            <button
              onClick={handleSave}
              className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
            >
              Save
            </button>
            <button
              onClick={() => {
                setIsEditing(false);
                setUsername(userData.username);
                setLocation(userData.location);
                setContact(userData.contact);
              }}
              className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
