using Newtonsoft.Json;
using Newtonsoft.Json.Converters;

namespace WebAPI.Entity.enums
{
    [JsonConverter(typeof(StringEnumConverter))]
    public enum ExperienceLevels
    {
        Beginner,
        intermediate,
        Expert
    }
}
