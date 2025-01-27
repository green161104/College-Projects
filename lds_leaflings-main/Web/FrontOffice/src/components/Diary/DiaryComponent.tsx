/**
 * MyDiaryDisplayComponent
 *
 * A component that displays a user's diary entries with the ability to add new logs and view tasks related to a specific plant.
 * It includes functionality for viewing detailed logs and tasks through modal popups.
 *
 * Props:
 * - diary: The Diary object containing the diary information (title and ID). If undefined, the component will not render diary-related data.
 * - logs: An array of Log objects representing the diary logs to display. If undefined or empty, a placeholder message will be shown.
 * - plantId: The ID of the plant associated with the tasks. This is used to fetch and display tasks for the specified plant.
 *
 * Functionality:
 * - Displays the diary title and a list of logs.
 * - Allows the user to add a new log via a modal.
 * - When a log is clicked, a detailed view of the log is shown in a popup.
 * - A button allows the user to view tasks related to the specified plant.
 * - A task modal displays tasks retrieved for the plant based on the provided plantId.
 *
 * Modals:
 * - Add Log Modal: Allows the user to add new logs to the diary.
 * - Log Detail Popup: Displays the full details of a selected log, including the log description and date.
 * - Task Detail Popup: Displays the tasks related to the plant, fetched from the server.
 *
 * States:
 * - isModalOpen: Boolean to track whether the Add Log modal is open.
 * - selectedLog: The log object that has been selected for detailed view.
 * - isTaskModalOpen: Boolean to track whether the Task modal is open.
 * - tasks: Array of Task objects related to the plant.
 *
 * Methods:
 * - handleAddLogClick: Opens the modal to add a new log.
 * - closeModal: Closes the Add Log modal.
 * - handleViewFullLog: Opens the detailed log view for the selected log.
 * - closeLogDetailPopup: Closes the Log Detail popup.
 * - handleViewPlantTasks: Fetches tasks related to the specified plantId and opens the task modal.
 * - closeTaskDetailPopup: Closes the Task Detail popup.
 * - getPreviewDescription: Returns a preview of the log description (first 5 words) to display in the log list.
 *
 * @component
 */
import { useState } from "react";
import { Diary } from "../../models/diary-model";
import { Log } from "../../models/log-model";
import { Task } from "../../models/task-model";
import AddLogComponent from "./AddLogComponent";
import LogDetailPopup from "./LogDetailComponent";
import { getTasksByPlant } from "../../services/http/plant/plant-service";
import TaskDetailPopup from "./TaskpopupComponent";

interface MyDiaryDisplayComponentProps {
  diary: Diary;
  logs: Log[] | undefined;
  plantId: number; // Assuming you pass plantId for the selected plant
  onUpdate: () => Promise<void>; // called everytime that logs change
}

const MyDiaryDisplayComponent: React.FC<MyDiaryDisplayComponentProps> = ({
  diary,
  logs,
  plantId,
  onUpdate
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedLog, setSelectedLog] = useState<Log | null>(null); // To store the log clicked
  const [isTaskModalOpen, setIsTaskModalOpen] = useState(false); // For plant tasks modal
  const [tasks, setTasks] = useState<Task[]>([]); // To store the tasks

  const handleAddLogClick = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleViewFullLog = (log: Log) => {
    setSelectedLog(log); // Set the selected log for detailed view
  };

  const closeLogDetailPopup = () => {
    setSelectedLog(null); // Close the popup by clearing the selected log
  };

  const handleViewPlantTasks = async () => {
    try {
      setIsTaskModalOpen(true); // Open the task modal
      
      const tasksData = await getTasksByPlant(plantId); // Fetch tasks for the plant

      setTasks(tasksData);
    } catch (error) {
      console.error("Error fetching tasks:", error);
    }
  };

  const closeTaskDetailPopup = () => {
    setIsTaskModalOpen(false); // Close task popup
    setTasks([]); // Reset tasks state
  };

  const getPreviewDescription = (
    description: string,
    wordCount: number = 5
  ) => {
    const words = description.split(" ");
    return words.length > wordCount
      ? words.slice(0, wordCount).join(" ") + "..."
      : description;
  };

  return (
    <div className="diary-container p-8 bg-white shadow-lg rounded-xl overflow-hidden max-w-6xl mx-auto h-screen">
      {/* Diary Title and Add Log Button */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-semibold text-green-600">
          {diary?.title}
        </h2>
        <div className="flex space-x-4">
          <button
            className="px-6 py-3 bg-orange-500 text-white rounded-lg shadow-md hover:bg-orange-600 transition-all duration-300"
            onClick={handleAddLogClick}
          >
            <span className="font-semibold">Add Log</span>
          </button>
          <button
            className="px-6 py-3 bg-blue-500 text-white rounded-lg shadow-md hover:bg-blue-600 transition-all duration-300"
            onClick={handleViewPlantTasks}
          >
            <span className="font-semibold">View Plant Tasks</span>
          </button>
        </div>
      </div>

      {/* Log Entries Container */}
      <div className="flex flex-col space-y-6">
        <div className="logs-container flex-1 pr-4 overflow-y-auto max-h-[70vh]">
          {logs?.length ? (
            <ul className="list-none space-y-4">
              {logs.map((log, index) => (
                <li
                  key={index}
                  className="border p-4 rounded-lg shadow-sm hover:shadow-xl transition-all duration-300 hover:bg-green-50 cursor-pointer"
                  onClick={() => handleViewFullLog(log)}
                >
                  <p className="text-lg text-gray-700">
                    {getPreviewDescription(log.logDescription, 5)}{" "}
                    {/* only shows first 5 words */}
                  </p>
                  <p className="text-sm text-gray-500 mt-2">
                    {log.logDate
                      ? new Date(log.logDate).toDateString()
                      : "Date not available"}
                  </p>
                </li>
              ))}
            </ul>
          ) : (
            <p className="text-gray-500">
              No logs available. Start by adding one!
            </p>
          )}
        </div>
      </div>

      {/* Modal for Adding Log */}
      {isModalOpen && (
        <AddLogComponent
          onClose={closeModal}
          diaryId={diary ? diary.id : 0}
          onUpdate={onUpdate}
        />
      )}

      {/* Log Detail Popup */}
      {selectedLog && (
        <LogDetailPopup
          onClose={closeLogDetailPopup}
          log={selectedLog}
        />
      )}

      {/* Plant Tasks Modal */}
      {isTaskModalOpen && (
        <TaskDetailPopup
          onClose={closeTaskDetailPopup}
          tasks={tasks}
        />
      )}
    </div>
  );
};

export default MyDiaryDisplayComponent;
