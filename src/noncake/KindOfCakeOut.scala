package noncake

class UserRepository {
  def findUser = println("Find User")
}

class UserService {
  context: AppContextTraits => // Injected dependencies
    
  def authenticate = {
	userRepository.findUser
	println("Authenticated user")
  }
}

trait ComplicatedDependencies {
   context: AppContextTraits => // Injected dependencies
     
   def anotherMethod = {
     println("Complicated")
     userRepository.findUser
     userService.authenticate
   }
}

// Trait defining all dependencies and app context, config data for the app
trait AppContextTraits {
  val userRepository: UserRepository
  val userService: UserService
}

// Real app config defining all required dependencies and config
trait AppContext extends AppContextTraits {
  val userRepository: UserRepository = new UserRepository
  //val userService: UserService = new UserService
}

trait TestAppContext 
	extends AppContextTraits
{
  val userRepository: UserRepository = new UserRepository {
    override def findUser = println("Mocked Find User")
  }
//  val userService: UserService = new UserService {
//    override def authenticate = println("Mocked Authenticate!!")
//  }
}

/**
 * Simulates the actual controller play can reference
 */
object KindOfCakeOut extends /*AppContext with */KindOfCakeOutController  

/**
 * Trait controller where dependencies injected
 */
trait KindOfCakeOutController extends App {
  /*context: AppContextTraits => // Injected dependencies
    
  def test = {
    context.userRepository.findUser
    context.userService.authenticate
  }
  
  test*/
}