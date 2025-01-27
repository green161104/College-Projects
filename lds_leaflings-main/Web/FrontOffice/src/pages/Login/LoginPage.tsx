import { ChangeEvent, FormEvent, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../services/http/auth/auth-service";

export default function LoginPage() {
  const navigate = useNavigate();

  const email = useRef("");
  const password = useRef("");
  const [error, setError] = useState<string | null>(null);

  const handleEmail = (event: ChangeEvent<HTMLInputElement>) => {
    email.current = event.target.value;
  };

  const handlePassword = (event: ChangeEvent<HTMLInputElement>) => {
    password.current = event.target.value;
  };

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault(); // Prevent the default form submission behavior
    setError(null); // Reset error state before trying to log in

    // não fazer pedido se os campos estiverem vazios
    if (!email || !password) {
      return;
    }

    try {
      setError(null);

      const data = await login(email.current, password.current);

      if (data.auth) {
        navigate(`/user/dashboard`);
      }
    } catch {
      setError(`Login failed. Wrong credentials.`);
    }
  };

  return (
    <main className="md:flex w-screen h-screen">
      {/* Logo */}
      <div className="md:flex-auto bg-green-200 flex justify-center items-center">
        <img
          src="./leaflings_logo.png"
          className="h-1/3 w-1/3"
          alt="leaflings"
        />
      </div>

      <div className="md:flex-auto bg-orange-400 flex justify-center items-center">
        <form className="w-full max-w-sm" onSubmit={handleSubmit}>
          <h1 className="text-center text-xl xl:text-2xl font-extrabold p-3">
            Login
          </h1>
          <div className="md:flex md:items-center mb-6">
            <div className="md:w-1/3">
              <label className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4">
                Email
              </label>
            </div>
            <div className="md:w-2/3">
              <input
                className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
                type="email"
                onChange={handleEmail}
                placeholder="Email"
              />
            </div>
          </div>
          <div className="md:flex md:items-center mb-6">
            <div className="md:w-1/3">
              <label className="block text-gray-500 font-bold md:text-right mb-1 md:mb-0 pr-4">
                Password
              </label>
            </div>
            <div className="md:w-2/3">
              <input
                className="bg-gray-200 appearance-none border-2 border-gray-200 rounded w-full py-2 px-4 text-gray-700 leading-tight focus:outline-none focus:bg-white focus:border-purple-500"
                type="password"
                onChange={handlePassword}
                placeholder="******************"
              />
            </div>
          </div>

          <div className="md:flex md:items-center">
            <div className="md:w-1/3"></div>
            <div className="md:w-2/3">
              <button
                className="w-full px-4 py-3 rounded-md bg-[#E8D5C4] text-gray-800 hover:bg-[#D4C2B1] transition-colors font-medium"
                type="submit"
              >
                Login
              </button>
            </div>
          </div>

          <div className="md:flex md:items-center">
            <div className="md:w-1/3"></div>
            <div className="md:w-2/3">
              <a
                href=""
                onClick={() => {
                  navigate("/register");
                }}
              >
                Don´t have an account? Sign Up
              </a>
            </div>
          </div>

          {error && (
            <div className="md:flex md:items-center">
              <div className="md:w-1/3"></div>
              <p className="text-white font-bold text-center text-sm mt-2">
                {error}
              </p>
            </div>
          )}
        </form>
      </div>
    </main>
  );
}
