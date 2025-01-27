/**
 * LogDetailPopup
 * This component displays a popup with details of a specific log entry.
 * The popup is displayed when the `isOpen` prop is true and contains the log's date and description.
 * It also includes a "Close" button to dismiss the popup.
 *
 * Props:
 * - isOpen: A boolean that controls whether the popup is visible or not.
 * - onClose: A function that is triggered to close the popup when the user clicks the "Close" button.
 * - log: A Log object containing the details of the log entry (logDate and logDescription) to be displayed.
 *
 * Rendering Logic:
 * - If the `isOpen` prop is false or the `log` object is not provided, the popup is not rendered.
 * - When the popup is open, it renders the log's date (formatted as a string) and description.
 * - A "Close" button is included to close the popup when clicked.
 */
import React from "react";
import { Log } from "../../models/log-model";

interface LogDetailPopupProps {
  onClose: () => void;
  log: Log;
}

const LogDetailPopup: React.FC<LogDetailPopupProps> = ({
  onClose,
  log,
}) => {
  if (!log) return null;

  return (
    <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50 z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-4xl w-full">
        <p className="text-gray-700 mb-4">
          {new Date(log.logDate).toDateString()}
        </p>
        <p className="text-lg">{log.logDescription}</p>

        <div className="mt-4 flex justify-end">
          <button
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
            onClick={onClose}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default LogDetailPopup;
