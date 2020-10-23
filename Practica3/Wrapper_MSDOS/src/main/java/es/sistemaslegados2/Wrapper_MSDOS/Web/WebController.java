package es.sistemaslegados2.Wrapper_MSDOS.Web;

import com.google.gson.Gson;
import es.sistemaslegados2.Wrapper_MSDOS.Models.Program;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {


    @GetMapping(path = "/greet")
    public ResponseEntity<String> greet(@RequestParam(name = "name", defaultValue = "World") String name) {
        Gson gson = new Gson();
        Program p = new Program(name, "tipo", "1a", "5");
        if (name.matches("^Error400.*$")){
            return new ResponseEntity<>(gson.toJson(p), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gson.toJson(p), HttpStatus.OK);
    }
}
