import { useEffect, useState } from "react";
import { User } from "../../models/user-model";
import { getUserData } from "../../services/http/user/user-service";
import { getAllPlants } from "../../services/http/plant/plant-service";
import { Plant } from "../../models/plant-model";
import PlantManual from "../../components/Reusables/PlantManual";
import Sidebar from "../../components/Reusables/Sidebar";
import { Ad } from "../../models/Ad";
import AdComponent from "../../components/Reusables/AdComponent";
import { getRandomAd } from "../../services/http/ad/ad-service";

export default function DashboardPage() {
  const [error, setError] = useState<string | null>(null);
  const [userData, setUserData] = useState<User>();
  const [plantData, setPlantData] = useState<Plant[]>([]); // aqui tenho todas as plantas
  const [ad, setAd] = useState<Ad | undefined>(undefined);

  useEffect(() => {
    // Fetch user data when the component mounts
    const fetchData = async () => {
      try {
        const data = await getUserData();
        console.log("user data:",data)
        const plantdata = await getAllPlants(); // I get all plants here - this is throwing it back and idk why
        const ad = await getRandomAd();

        if (data.error) {
          setError(data.error);
          return;
        }

        setUserData(data);
        setPlantData(plantdata);
        setAd(ad);
        // setAd(undefined)
      } catch {
        console.log("error fetching data : ", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="font-poppins overflow-hidden">
      <div id="view" className="flex relative mr-1">
        <Sidebar userData={userData} />
        {/* Main content */}
        <aside className="h-screen w-[75%] lg:w-[70%] p-4 bg-white overflow-y-hidden justify-center">
          <PlantManual plants={plantData} userData={userData} ad={ad} />
        </aside>
  
        {/* Ad Section */}
        {!userData?.rolePaid && ad && (
          <aside className="h-screen w-[25%] lg:w-[20%] p-4 flex items-start">
            <div className="w-full ml-2"> {/* Adjusted spacing */}
              <AdComponent adItem={ad} />
            </div>
          </aside>
        )}
      </div>
    </div>
  );
  
  
}
