package com.example.webApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/demo")
public class MainController {
	@Autowired
	
	private UserRepository userRepository;
	
	@PostMapping(path="/add")
	public @ResponseBody String addNewUser(@RequestParam String name, @RequestParam String email) {
		if(name == null || email == null || name == "" || email == "")
			return "Invalid user. Can't save.";
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		userRepository.save(user);
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping(path="/id")
	public @ResponseBody User getUserById(@RequestParam Integer id){
		Iterable<User> itr = userRepository.findAll();
		for(User user : itr) {
			if(user.getId() == id) {
				return user;
			}
		}
		System.out.println("Can't get as User is not registered.");
		return null;
	}
	
	@PostMapping(path="/delete")
	public @ResponseBody String deleteUser(@RequestParam Integer id) {
		Iterable<User> itr = userRepository.findAll();
		for(User user : itr) {
			if(user.getId() == id) {
				userRepository.deleteById(id);
				return "Deleted.";
			}
		}
		return "Can't delete as User is not registered.";
	}
	
	@PostMapping(path="/update")
	public @ResponseBody String updateUser(@RequestParam Integer id, @RequestParam String name,@RequestParam String email) {
		Iterable<User> itr = userRepository.findAll();
		for(User user : itr) {
			if(user.getId() == id) {
				user.setName(name);
				user.setEmail(email);
				userRepository.save(user);
				return "Updated";
			}
		}
		return "Can't update as User is not registered.";
	}
}
