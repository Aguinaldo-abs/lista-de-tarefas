package br.com.bs.listadetarefas.user;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRespository extends JpaRepository<UserModel, UUID> {

    UserModel findByUserName(String userName);
    
}
