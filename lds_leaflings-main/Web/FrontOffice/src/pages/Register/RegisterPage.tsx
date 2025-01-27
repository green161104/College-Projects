import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../../services/http/auth/auth-service";

export default function RegisterPage() {
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    location: "",
    contact: "",
    careExperience: "",
    waterAvailability: "",
    luminosityAvailability: "",
  });

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = event.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);

    const isFormValid = Object.values(formData).every((value) => value.trim() !== "");
    if (!isFormValid) {
        setError("Please fill in all fields before submitting.");
        return;
    }

    try {
        const response = await register(formData);

        if (!response) {
            setError("Registration failed. Please try again.");
            return;
        }

        navigate(`/login`);
    } catch (error) {
        setError(`An error occurred during registration: ${error}`);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-green-300">
      <div className="max-w-md w-full bg-white p-8 rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-center">Register</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="username"
            placeholder="Username"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
          <input
            type="email"
            name="email"
            placeholder="Email"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
          <input
            type="password"
            name="password"
            placeholder="Password"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
          <input
            type="text"
            name="location"
            placeholder="Location"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />
          <input
            type="tel"
            name="contact"
            placeholder="Contact"
            onChange={handleChange}
            pattern="([0-9]{3}){3}"
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          />

          <select
            name="careExperience"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="">Select Care Experience</option>
            <option value="Beginner">Beginner</option>
            <option value="Intermediate">Intermediate</option>
            <option value="Expert">Expert</option>
          </select>

          <select
            name="waterAvailability"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="">Select Water Availability</option>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
          </select>

          <select
            name="luminosityAvailability"
            onChange={handleChange}
            required
            className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
          >
            <option value="">Select Luminosity Availability</option>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
          </select>

          <button
            type="submit"
            className="w-full px-4 py-3 rounded-md bg-gray-200 text-gray-800 hover:bg-teal-500 transition-colors font-medium"
          >
            Register
          </button>

          {error && <p className="text-red-500 mt-4">{error}</p>}
        </form>
      </div>
    </div>
  );
}
