/**
 * AddLogComponent
 *
 * A modal component for adding a new log entry to a diary. It allows the user to input a description of the log, validate it, and submit it to the server.
 * After a successful submission, the modal will close automatically.
 *
 * Props:
 * - isOpen: A boolean that determines if the modal is visible or not. If false, the modal is not rendered.
 * - onClose: A callback function to close the modal when called. Typically passed from the parent component.
 * - diaryId: The ID of the diary to which the log is being added. This is used to associate the log with a specific diary.
 *
 * State:
 * - logDescription: The description of the log being entered by the user.
 * - loading: A boolean state to track if the form is currently being submitted.
 * - error: A string to hold any error messages that may occur during submission.
 *
 * Functionality:
 * - The user can enter a description for the log. The description is validated to ensure it's not empty before submission.
 * - If the submission is successful, the modal will be closed and the input field will be cleared.
 * - If there is an error during submission, an error message is displayed.
 * - The modal contains a cancel button that will close the modal without saving the log.
 *
 * Methods:
 * - handleSubmit: Handles the form submission by validating the input, calling the `createNewLog` API function, and handling success or error states.
 *
 * @component
 */
import { useState } from "react";
import { createNewLog } from "../../services/http/diarylogs/diarylogs-service"; // Your API function

interface AddLogComponentProps {
  onClose: () => void; // Close the modal
  diaryId: number;
  onUpdate: () => void;
}

const AddLogComponent: React.FC<AddLogComponentProps> = ({
  onClose,
  diaryId,
  onUpdate
}) => {
  const [logDescription, setLogDescription] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!logDescription.trim()) {
      setError("Log description cannot be empty.");
      return;
    }

    try {
      setLoading(true);

      await createNewLog(diaryId, logDescription);
      setLogDescription(""); // Clear the input field

      onClose(); // Close the modal after successful submission
    } catch {
      setError("An error occurred while creating the log.");
    } finally {
      setLoading(false);
      onUpdate()
    }
  };


  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full">
        <h2 className="text-lg font-bold">Add New Log</h2>
        <form onSubmit={handleSubmit} className="mt-4">
          <textarea
            className="w-full p-2 border rounded-md"
            placeholder="Enter log description..."
            value={logDescription}
            onChange={(e) => setLogDescription(e.target.value)}
            rows={4}
          />
          {error && <p className="text-red-500 mt-2">{error}</p>}
          <div className="mt-4 flex justify-between">
            <button
              type="button"
              className="px-4 py-2 bg-gray-500 text-white rounded-lg"
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded-lg"
              disabled={loading}
            >
              {loading ? "Submitting..." : "Submit Log"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddLogComponent;
