package com.naname.fileparser.web;

import com.naname.fileparser.Chapter;
import com.naname.fileparser.data.File;
import com.naname.fileparser.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/files")
public class FilesController {

    private final FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public String getIndex(Model model){
        model.addAttribute("files", fileService.loadAll());
        return "index";
    }

    @PostMapping
    public String handleFileUploading(@RequestParam(value = "file")MultipartFile file,
                                      RedirectAttributes redirectAttributes) throws IOException {
        if (file.getSize() == 0)
            throw new IOException("File is empty!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        StringBuilder content = new StringBuilder();
        while (reader.ready()){
            content.append(reader.readLine());
            content.append("\n");
        }

        String size = String.format("%.2f KB", (file.getSize()*1.0 / 1024));
        fileService.addFile(file.getOriginalFilename(), size, content.toString());
        redirectAttributes.addFlashAttribute("message", "Файл успешно загружен!");

        return "redirect:/files/" + fileService.getLastId() + "?name="+file.getOriginalFilename();
    }

    @GetMapping("/{id}")
    public String getFile(@PathVariable(name = "id") long id,
                          @RequestParam(name = "name") String fileName,
                          Model model){
        model.addAttribute("fileName", fileName);
        File file = fileService.loadFileById(id);
        String[] parts = file.getContent().split("\n");
        List<Chapter> chapters = new ArrayList<>();
        Chapter chapter;
        Map<Integer, String> numbers = new HashMap<>(){{
            put(1, "0");
        }};

        for (int i = 0; i < parts.length; i++) {
            StringBuilder tabs = new StringBuilder();
            int counter = 0;
            while (parts[i].charAt(0) == '#') {
                counter++;
                tabs.append("   ");
                parts[i] = parts[i].replaceFirst("#", "");
            }
            if (counter == 1){
                numbers.put(counter, String.valueOf(Integer.parseInt(numbers.get(counter))+1));
                numbers.entrySet().removeIf(e -> e.getKey() > 1);
            } else if (!numbers.containsKey(counter)){
                numbers.put(counter, numbers.get(counter - 1) + "." + 1);
            } else{
                String[] splitted = numbers.get(counter).split("\\.");
                splitted[splitted.length-1] = String.valueOf(Integer.parseInt(splitted[splitted.length-1])+1);
                numbers.put(counter, String.join(".", splitted));
            }
            if (counter > 0) {
                chapter = new Chapter(numbers.get(counter), parts[i], true, tabs.toString());
            } else{
                chapter = new Chapter("null", parts[i], false, tabs.toString());
            }
            chapters.add(chapter);
        }
        model.addAttribute("chapters", chapters);
        return "file";
    }
}
