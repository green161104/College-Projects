using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Data;
using WebAPI.Entity;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.services
{
    public class AuthServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly IConfiguration _configuration;
        private readonly Cryptography _cryptography;
        private readonly AuthService _authService;
        private readonly WebApiContext _webApiContext;

        public AuthServiceTests()
        {
            var webApiContext = new WebApiContext();

            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _cryptography = new Cryptography(_configuration);
            _authService = new AuthService(_context, _configuration, _cryptography);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        [Fact]
        public async void AuthenticateAsync_ValidAdminCredentials_ReturnsValidToken()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "admin@admin.com",
                Password = "adminpassword"
            };

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.NotNull(token);
            var tokenHandler = new JwtSecurityTokenHandler();
            var jwtToken = tokenHandler.ReadJwtToken(token);

            // Verify claims
            var roleClaim = jwtToken.Claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);
            Assert.NotNull(roleClaim);
            Assert.Equal("admin", roleClaim.Value);

            // Verify token structure
            Assert.Equal(_configuration["Jwt:Issuer"], jwtToken.Issuer);
            //Assert.Equal(_configuration["Jwt:Audience"], jwtToken.Audiences);
            Assert.True(jwtToken.ValidTo > DateTime.Now);
        }

        [Fact]
        public async void AuthenticateAsync_ValidUserCredentials_ReturnsValidToken()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "user@user.com",
                Password = "userpassword"
            };

            // First add a regular user to the context
            var hashedEmail = _cryptography.HashString(loginModel.Email);
            var hashedPassword = _cryptography.HashString(loginModel.Password);

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.NotNull(token);
            var tokenHandler = new JwtSecurityTokenHandler();
            var jwtToken = tokenHandler.ReadJwtToken(token);

            // Verify claims
            var roleClaim = jwtToken.Claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);
            Assert.NotNull(roleClaim);
            Assert.Equal("user", roleClaim.Value);
        }

        [Fact]
        public async void AuthenticateAsync_InvalidCredentials_ReturnsNull()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "wrong@email.com",
                Password = "wrongpassword"
            };

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.Null(token);
        }

        [Fact]
        public async void AuthenticateAsync_EmptyCredentials_ReturnsNull()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "",
                Password = ""
            };

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.Null(token);
        }

        //[Fact]
        //public void GenerateToken_ValidClaims_ReturnsValidToken()
        //{
        //    // Arrange
        //    var claims = new List<Claim>
        //    {
        //        new Claim(ClaimTypes.NameIdentifier, "1"),
        //        new Claim(ClaimTypes.Role, "admin")
        //    };

        //    // Use reflection to access private method
        //    var methodInfo = typeof(AuthService).GetMethod("GenerateToken",
        //        System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.Instance);

        //    // Act
        //    var token = (string)methodInfo.Invoke(_authService, new object[] { claims });

        //    // Assert
        //    Assert.NotNull(token);
        //    var tokenHandler = new JwtSecurityTokenHandler();
        //    var jwtToken = tokenHandler.ReadJwtToken(token);

        //    // Verify token properties
        //    Assert.Equal(_configuration["Jwt:Issuer"], jwtToken.Issuer);
        //    //Assert.Equal(_configuration["Jwt:Audience"], jwtToken.Audience);
        //    Assert.True(jwtToken.ValidTo > DateTime.Now);

        //    // Verify claims
        //    var nameClaim = jwtToken.Claims.FirstOrDefault(c => c.Type == ClaimTypes.NameIdentifier);
        //    var roleClaim = jwtToken.Claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);
        //    Assert.Equal("1", nameClaim?.Value);
        //    Assert.Equal("admin", roleClaim?.Value);
        //}

        [Fact]
        public async void AuthenticateAsync_VerifyTokenExpiration()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "admin@admin.com",
                Password = "adminpassword"
            };

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.NotNull(token);
            var tokenHandler = new JwtSecurityTokenHandler();
            var jwtToken = tokenHandler.ReadJwtToken(token);

            // Verify expiration time
            Assert.True(jwtToken.ValidTo > DateTime.Now);
            Assert.True(jwtToken.ValidTo <= DateTime.Now.AddMinutes(61)); // Should be around 60 minutes
        }

        [Fact]
        public async void AuthenticateAsync_ValidatesTokenSignature()
        {
            // Arrange
            var loginModel = new LoginModel
            {
                Email = "admin@admin.com",
                Password = "adminpassword"
            };

            // Act
            var (token, userId, rolePaid) = await _authService.AuthenticateAsync(loginModel);

            // Assert
            Assert.NotNull(token);

            var tokenHandler = new JwtSecurityTokenHandler();
            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));
            var validationParameters = new TokenValidationParameters
            {
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = key,
                ValidateIssuer = true,
                ValidIssuer = _configuration["Jwt:Issuer"],
                ValidateAudience = true,
                ValidAudience = _configuration["Jwt:Audience"],
                ValidateLifetime = true,
                ClockSkew = TimeSpan.Zero
            };

            // This will throw an exception if the token is invalid
            var principal = tokenHandler.ValidateToken(token, validationParameters, out var validatedToken);
            Assert.NotNull(principal);
            Assert.NotNull(validatedToken);
        }
    }
}
