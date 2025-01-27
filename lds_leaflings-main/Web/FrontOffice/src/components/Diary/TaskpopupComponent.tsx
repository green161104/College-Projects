/**
 * TaskDetailPopup
 * This component displays a popup with a list of tasks associated with a plant.
 * The popup is visible when the `isOpen` prop is true and is closed by calling the `onClose` function.
 * It shows each task's name and description, or a message indicating there are no tasks available for the plant.
 *
 * Props:
 * - isOpen: A boolean that controls whether the popup is visible or not.
 * - onClose: A function that is triggered to close the popup when the user clicks the "Close" button.
 * - tasks: An array of task objects that contain details about the tasks associated with the plant.
 *          If the tasks array is null or empty, a message is displayed stating that no tasks are available.
 *
 * Rendering Logic:
 * - If the `isOpen` prop is false or the `tasks` array is null, the popup is not rendered.
 * - When the popup is open and tasks are available, it renders a list of tasks.
 * - Each task in the list is displayed with its name and description, styled with padding, borders, and shadow.
 * - If no tasks are available, a message is displayed indicating that.
 * - A "Close" button is provided to close the popup when clicked.
 */
import { Task } from "../../models/task-model";

interface TaskDetailPopupProps {
  onClose: () => void;
  tasks: Task[];
}

const TaskDetailPopup = ({ onClose, tasks }: TaskDetailPopupProps) => {

  return (
    <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex items-center justify-center z-10">
      <div className="bg-white p-6 rounded-lg w-96">

        <h2 className="text-xl font-semibold mb-4">Plant Tasks</h2>

        {tasks && tasks.length  ? (
          <ul className="space-y-4">
            {tasks.map((task) => (
              <li key={task.id} className="border p-4 rounded-lg shadow-sm">
                <h3 className="text-lg font-semibold">{task.taskName}</h3>
                <p className="text-sm text-gray-600">{task.taskDescription}</p>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-500">No tasks available for this plant.</p>
        )}

        <div className="flex justify-end mt-4">
          <button
            onClick={onClose}
            className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskDetailPopup;
