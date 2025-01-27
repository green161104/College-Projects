using System.Text;
using System.Security.Cryptography;
using static System.Net.Mime.MediaTypeNames;
using System.Runtime.Intrinsics.Arm;

namespace WebAPI.Utils
{
    public class Cryptography
    {

        private readonly string _salt;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="configuration"></param>
        public Cryptography(IConfiguration configuration)
        {
            _salt = configuration["Security:Salt"];
        }

        // Hash para a senha
        /// <summary>
        /// 
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public string HashString(string input)
        {
            if (string.IsNullOrEmpty(input) || string.IsNullOrEmpty(_salt))
            {
                return string.Empty;
            }

            // Uses SHA256 to create the hash
            using (var sha256 = SHA256.Create())
            {
                // Convert the string to a byte array first, to be processed
                byte[] textBytes = System.Text.Encoding.UTF8.GetBytes(input + _salt);
                byte[] hashBytes = sha256.ComputeHash(textBytes);

                // Convert back to a string, removing the '-' that BitConverter adds
                string hash = BitConverter
                    .ToString(hashBytes)
                    .Replace("-", String.Empty);

                return hash;
            }
        }
    }
}
