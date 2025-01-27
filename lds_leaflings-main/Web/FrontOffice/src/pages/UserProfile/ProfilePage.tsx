import { useEffect, useState } from "react";
import { User } from "../../models/user-model";
import {
  updateProfilePicture,
  updatePassword,
  getUserData,
} from "../../services/http/user/user-service";
import MyPreferences from "../../components/UserPreferences/UserPreferencesForm-component";
import Sidebar from "../../components/Reusables/Sidebar";
import { Camera } from "lucide-react";
import UserDataUpdate from "../../components/UserPreferences/UpdateUserData";
import { getFullImageUrl } from "../../services/http/plant/plant-service";

export default function ProfilePage() {
  const [userData, setUserData] = useState<User>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isPasswordModalOpen, setIsPasswordModalOpen] = useState(false); // State for password modal
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const fetchUserData = async () => {
    try {
      const data = await getUserData();
      setUserData(data);
      setLoading(false);
    } catch {
      setError("An error occurred while fetching user data");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUserData();
  }, []);

  if (loading) {
    return <main>Loading user data...</main>;
  }

  if (error) {
    return <main>{error}</main>;
  }

  const handleUserUpdate = (updatedUser: User) => {
    setUserData(updatedUser);
  };

  const handleProfilePictureChange = async (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const file = event.target.files?.[0];
    if (!file) {
      alert("Please select a file to upload.");
      return;
    }

    if (!file.type.startsWith("image/")) {
      alert("Only image files are allowed.");
      return;
    }

    try {
      const response = await updateProfilePicture(file);
      alert(response.message || "Profile picture updated successfully!");
      fetchUserData(); // Re-fetch user data to refresh the UI
    } catch (error: Error | unknown) {
      console.error("Error updating profile picture:", error);
      const errorMessage = (error as Error).message || "An error occurred while updating the profile picture.";
      alert(errorMessage);
    }
  };

  const handlePasswordUpdate = async () => {
    if (newPassword !== confirmPassword) {
      alert("New password and confirm password do not match.");
      return;
    }

    try {
      const response = await updatePassword(oldPassword, newPassword);
      alert(response.message || "Password updated successfully!");
      setIsPasswordModalOpen(false); // Close the modal after successful update
      setOldPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (error: unknown) {
      console.error("Error updating password:", error);
      const errorMessage = (error as Error).message || "An error occurred while updating the password.";
      alert(errorMessage);
    }
  };

  return (
    <main className="flex h-screen">
      <Sidebar userData={userData} />

      <div className="flex-1 flex flex-col p-4 overflow-hidden">
        {/* Personal Information Section */}
        <div className="bg-white rounded-lg shadow-md p-10 mb-4 mt-5 relative">
          {/* Profile Picture Container */}
          <div className="flex items-center space-x-4">
            <div className="relative">

              {userData?.userAvatar && userData.userAvatar !== "" ? (
                // Profile Picture
                <img
                  src={getFullImageUrl(userData?.userAvatar)}
                  alt="Profile"
                  className="w-24 h-24 rounded-full object-cover border-4 border-white shadow-lg"
                />
              ) : (
                // Default Profile Picture
                <img
                  src="/default_user_image.png"
                  alt="Profile"
                  className="w-24 h-24 rounded-full object-cover border-4 border-white shadow-lg"
                />
              )}
              <button
                title="Change Profile Picture"
                className="absolute bottom-0 right-0 bg-teal-500 text-white rounded-full w-8 h-8 flex items-center justify-center hover:bg-teal-600 transition-colors"
              >

                <Camera size={16} />

                <input
                  type="file"
                  accept="image/*"
                  onChange={handleProfilePictureChange}
                  className="absolute inset-0 opacity-0 cursor-pointer"
                />
              </button>
            </div>
            <div className="ml-32">
              {/* Update User Data Component */}
              <UserDataUpdate userData={userData} onUpdate={handleUserUpdate} />
              <button
                onClick={() => setIsPasswordModalOpen(true)}
                className="mt-4 ml-4 bg-blue-500 text-white px-6 py-2 rounded hover:bg-blue-600"
              >
                Update Password
              </button>
            </div>
          </div>
        </div>

        {/* Preferences Section */}
        <div className="flex-1 overflow-y-auto mb-4">
          <MyPreferences userData={userData} onUpdate={fetchUserData} />
        </div>

        {/* Password Update Modal */}
        {isPasswordModalOpen && (
          <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-10">
            <div className="bg-white p-6 rounded-lg w-96">
              <h2 className="text-xl font-semibold mb-4">Update Password</h2>
              <div className="mb-4">
                <label htmlFor="oldPassword" className="block mb-2">
                  Old Password
                </label>
                <input
                  type="password"
                  id="oldPassword"
                  value={oldPassword}
                  onChange={(e) => setOldPassword(e.target.value)}
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="mb-4">
                <label htmlFor="newPassword" className="block mb-2">
                  New Password
                </label>
                <input
                  type="password"
                  id="newPassword"
                  value={newPassword}
                  onChange={(e) => setNewPassword(e.target.value)}
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="mb-4">
                <label htmlFor="confirmPassword" className="block mb-2">
                  Confirm New Password
                </label>
                <input
                  type="password"
                  id="confirmPassword"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  className="w-full p-2 border rounded"
                />
              </div>
              <div className="flex space-x-4">
                <button
                  onClick={handlePasswordUpdate}
                  className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                >
                  Save
                </button>
                <button
                  onClick={() => setIsPasswordModalOpen(false)}
                  className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </main>
  );
}
