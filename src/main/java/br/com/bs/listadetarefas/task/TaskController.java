package br.com.bs.listadetarefas.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bs.listadetarefas.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository iTaskRespository;

    @RequestMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {


        taskModel.setIdUser((UUID) request.getAttribute("idUser"));

        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / término deve ser maior que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio não pode ser depis da data de término");
            
        }
        var task = this.iTaskRespository.save(taskModel);

        return ResponseEntity.status(HttpStatus.OK).body(task);
    }


    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var task = this.iTaskRespository.findByIdUser((UUID) idUser);
        return task;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
      TaskModel task = this.iTaskRespository.findById(id).orElse(null);
      var idUser = request.getAttribute("idUser");

      if(null == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
      }

      if(task.getIdUser().equals(idUser)){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão para alterar essa tarefa");
      }


      Utils.copyNonNullProperties(taskModel, task);
      var taskUpdated = this.iTaskRespository.save(taskModel);

      return ResponseEntity.ok().body(this.iTaskRespository.save(taskUpdated));
    }

}
