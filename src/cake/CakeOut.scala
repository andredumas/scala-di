package cake

trait UserRepositoryComponent {
  val userRepository: UserRepository
  
  class UserRepository {
    def findUser = println("Find User")
  }
}

trait UserServiceComponent { 
  this: UserRepositoryComponent =>
  val userService: UserService
  
  class UserService {
	def authenticate = {
	  userRepository.findUser
	  println("Authenticated user")
	}
  }
}

trait ComplicatedDependencies {
  this: UserRepositoryComponent 
  with  UserServiceComponent =>

  val complicatedDependencies: ComplicatedDependencies 
     
  class ComplicatedDependencies {
	  def anotherMethod = {
			  println("Complicated")
			  userRepository.findUser
			  userService.authenticate
	  }
  }
}

// Trait defining all dependencies and app context, config data for the app
trait AppContextTraits 
	extends UserServiceComponent
		with UserRepositoryComponent
		with ComplicatedDependencies

// Real app config defining all required dependencies and config
trait AppContext extends AppContextTraits {
  val userRepository: UserRepository = new UserRepository
  val userService: UserService = new UserService
  val complicatedDependencies: ComplicatedDependencies = new ComplicatedDependencies
}

trait TestAppContext 
	extends AppContextTraits
{
  val userRepository: UserRepository = new UserRepository {
    override def findUser = println("Mocked Find User")
  }
  val userService: UserService = new UserService {
    override def authenticate = println("Mocked Authenticate!!")
  }
  
  val complicatedDependencies: ComplicatedDependencies = new ComplicatedDependencies {
    override def anotherMethod = println("Mocked AnotherMethod!!")
  }
  
}


/**
 * Trait controller where dependencies injected
 */
trait CakeOutController extends App {
  this: AppContextTraits => // Injected dependencies
    
  def action = {
    userRepository.findUser
    userService.authenticate
    complicatedDependencies.anotherMethod
  }
  
  // Calling 
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
object CakeOut extends AppContext with CakeOutController

/**
 * Same controller trait, mocked dependencies 
 */
object CakeOutTest extends TestAppContext with CakeOutController
