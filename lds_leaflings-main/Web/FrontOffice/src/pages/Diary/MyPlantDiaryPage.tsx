import { useEffect, useState } from "react";
import { Diary } from "../../models/diary-model";
import { Log } from "../../models/log-model";
import { useParams } from "react-router-dom";
import { getDiaryByUserPlantId } from "../../services/http/diary/diary-service";
import { getLogsByDiaryId } from "../../services/http/diarylogs/diarylogs-service";
import Sidebar from "../../components/Reusables/Sidebar";
import DiaryComponent from "../../components/Diary/DiaryComponent";
import { User } from "../../models/user-model";
import { getUserData } from "../../services/http/user/user-service";

const MyPlantDiaryPage = () => {
  const { userplantId } = useParams<{ userplantId: string }>(); // Access the route parameter
  const { plantId } = useParams<{ plantId: string }>(); // Access the route parameter
  const [diary, setDiary] = useState<Diary>();
  const [logs, setLogs] = useState<Log[]>();
  const [userData, setUserData] = useState<User>();
  const [loading, setLoading] = useState(true);
  const [error] = useState<string | null>(null);

  const fetchDiaryData = async () => {
    try {
      const diaryData = await getDiaryByUserPlantId(Number(userplantId));
      setDiary(diaryData);

      const logsData = await getLogsByDiaryId(diaryData.id);
      setLogs(logsData);

      const userData = await getUserData();
      setUserData(userData);
    } catch (error) {
      console.error("Error fetching diary/logs:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDiaryData();
  }, []);

  if (loading) {
    return <main>Loading user data...</main>;
  }

  return (
    <body className="font-poppins overflow-hidden">
      <div id="view" className="flex h-screen">

        {/* Make sure the container takes full height */}
        <div className="flex w-full h-full">

          {/* Ensure this takes the full space */}
          <Sidebar userData={userData} />

          <aside className="h-[90%] w-[65%] lg:w-[85%] p-4 bg-white overflow-hidden">
            {/* Ensure the content area takes full height */}
            {diary && (
              <DiaryComponent
                diary={diary}
                logs={logs}
                plantId={Number(plantId)}
                onUpdate={fetchDiaryData}
              />
            )}
          </aside>

          {error && (
            <p className="text-center text-red-500 font-bold">{error}</p>
          )}
          
        </div>
      </div>
    </body>
  );
};

export default MyPlantDiaryPage;
