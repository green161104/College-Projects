import { Link } from "react-router-dom";
import CreateOrderButton from "../../components/Payment/PaypalComponent";


export default function PremiumPage() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-r from-green-400 via-white to-green-200 h-screen text-black">
      <div className="text-center max-w-md bg-white/30 p-8 rounded-lg shadow-lg">
        <h1 className="text-4xl font-bold mb-6">Unlock Premium Features</h1>
        <p className="text-lg mb-6">
          Our small sprout team would really appreciate it if you could support us by getting premium! <br />
          This is a one-time purchase that'll make your <span className="text-green-500">Leaflings™</span> experience sleeker and help us improve!
        </p>
        {/** Benefits Section */}
        <div className="bg-white/40 bg-opacity-60 backdrop-blur-xl rounded drop-shadow-lg p-6 mb-6">
          <h2 className="text-xl font-semibold mb-4">What You'll Get:</h2>
          <ul className="text-left space-y-2">
            <li>🌿 Early access to new features</li>
            <li>🍀 Ad-free experience</li>
            <li className="text-center font-bold">🌱Price: 10.00€🌱</li>
          </ul>
        </div>

        <div>
          <CreateOrderButton />
        </div>

        {/** Back to Homepage Button */}
        <div className="mt-6">
          <Link
            to="/user/dashboard"
            className="inline-block px-6 py-3 text-lg font-semibold text-white bg-green-800 rounded-lg shadow-lg hover:bg-green-700 transition duration-200 ease-in-out"
          >
            Back to Homepage
          </Link>
        </div>
      </div>
    </div>
  );
}

