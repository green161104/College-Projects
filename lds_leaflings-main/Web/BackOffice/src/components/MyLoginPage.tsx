import { useState } from "react";
import { useLogin, useNotify } from "react-admin";

/**
 * `MyLoginPage` Component
 *
 * A custom login page that allows users to log in with their email and password.
 * It uses `useLogin` and `useNotify` hooks from `react-admin` for authentication and notification handling.
 */
export default function MyLoginPage() {
  // Local state for storing email and password inputs
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // `useLogin` hook to handle the login action
  const login = useLogin();

  // `useNotify` hook to display notifications on failure
  const notify = useNotify();

  /**
   * Handle form submission. 
   * If login is unsuccessful, notify the user about invalid email or password.
   */
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    
    login({ email, password }).catch(() => notify("Invalid email or password"));
  };

  return (
    <div className="w-screen h-screen grid place-items-center">
      <div className="text-center p-10 shadow bg-white">
        <h2 className="text-2xl">Login</h2>
        
        {/* Login form */}
        <form onSubmit={handleSubmit}>
          {/* Email input */}
          <input
            type="email"
            className="p-3 m-2 border-black"
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email..."
          />
          <br />
          
          {/* Password input */}
          <input
            type="password"
            className="p-3 m-2"
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password..."
          />
          <br />

          {/* Submit button */}
          <button
            type="submit"
            className="bg-green-500 hover:bg-green-600 text-white font-bold pl-6 pr-6 pt-3 pb-3 mt-3 rounded-2xl"
          >
            Enter
          </button>
        </form>
      </div>
    </div>
  );
}
