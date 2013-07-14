package noncake

class UserRepository {
  def findUser = println("Find User")
}

class UserService(
    userRepository: UserRepository) {
  def authenticate = {
  	userRepository.findUser
  	println("Authenticated user")
  }
}

class ComplicatedDependencies(
    userRepository: UserRepository, 
    userService: UserService) {
  
  def anotherMethod = {
    println("Complicated")
    userRepository.findUser
    userService.authenticate
  }
}

// Trait defining all dependencies and app context, config data for the app
trait ContextTraits {
  val userRepository: UserRepository
  val userService: UserService
}

// Real app config defining all required dependencies and config
trait Context extends ContextTraits {
  val userRepository: UserRepository = new UserRepository
  val userService: UserService = new UserService(userRepository)
}

trait TestContext extends Context
{
  override val userRepository: UserRepository = new UserRepository {
    override def findUser = println("Mocked Find User")
  }
  override val userService: UserService = new UserService(userRepository) {
    override def authenticate = println("Mocked Authenticate!!")
  }
}

/**
 * Trait controller where dependencies injected
 */
trait KindOfCakeOutController extends App {
  context: ContextTraits => // Injected dependencies
    
  def action = {
    userRepository.findUser
    userService.authenticate
  }
  
  action
}

/*
 * Couple of Apps to test concepts of Controller with real dependencies and same controller 
 * with mocked dependencies
 */

/**
 * Simulates the actual controller play can reference. NOTE: The trait include order. Normally logically it
 * would be the other way round, but since I am calling the action in the CakeOutController, the 
 * dependencies need to have been initialised first by the AppContext. 
 */
object KindOfCakeOut extends Context with KindOfCakeOutController  

/**
 * Same controller trait, mocked dependencies 
 */
object KindOfCakeOutTest extends TestContext with KindOfCakeOutController  