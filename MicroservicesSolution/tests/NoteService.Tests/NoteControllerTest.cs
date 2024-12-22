using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Moq;
using NoteService.Controllers;
using NoteService.Data;
using NoteService.Models;

namespace NoteService.Tests
{
    [TestClass]
    public class NoteControllerTests
    {
        private Mock<ApplicationDbContext> _mockContext;
        private Mock<IHttpClientFactory> _mockHttpClientFactory;
        private Mock<HttpClient> _mockHttpClient;
        private Mock<IConfiguration> _mockConfiguration;
        private NoteController _controller;

        [TestInitialize]
        public void Setup()
        {
            // Мокаем контекст базы данных
            _mockContext = new Mock<ApplicationDbContext>();
            _mockHttpClientFactory = new Mock<IHttpClientFactory>();
            _mockHttpClient = new Mock<HttpClient>();
            _mockConfiguration = new Mock<IConfiguration>();

            // Мокаем HttpClient
            _mockHttpClientFactory.Setup(m => m.CreateClient(It.IsAny<string>())).Returns(_mockHttpClient.Object);

            // Мокаем строку подключения
            _mockConfiguration.Setup(c => c.GetConnectionString("LoggerService")).Returns("http://fake-logger-service");

            // Создаем экземпляр контроллера
            _controller = new NoteController(_mockContext.Object, _mockHttpClient.Object, _mockConfiguration.Object);
        }

        [TestMethod]
        public void GetAll_ShouldReturnOkResult_WithListOfNotes()
        {
            // Arrange
           var mockNotes = new List<Note>
           {
                new Note("Title 1", "Description 1"),
                new Note("Title 2", "Description 2")
           }.AsQueryable();

            var mockDbSet = new Mock<DbSet<Note>>();
            mockDbSet.As<IQueryable<Note>>().Setup(m => m.Provider).Returns(mockNotes.Provider);
            mockDbSet.As<IQueryable<Note>>().Setup(m => m.Expression).Returns(mockNotes.Expression);
            mockDbSet.As<IQueryable<Note>>().Setup(m => m.ElementType).Returns(mockNotes.ElementType);
            mockDbSet.As<IQueryable<Note>>().Setup(m => m.GetEnumerator()).Returns(mockNotes.GetEnumerator());

            _mockContext.Setup(c => c.Notes).Returns(mockDbSet.Object);

            // Act
            var result = _controller.GetAll();

            // Assert
            var okResult = result as OkObjectResult;
            Assert.IsNotNull(okResult);
            Assert.AreEqual(200, okResult.StatusCode);

            var notes = okResult.Value as List<Note>;
            Assert.AreEqual(2, notes.Count);
        }

        [TestMethod]
        public async Task GetOne_ShouldReturnOkResult_WithNote_WhenNoteExists()
        {
            // Arrange
            var noteId = Guid.NewGuid();
            Note? mockNote = new Note("Title 1", "Description 1") { Id = noteId };

            var mockDbSet = new Mock<DbSet<Note>>();
            _mockContext.Setup(c => c.Notes.SingleOrDefault(It.IsAny<System.Linq.Expressions.Expression<Func<Note, bool>>>()))
                .Returns(mockNote);

            // Act
            var result = _controller.GetOne(noteId.ToString());

            // Assert
            var okResult = result as OkObjectResult;
            Assert.IsNotNull(okResult);
            Assert.AreEqual(200, okResult.StatusCode);

            var note = okResult.Value as Note;
            Assert.IsNotNull(note);
            Assert.AreEqual(noteId, note.Id);
        }

        [TestMethod]
        public async Task GetOne_ShouldReturnNotFound_WhenNoteDoesNotExist()
        {
            //Arrange
           var noteId = Guid.NewGuid();

            _mockContext.Setup(c => c.Notes.SingleOrDefault(It.IsAny<System.Linq.Expressions.Expression<System.Func<Note, bool>>>()))
                .Returns((Note)null);

            // Act
            var result = _controller.GetOne(noteId.ToString());

            // Assert
            var notFoundResult = result as NotFoundResult;
            Assert.IsNotNull(notFoundResult);
            Assert.AreEqual(404, notFoundResult.StatusCode);
        }

        [TestMethod]
        public async Task Create_ShouldReturnOkResult_WhenNoteIsCreated()
        {
            // Arrange
            var noteDto = new NoteDto("New Note", "Description of new note");

            var mockDbSet = new Mock<DbSet<Note>>();
            _mockContext.Setup(c => c.Notes.Add(It.IsAny<Note>())).Verifiable();
            _mockContext.Setup(c => c.SaveChanges()).Verifiable();

            // Act
            var result = await _controller.Create(noteDto);

            // Assert
            var okResult = result as OkObjectResult;
            Assert.IsNotNull(okResult);
            Assert.AreEqual(200, okResult.StatusCode);

            _mockContext.Verify(c => c.Notes.Add(It.IsAny<Note>()), Times.Once);
            _mockContext.Verify(c => c.SaveChanges(), Times.Once);
        }

        [TestMethod]
        public void Delete_ShouldReturnNoContent_WhenNoteIsDeleted()
        {
            // Arrange
            var noteId = Guid.NewGuid();
            var mockNote = new Note("Title 1", "Description 1") { Id = noteId };

            var mockDbSet = new Mock<DbSet<Note>>();
            _mockContext.Setup(c => c.Notes.SingleOrDefault(It.IsAny<System.Linq.Expressions.Expression<System.Func<Note, bool>>>()))
                .Returns(mockNote);
            _mockContext.Setup(c => c.Notes.Remove(It.IsAny<Note>())).Verifiable();
            _mockContext.Setup(c => c.SaveChanges()).Verifiable();

            // Act
            var result = _controller.Delete(noteId.ToString());

            // Assert
            var noContentResult = result as NoContentResult;
            Assert.IsNotNull(noContentResult);
            Assert.AreEqual(204, noContentResult.StatusCode);

            _mockContext.Verify(c => c.Notes.Remove(It.IsAny<Note>()), Times.Once);
            _mockContext.Verify(c => c.SaveChanges(), Times.Once);
        }

        [TestMethod]
        public void Delete_ShouldReturnNotFound_WhenNoteDoesNotExist()
        {
            // Arrange
            var noteId = Guid.NewGuid();

            _mockContext.Setup(c => c.Notes.SingleOrDefault(It.IsAny<System.Linq.Expressions.Expression<System.Func<Note, bool>>>()))
                .Returns((Note)null);

            // Act
            var result = _controller.Delete(noteId.ToString());

            // Assert
            var notFoundResult = result as NotFoundResult;
            Assert.IsNotNull(notFoundResult);
            Assert.AreEqual(404, notFoundResult.StatusCode);
        }
    }
}
