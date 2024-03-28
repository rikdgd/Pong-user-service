using Microsoft.AspNetCore.Mvc;
using Pong_user_service.Models;



namespace Pong_user_service.Controllers;

[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    [HttpGet(Name = "GetUser")]
    public IActionResult GetTestUser(int userId)
    {
        var database = new DbMock();
        var userModel = database.GetUserById(userId);
        
        return Ok(userModel);
    }
}
