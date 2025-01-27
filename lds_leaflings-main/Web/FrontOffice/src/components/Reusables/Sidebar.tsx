import { useNavigate } from "react-router-dom";
import { User } from "../../models/user-model";
import { logout } from "../../services/http/auth/auth-service";
import { Bounce, toast } from "react-toastify";
import { getFullImageUrl } from "../../services/http/plant/plant-service";

/**
 * Props for the Sidebar component.
 *
 * @interface SidebarProps
 * @property {User | undefined} userData - The user data, which contains details like username, avatar, and role information.
 */
interface SidebarProps {
  userData?: User;
}

/**
 * Sidebar component for navigation and user interaction.
 *
 * This component provides links to various parts of the application and displays user-specific
 * information such as their avatar, username, and subscription status. It also handles user logout
 * and navigation to the premium page or other areas.
 *
 * @param {SidebarProps} props - Props passed to the Sidebar component.
 * @returns {JSX.Element} A functional React component.
 */
const Sidebar: React.FC<SidebarProps> = ({ userData }) => {
  const navigate = useNavigate();
  /**
   * Handles the logout process.
   *
   * - Removes the user's authentication token from local storage using the `logout` service.
   * - Redirects the user to the login page.
   */
  const handleLogout = () => {
    // service that removes token from local storage
    logout();
    // navigate to login page
    navigate("/login");
  };

  /**
   * Handles the click event for the "Premium" link.
   *
   * - If the user has a premium role, a success toast is displayed.
   * - Otherwise, navigates the user to the premium subscription page.
   *
   * @param {React.MouseEvent<HTMLAnchorElement>} e - The click event object.
   */
  const handleClick: React.MouseEventHandler<HTMLAnchorElement> = (e) => {
    e.preventDefault();
    if (userData?.rolePaid) {
      toast.success("You already have premium!🌿", {
        position: "bottom-center",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "colored",
        transition: Bounce,
      });
    } else {
      navigate("/user/premium");
    }
  };

  return (
    <div className="font-poppins antialiased overflow-hidden ">
      <div id="view" className="h-full w-11/12 flex flex-row">
        <button
          onClick={() => navigate("/user/profile")}
          className="p-2 border-2  bg-white rounded-md border-gray-200 shadow-lg text-gray-500 focus:bg-#F5EDE4 focus:outline-none focus:text-white absolute top-0 left-0 sm:hidden"
        >
          <svg
            className="w-5 h-5 fill-current"
            fill="currentColor"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              fillRule="evenodd"
              d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
              clipRule="evenodd"
            ></path>
          </svg>
        </button>
        <div
          id="sidebar"
          className=" bg-[#E6D4C3] h-screen md:block shadow-xl px-3 w-30 md:w-60 lg:w-60  overflow-hidden transition-transform duration-300 ease-in-out "
        >
          <div className="space-y-6 md:space-y-10 mt-10 overflow-hidden">
            <h1 className="hidden md:block font-bold text-sm md:text-xl text-center">
              Leaflings<span className="text-orange-600">.</span>
            </h1>

            <div id="profile" className="space-y-3 overflow-hidden">
              {userData?.userAvatar && userData.userAvatar !== "" ? (
                // User Avatar
                <img
                  src={getFullImageUrl(userData?.userAvatar)}
                  alt="Avatar user"
                  className="w-16 h-16 md:w-16 md:h-16 rounded-full object-cover mx-auto"
                />
              ) : (
                // Default Avatar
                <img
                  src="/default_user_image.png"
                  alt="Profile"
                  className="w-16 h-16 md:w-16 md:h-16 rounded-full object-cover mx-auto"
                />
              )}
              <div>
                <h2 className="font-bold text-xs md:text-sm text-center text-green-700">
                  {userData?.username || "Loading..."} 
                </h2>
                <p className="text-xs text-gray-500 text-center">
                  {userData?.rolePaid ? "Premium" : "Free"}
                </p>
              </div>
            </div>
          </div>
          <div
            id="menu"
            className="flex flex-col space-y-2 mt-20 overflow-hidden"
          >
            <a
              href=""
              className="text-sm font-medium text-gray-700 py-2 px-2 hover:bg-[#F5EDE4] hover:text-gray-900 hover:text-base rounded-md transition duration-150 ease-in-out overflow-hidden"
              onClick={() => navigate("/user/profile")}
            >
              <svg
                className="inline-block mr-5"
                xmlns="http://www.w3.org/2000/svg"
                height="24px"
                viewBox="0 -960 960 960"
                width="24px"
                fill="#292929"
              >
                <path d="M480-480q-66 0-113-47t-47-113q0-66 47-113t113-47q66 0 113 47t47 113q0 66-47 113t-113 47ZM160-160v-112q0-34 17.5-62.5T224-378q62-31 126-46.5T480-440q66 0 130 15.5T736-378q29 15 46.5 43.5T800-272v112H160Zm80-80h480v-32q0-11-5.5-20T700-306q-54-27-109-40.5T480-360q-56 0-111 13.5T260-306q-9 5-14.5 14t-5.5 20v32Zm240-320q33 0 56.5-23.5T560-640q0-33-23.5-56.5T480-720q-33 0-56.5 23.5T400-640q0 33 23.5 56.5T480-560Zm0-80Zm0 400Z" />
              </svg>
              <span className="">{userData?.username}</span>
            </a>

            <a
              href=""
              className="text-sm font-medium text-gray-700 py-2 px-2 hover:bg-[#F5EDE4] hover:text-gray-900 hover:text-base rounded-md transition duration-150 ease-in-out overflow-hidden"
              onClick={() => navigate("/user/dashboard")}
            >
              <svg
                className="inline-block mr-5"
                xmlns="http://www.w3.org/2000/svg"
                height="24px"
                viewBox="0 -960 960 960"
                width="24px"
                fill="#292929"
              >
                <path d="M240-240h220v-160H240v160Zm0-200h220v-280H240v280Zm260 200h220v-280H500v280Zm0-320h220v-160H500v160ZM200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v80h80v80h-80v80h80v80h-80v80h80v80h-80v80q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm0-560v560-560Z" />
              </svg>
              <span className="">Dashboard</span>
            </a>

            <a
              href=""
              className="text-sm font-medium text-gray-700 py-2 px-2 hover:bg-[#F5EDE4] hover:text-gray-900 hover:text-base rounded-md transition duration-150 ease-in-out overflow-hidden"
              onClick={() => navigate("/user/mygarden")}
            >
              <svg
                className="inline-block mr-5"
                xmlns="http://www.w3.org/2000/svg"
                height="24px"
                viewBox="0 -960 960 960"
                width="24px"
                fill="#292929"
              >
                <path d="M342-160h276l40-160H302l40 160Zm0 80q-28 0-49-17t-28-44l-45-179h520l-45 179q-7 27-28 44t-49 17H342ZM200-400h560v-80H200v80Zm280-240q0-100 70-170t170-70q0 90-57 156t-143 80v84h320v160q0 33-23.5 56.5T760-320H200q-33 0-56.5-23.5T120-400v-160h320v-84q-86-14-143-80t-57-156q100 0 170 70t70 170Z" />
              </svg>
              <span className="">My Garden</span>
            </a>

            <a
              href=""
              className="text-sm font-medium text-gray-700 py-2 px-2 hover:bg-[#F5EDE4] hover:text-gray-900 hover:text-base rounded-md transition duration-150 ease-in-out overflow-hidden"
              onClick={handleClick}
            >
              <svg
                className="inline-block mr-5"
                xmlns="http://www.w3.org/2000/svg"
                height="24px"
                viewBox="0 -960 960 960"
                width="24px"
                fill="#292929"
              >
                <path d="M841-518v318q0 33-23.5 56.5T761-120H201q-33 0-56.5-23.5T121-200v-318q-23-21-35.5-54t-.5-72l42-136q8-26 28.5-43t47.5-17h556q27 0 47 16.5t29 43.5l42 136q12 39-.5 71T841-518Zm-272-42q27 0 41-18.5t11-41.5l-22-140h-78v148q0 21 14 36.5t34 15.5Zm-180 0q23 0 37.5-15.5T441-612v-148h-78l-22 140q-4 24 10.5 42t37.5 18Zm-178 0q18 0 31.5-13t16.5-33l22-154h-78l-40 134q-6 20 6.5 43t41.5 23Zm540 0q29 0 42-23t6-43l-42-134h-76l22 154q3 20 16.5 33t31.5 13ZM201-200h560v-282q-5 2-6.5 2H751q-27 0-47.5-9T663-518q-18 18-41 28t-49 10q-27 0-50.5-10T481-518q-17 18-39.5 28T393-480q-29 0-52.5-10T299-518q-21 21-41.5 29.5T211-480h-4.5q-2.5 0-5.5-2v282Zm560 0H201h560Z" />
              </svg>
              <span className="">Premium</span>
            </a>

            <a
              href=""
              className="text-sm font-medium text-gray-700 py-2 px-2 hover:bg-[#F5EDE4] hover:text-gray-900 hover:text-base rounded-md transition duration-150 ease-in-out overflow-hidden"
              onClick={handleLogout}
            >
              <svg
                className="inline-block mr-5"
                xmlns="http://www.w3.org/2000/svg"
                height="24px"
                viewBox="0 -960 960 960"
                width="24px"
                fill="#292929"
              >
                <path d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h280v80H200v560h280v80H200Zm440-160-55-58 102-102H360v-80h327L585-622l55-58 200 200-200 200Z" />
              </svg>
              <span className="">Logout</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};
export default Sidebar;
