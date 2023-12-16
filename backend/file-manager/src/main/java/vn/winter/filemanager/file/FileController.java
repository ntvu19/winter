package vn.winter.filemanager.file;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/file")
public class FileController {
    @GetMapping("/hello")
    String hello() {
        return "Hello, this is file management microservice!";
    }
}
