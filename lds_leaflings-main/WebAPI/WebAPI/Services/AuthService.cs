using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using WebAPI.Entity;
using WebAPI.Data;
using Microsoft.EntityFrameworkCore;
using WebAPI.Utils; // Para usar o contexto do EF Core

public class AuthService
{
    private readonly DataContext _context;
    private readonly IConfiguration _configuration;
    private readonly Cryptography _cryptography;

    public AuthService(DataContext context, IConfiguration configuration, Cryptography cryptography)
    {
        _context = context;
        _configuration = configuration;
        _cryptography = cryptography;
    }

    /// <summary>
    /// Validates a JWT token and retrieves the claims.
    /// </summary>
    /// <param name="token">The JWT token to validate.</param>
    /// <returns>A ClaimsPrincipal if valid, otherwise null.</returns>
    public ClaimsPrincipal? ValidateToken(string token)
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        var key = Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]);

        try
        {
            var claimsPrincipal = tokenHandler.ValidateToken(token, new TokenValidationParameters
            {
                ValidateIssuer = true,
                ValidateAudience = true,
                ValidateLifetime = true,
                ValidateIssuerSigningKey = true,
                ValidIssuer = _configuration["Jwt:Issuer"],
                ValidAudience = _configuration["Jwt:Audience"],
                IssuerSigningKey = new SymmetricSecurityKey(key)
            }, out _);

            return claimsPrincipal;
        }
        catch
        {
            return null; // Return null if the token is invalid or expired
        }
    }


    /// <summary>
    /// Authenticates a user based on the provided login model and returns a JWT token if successful.
    /// </summary>
    /// <param name="model">The login model containing user credentials (email and password).</param>
    /// <returns>A JWT token as a string if authentication is successful; otherwise, null.</returns>
    public async Task<(string? Token, int? UserId, bool? RolePaid)> AuthenticateAsync(LoginModel model)
    {
        // Hash the email and password from the login model for secure comparison
        var hashedEmail = _cryptography.HashString(model.Email);
        var hashedPassword = _cryptography.HashString(model.Password);

        // Search the 'Persons' table for a user with the given hashed email and password
        var person = await _context.Person // Procura na tabela 'Persons' para verificar `User` e `Admin`
            .FirstOrDefaultAsync(p => p.Email == hashedEmail && p.Password == hashedPassword);

        if (person != null)
        {
            // Initialize the claims list
            var claims = new List<Claim>
        {
            // Add the user's ID as a claim
            new Claim(ClaimTypes.NameIdentifier, person.Id.ToString()),
            // Add the user's role as a claim, determining if the user is an admin or a regular user
            new Claim(ClaimTypes.Role, person.Role == 1 ? "admin" : "user")
        };

            bool? rolePaid = null;

            // Check if the person is a User, and if so, add the `rolePaid` claim
            if (person is User user) // Se for do tipo User, inclui o rolePaid
            {
                claims.Add(new Claim("rolePaid", user.RolePaid.ToString()));
                rolePaid = user.RolePaid; // Set rolePaid from the User entity
            }

            // Return the generated token along with the user ID and rolePaid
            return (GenerateToken(claims), person.Id, rolePaid);
        }

        return (null, null, null);
    }



    /// <summary>
    /// Generates a JSON Web Token (JWT) for the given claims.
    /// </summary>
    /// <param name="claims">The claims to include in the token.</param>
    /// <returns>A string representing the generated JWT.</returns>
    public string GenerateToken(IEnumerable<Claim> claims)
    {
        // Create a symmetric security key using the secret key from configuration
        var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));

        // Create signing credentials using the security key and the HMAC SHA-256 algorithm
        var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        var token = new JwtSecurityToken(
            issuer: _configuration["Jwt:Issuer"],
            audience: _configuration["Jwt:Audience"],
            claims: claims,
            expires: DateTime.Now.AddMinutes(60),
            signingCredentials: creds
        );

        return new JwtSecurityTokenHandler().WriteToken(token);
    }
}
