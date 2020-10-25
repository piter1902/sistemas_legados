package es.sistemaslegados2.Wrapper_MSDOS.Web;

import com.google.gson.Gson;
import es.sistemaslegados2.Wrapper_MSDOS.Models.Program;
import es.sistemaslegados2.Wrapper_MSDOS.Repository.MSDOSWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Autowired
    private MSDOSWrapper wrapper;

    @GetMapping(path = "/greet")
    public ResponseEntity<String> greet(@RequestParam(name = "name", defaultValue = "World") String name) {
        Gson gson = new Gson();
        Program p = new Program(name, "tipo", "1a", "5");
        if (name.matches("^Error400.*$")){
            return new ResponseEntity<>(gson.toJson(p), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gson.toJson(p), HttpStatus.OK);
    }


    @GetMapping("/filterByName")
    public ResponseEntity<String> filterByName(@RequestParam(name = "name") String name) {
        return new ResponseEntity<>("Filtrando para name: " + name, HttpStatus.OK);
    }

    @GetMapping("/filterByTape")
    public ResponseEntity<String> filterByTape(@RequestParam(name = "tape") String tape) {
        return new ResponseEntity<>("Filtrando para tape: " + tape, HttpStatus.OK);
    }

    @GetMapping("/getRecords")
    public ResponseEntity<String> getRecords() {
        int numRecords = wrapper.getRecordNumber();
        return new ResponseEntity<>(String.format("Hay %d registros", numRecords), HttpStatus.OK);
    }
}
