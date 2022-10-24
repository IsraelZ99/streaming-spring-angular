package com.demo.springbootstreaming.springcontent;

import org.springframework.content.fs.config.EnableFilesystemStores;
import org.springframework.content.fs.io.FileSystemResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
@EnableFilesystemStores
public class ApplicationConfig {

    @Bean
    File filesSystemRoot(){
        try {
            return Files.createTempDirectory("").toFile();
        } catch (IOException ioe){}
        return null;
    }

    public FileSystemResourceLoader fileSystemResourceLoader(){
        return new FileSystemResourceLoader(filesSystemRoot().getAbsolutePath());
    }

}
