import Sidebar from "../../components/Reusables/Sidebar";
import { useEffect, useState } from "react";
import { getUserData } from "../../services/http/user/user-service";
import { getAllPlants } from "../../services/http/plant/plant-service";
import { getPlantsByUserID } from "../../services/http/userplant/userPlant-service";
import MyGardenDisplayComponent from "../../components/MyGarden/MyGardenDisplayComponent";
import { getRandomAd } from "../../services/http/ad/ad-service";
import { UserPlant } from "../../models/userPlant-model";

export default function MyGardenPage() {
  const [userData, setUserData] = useState();
  const [ad, setAd] = useState();
  const [, setPlants] = useState([]);
  const [userPlants, setUserPlants] = useState<UserPlant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>();

  const fetchUserPlants = async () => {
    try {
      const fetchedUserPlants = await getPlantsByUserID();

      if (fetchedUserPlants) {
        setUserPlants(fetchedUserPlants);
      }
    } catch {
      setError("Error fetching User Plants");
    }
  };

  useEffect(() => {
    if (userData) {
      setLoading(false);
    }
  }, [userData, ad]);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const data = await getUserData();
        setUserData(data);
      } catch {
        setError("Error fetching user data");
      }
    };

    fetchUserData();
  }, []);

  useEffect(() => {
    const fetchAds = async () => {
      try {
        const adData = await getRandomAd();

        if (adData) {
          setAd(adData);
        }
      } catch {
        setError("Error fetching ads");
      }
    };

    fetchAds();
  }, []);

  useEffect(() => {
    fetchUserPlants();
  }, []);

  useEffect(() => {
    // Function to fetch plants and userPlants
    const fetchPlants = async () => {
      try {
        const fetchedPlants = await getAllPlants();
        setPlants(fetchedPlants);

        // Fetch user plants after plants are fetched
      } catch {
        setError("Error fetching plants or user plants");
      }
    };

    fetchPlants();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="font-poppins overflow-hidden ">
      <div id="view" className="flex ">
        <div className="flex h-screen">
          <Sidebar userData={userData} />

          {/* MyPreferences Component */}
          <aside className="h-full w-[65%] lg:w-[85%] p-4 bg-white overflow-y-hidden  overflow-hidden justify-center ">
            {error ? (
              <div>Error: {error}</div>
            ) : (
              <MyGardenDisplayComponent
                userplants={userPlants}
                userData={userData}
                onUpdate={fetchUserPlants}
              />
            )}
          </aside>
        </div>
      </div>
    </div>
  );
}
