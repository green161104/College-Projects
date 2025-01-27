import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gradient-to-b from-green-50 to-green-100">
      <div className="max-w-4xl mx-auto px-4 py-12">
        {/* Main content container with background image */}
        <div className="relative rounded-lg overflow-hidden bg-cover bg-center p-8" 
             style={{
               backgroundImage: "url('/api/placeholder/1200/800')",
               backgroundColor: 'rgba(0, 0, 0, 0.4)'
             }}>
          
          {/* Logo and content section */}
          <div className="flex flex-col items-center space-y-8 relative z-10">
            {/* Logo */}
            <div className="flex items-center space-x-2">
              <div className="w-8 h-8 bg-gradient-to-r from-green-500 to-orange-500 rounded-full"></div>
              <h1 className="text-3xl font-bold text-white">Leaflings</h1>
            </div>

            {/* Description Card */}
            <div className="bg-cream-50 bg-opacity-90 p-6 rounded-lg max-w-lg shadow-lg">
              <p className="text-gray-800 mb-4">
                Leafling is a user-friendly app designed to help you take care of your houseplants.
              </p>
              <p className="text-gray-800 mb-4">
                Whether you're a beginner or an expert, our app helps you track your plants' needs and schedule their care routine.
              </p>
              <p className="text-gray-800">
                With tracking logs & plant data, we measure plant progress and help you avoid common mistakes others have faced in their plant care routine.
              </p>
            </div>

            {/* Navigation Buttons */}
            <div className="flex space-x-4 mt-6">
              <button
                onClick={() => navigate('/login')}
                className="px-6 py-2 bg-orange-500 text-white rounded-md hover:bg-orange-600 transition-colors"
              >
                Log in
              </button>
              <button
                onClick={() => navigate('/register')}
                className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
              >
                Register
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
