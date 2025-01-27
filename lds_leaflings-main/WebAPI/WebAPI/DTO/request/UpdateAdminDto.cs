using System.ComponentModel.DataAnnotations;

namespace WebAPI.DTO.request
{
    public class UpdateAdminDto
    {
        public required string Username { get; set; }

        public required string Contact { get; set; }
    }
}
