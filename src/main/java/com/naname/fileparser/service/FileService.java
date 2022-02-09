package com.naname.fileparser.service;

import com.naname.fileparser.data.File;
import com.naname.fileparser.data.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void addFile(String name, String size, String content){
        fileRepository.insertFile(name, size, content);
    }

    public int getLastId(){
        return fileRepository.getLastId();
    }

    public List<File> loadAll(){
        return fileRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public File loadFileById(long id){
        return fileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("File with id " + id + " not found!"));
    }
}
