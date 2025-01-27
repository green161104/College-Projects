using Newtonsoft.Json.Converters;

namespace WebAPI.Entity.enums
{
    [Newtonsoft.Json.JsonConverter(typeof(StringEnumConverter))]
    public enum TypesPlants
    {
        Decorative,
        Medicinal,
        Fruit,
        Vegetable,
        Flower,
        Succulent
    }
}
