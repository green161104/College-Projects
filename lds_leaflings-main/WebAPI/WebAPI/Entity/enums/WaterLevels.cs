using Newtonsoft.Json.Converters;

namespace WebAPI.Entity.enums
{
    [Newtonsoft.Json.JsonConverter(typeof(StringEnumConverter))]
    public enum WaterLevels
    {
        Low,
        Medium,
        High
    }
}
