using Microsoft.AspNetCore.Mvc;
using Models;


namespace Controllers;



[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{
    [HttpGet(Name = "GetUser")]
    public IActionResult GetTestUser(int userId)
    {
        var database = new DBMock();
        var userModel = database.GetUserById(userId);
        
        return Ok(userModel);
    }
}