import { getFullImageUrl } from "../../services/http/plant/plant-service";

interface AdProps {
  adItem?: {
    adFile?: string | undefined;
  };
}

/**
 * AdComponent
 *
 * This component displays an advertisement image if available. If the ad image is not available, it shows a placeholder.
 * It also prompts the user to get a premium version for an ad-free experience.
 *
 * @param {Object} props - The properties passed to this component.
 * @param {Object} props.adItem - The advertisement data, which may contain an ad image.
 * @returns {JSX.Element} The ad component.
 */
const AdComponent: React.FC<AdProps> = ({ adItem }) => {
  return (
    <div className="ad-container p-4 rounded-lg shadow-lg bg-white flex flex-col items-center">

      {adItem?.adFile && adItem.adFile !== "" ? (
        // Advertisement Image
        <img
          src={getFullImageUrl(adItem.adFile)}
          alt={"Advertisement"}
          className="w-full h-full object-fit rounded-t-lg"
        />
      ) : (
        // Default Advertisement Image
        <img
          src="/default_ad_image.jpg"
          alt={"Advertisement"}
          className="w-full h-full object-fit rounded-t-lg"
        />
      )}

      <p className="text-sm text-gray-500 mt-2">
        <strong>Get Premium</strong> to enjoy an ad-free experience!
      </p>
    </div>
  );
};

export default AdComponent;
