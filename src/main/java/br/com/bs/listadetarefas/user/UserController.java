package br.com.bs.listadetarefas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRespository iUserRespository;

    @PostMapping("/")
    public ResponseEntity createUser(@RequestBody UserModel userModel) {
        var user = this.iUserRespository.findByUserName(userModel.getUserName());

        if(null != user ) {
            System.out.println("User name ja cadastrador");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);

         var userCreated = this.iUserRespository.save(userModel);
         return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);

    }
}