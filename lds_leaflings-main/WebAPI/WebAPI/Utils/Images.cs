using System.Globalization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Hosting;

namespace WebAPI.Utils
{
    public class Images
    {
        private readonly string _pathImages;
        private readonly string _urlBase;
        private readonly IWebHostEnvironment _webHostEnvironment;

        public Images(IWebHostEnvironment webHostEnvironment, string folderName = "images", string urlBase = "images/")
        {
            _webHostEnvironment = webHostEnvironment;

            // Configura o caminho base onde as imagens serão guardadas
            string webRootPath = _webHostEnvironment.WebRootPath;
            if (string.IsNullOrWhiteSpace(webRootPath))
            {
                webRootPath = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot");
            }

            // Cria dinamicamente as pastas necessárias
            EnsureDirectoryExists(webRootPath);

            // Define o caminho completo para a pasta de imagens
            _pathImages = Path.Combine(webRootPath, folderName);
            EnsureDirectoryExists(_pathImages);

            // Garante que a URL base termine com "/"
            _urlBase = urlBase.EndsWith("/") ? urlBase : urlBase + "/";
        }

        private void EnsureDirectoryExists(string path)
        {
            if (!Directory.Exists(path))
            {
                Directory.CreateDirectory(path);
            }
        }

        /// <summary>
        /// Verifica se o ficheiro é uma imagem válida.
        /// </summary>
        private bool IsImage(IFormFile file)
        {
            if (file == null || file.Length == 0)
                return false;

            var allowedExtensions = new[] { ".jpg", ".jpeg", ".png", ".gif", ".bmp" };
            var fileExtension = Path.GetExtension(file.FileName).ToLower();

            // Verifica a extensão e o MIME type
            return allowedExtensions.Contains(fileExtension) &&
                   file.ContentType.StartsWith("image/", StringComparison.OrdinalIgnoreCase);
        }

        /// <summary>
        /// Guarda uma imagem e retorna sua URL.
        /// </summary>
        public async Task<string?> SaveImageAsync(IFormFile image)
        {
            if (image == null)
                throw new ArgumentNullException(nameof(image));

            if (!IsImage(image))
                throw new InvalidOperationException("O arquivo enviado não é uma imagem válida.");

            string dateNow = DateTime.Now.ToString("yyyy-MM-dd", CultureInfo.InvariantCulture);
            string fileName = $"{dateNow}_{Guid.NewGuid()}{Path.GetExtension(image.FileName).ToLower()}";
            string filePath = Path.Combine(_pathImages, fileName);

            try
            {
                using (var stream = new FileStream(filePath, FileMode.Create))
                {
                    await image.CopyToAsync(stream);
                }

                return $"{_urlBase}{fileName}";
            }
            catch (Exception ex)
            {
                throw new IOException($"Erro ao salvar a imagem: {ex.Message}", ex);
            }
        }

        /// <summary>
        /// Elimina uma imagem.
        /// </summary>
        public bool DeleteImage(string fileName)
        {
            if (string.IsNullOrWhiteSpace(fileName))
                return false;

            // Remove o prefixo da URL base se presente
            fileName = fileName.Replace(_urlBase, "", StringComparison.OrdinalIgnoreCase);

            string fullPath = Path.Combine(_pathImages, fileName);
            try
            {
                if (File.Exists(fullPath))
                {
                    File.Delete(fullPath);
                    return true;
                }
                return false;
            }
            catch (Exception)
            {
                return false;
            }
        }

        /// <summary>
        /// Obtém o caminho físico completo de uma imagem a partir de sua URL.
        /// </summary>
        public string? GetPhysicalPath(string imageUrl)
        {
            if (string.IsNullOrWhiteSpace(imageUrl))
                return null;

            string fileName = imageUrl.Replace(_urlBase, "", StringComparison.OrdinalIgnoreCase);
            return Path.Combine(_pathImages, fileName);
        }
    }
}