using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WebAPI.Controllers;
using WebAPI.Data;
using WebAPI.DTO.request;
using WebAPI.Entity;
using WebAPI.Services;
using WebAPI.Utils;

namespace WebAPITests.services
{
    public class PaymentServiceTests : IDisposable
    {
        private readonly DataContext _context;
        private readonly PaymentService _PaymentService;
        private readonly UserService _userService;
        private readonly Cryptography _cryptography;
        private readonly IConfiguration _configuration;

        /** TO TEST 
         * GetPaymentById - does it return valid objects for valid data (?) (yes), does it return empty lists when it should (?) ( yes )  , what happens when I give it an ID that is not valid(?) ( it returns null ) - done 
         * GetAllPaymentsAsync - 
         * GetPaymentsByUserId -    
         * AddNewPayment - 
         * UpdatePayment - 
         * DeletePayment - 
         * GetPaypalAccessToken - 
         * getPaypalUrl´- 
         */

        public PaymentServiceTests()
        {
            var webApiContext = new WebApiContext();
            _context = webApiContext.getWebApiContext();
            _configuration = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build();
            _PaymentService = new PaymentService ( _configuration, _context);
            _userService = new UserService(_context, _cryptography);
        }

        public void Dispose()
        {
            _context.Database.EnsureDeleted();
            _context.Dispose();
        }

        [Fact]
        public async void GetAllPaymentsAsyncShouldNotBeEmpty() // isn't empty
        {
            var payments = await _PaymentService.GetAllPaymentsAsync();

            Assert.NotEmpty(payments);
        }

        [Fact]
        public async void GetPaymentsByUserIdAsyncShouldBeCorrectForValidID()
        {
            var expectedPaymentID = 1; // this is is valid, so it should 
            var paymentID = await _PaymentService.GetPaymentById(expectedPaymentID);

            Assert.NotNull(paymentID); // return null
        }

        [Fact]
        public async void GetPaymentsByUserIdAsyncShouldBeCorrectForInvalidID()
        {
            var expectedPaymentID = 10; // this is is invalid, so it should 
            var paymentID = await _PaymentService.GetPaymentById(expectedPaymentID);

            Assert.Null(paymentID); // return null
        }

        [Fact]
        public async void GetPaymentsByUserIdShouldNotBeNull()
        {
            var UserID = 4;
            var user = await _PaymentService.GetPaymentsByUserId(UserID);
            Assert.NotNull(user);
        }

        [Fact]
        public async void GetPaymentsByUserIdInvalidShouldBeNull()
        {
            var UserID = 1;
            var user = await _PaymentService.GetPaymentsByUserId(UserID);
            Assert.Empty(user);
        }

       
        [Fact]
        public async void AddNewPaymentValidShouldBeSuccesfull()
        {
            var newPaymentl = new PaymentRequestDto
            {
                Title = "hallo",
                UserId = 3,
                CreationDate = DateTime.Now,
            };

            var payment = await _PaymentService.AddNewPayment(newPaymentl);
            Assert.NotNull(payment);
        }


        [Fact]
        public async void AddNewPaymentInvalidShouldBeUnsuccesfull()
        {
            var newPayment = new PaymentRequestDto
            {
                Title = "hallo",
                UserId = -230,
                CreationDate = DateTime.Now,
            };

           var payment = await _PaymentService.AddNewPayment(newPayment);
            Assert.Null(payment);
        }

        [Fact]
        public async void UpdatePaymentShouldBeSuccesfull()
        {
            int id = 1;
            var paymentUpdateInfo = new PaymentRequestDto
            {
                Title = "halloo",
                UserId = 3,
                CreationDate = DateTime.Now,
            };
            var payment = await _PaymentService.UpdatePayment(id, paymentUpdateInfo);
            Assert.True(payment);
        }

        [Fact]
        public async void UpdatePaymentForInvalidDataShouldBeUnsuccesfull()
        {
            int id = 100000;
            var paymentUpdateInfo = new PaymentRequestDto
            {
                Title = "halloo",
                UserId = 3453,
                CreationDate = DateTime.Now,
            };
            var payment = await _PaymentService.UpdatePayment(id, paymentUpdateInfo);
            Assert.False(payment);
        }


        [Fact]
        public async void DeletePaymentForInvalidDataShouldBeSuccesfull()
        {
            int id = 1;
            
            var paymentDeletus = await _PaymentService.DeletePayment(id);
            Assert.True(paymentDeletus);
        }

        [Fact]
        public async void DeletePaymentForInvalidDataShouldBeUnsuccesfull()
        {
            int id = 123232;

            var paymentDeletus = await _PaymentService.DeletePayment(id);
            Assert.False(paymentDeletus);
        }

        [Fact]
        public async void returnPaypalTokenShouldWork()
        {
            var token = await _PaymentService.GetPaypalAccessToken();
            Assert.NotNull(token);
        }


    }
}
