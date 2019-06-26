package dewittt.blog.controller;

import dewittt.blog.fileserver.domain.File;
import dewittt.blog.service.FileService;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*",maxAge = 3600)
@Controller
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("files",fileService.listFilesByPage(0,20));
        return "index";
    }

    @GetMapping("files/{pageIndex}/{pageSize}")
    @ResponseBody
    public List<File> listFilesByPage(@PathVariable int pageIndex,@PathVariable int pageSize){
        return fileService.listFilesByPage(pageIndex,pageSize);
    }

    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFile(@PathVariable String id){
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName=\""+file.get().getName()+"\"")
                    .header(HttpHeaders.CONTENT_TYPE,"application/octet-stream")
                    .header(HttpHeaders.CONTENT_LENGTH,file.get().getSize()+"")
                    .header("Connection","close")
                    .body(file.get().getContent().getData());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    @GetMapping("/view/{id}")
    @ResponseBody
    public ResponseEntity<Object> serveFileOnline(@PathVariable("id")String id){
        Optional<File> file = fileService.getFileById(id);
        if (file.isPresent()){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"fileName=\""+file.get().getName()+"\"")
                    .header(HttpHeaders.CONTENT_TYPE,file.get().getContentType())
                    .header(HttpHeaders.CONTENT_LENGTH,file.get().getSize()+"")
                    .header("Connection","close")
                    .body(file.get().getContent().getData());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not found");
        }
    }

    @GetMapping("/")
    public String handleFileUpload(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes){
        try {
            File f = new File(file.getOriginalFilename(),file.getContentType(),file.getSize(),new Binary(file.getBytes()));
            f.setMd5(MD5Util);
        }
    }

}
