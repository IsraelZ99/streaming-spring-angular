package com.demo.springbootstreaming.controller;

import com.demo.springbootstreaming.pojo.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Duration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequestMapping("/api/stream")
@RestController
public class APIController {

    @GetMapping("/data")
    public ResponseEntity<StreamingResponseBody> streamData() {
        StreamingResponseBody responseBody = response -> {
            for (int iteration = 1; iteration <= 100; iteration++) {
                try {
                    Thread.sleep(10);
                    response.write(("Data stream line -" + iteration + "\n").getBytes());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN)
                .body(responseBody);
    }

    @GetMapping(value = "/data/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Object> streamDataFlux() {
        return Flux.interval(Duration.ofSeconds(1)).map(i -> "Data stream line - " + i);
    }

    @GetMapping("/json")
    public ResponseEntity<StreamingResponseBody> streamJson() {
        int maxRecords = 1000;
        StreamingResponseBody responseBody = response -> {
            for (int iteration = 1; iteration < maxRecords; iteration++) {
                Student st = new Student("Name" + iteration, iteration);
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(st) + "\n";
                response.write(jsonString.getBytes());
                response.flush();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(responseBody);
    }

    @GetMapping(value = "/json/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Student> streamJsonObjects() {
        return Flux.interval(Duration.ofSeconds(1)).map(i -> new Student("Name" + i, i.intValue()));
    }

    @GetMapping("/textFile")
    public ResponseEntity<StreamingResponseBody> streamContentAsFile() {
        StreamingResponseBody responseBody = response -> {
            for (int iteration = 1; iteration < 1000; iteration++) {
                response.write(("Data stream line - " + iteration + "\n").getBytes());
                response.flush();
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test_data.txt")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(responseBody);
    }

    @GetMapping("/pdfFile")
    public ResponseEntity<StreamingResponseBody> streamPdfFile() throws FileNotFoundException {
        String fileName = "DECSEF - EDB_Postgres_Advanced v1.pdf";
        File file = ResourceUtils.getFile("classpath:static/" + fileName);
        StreamingResponseBody responseBody = outputStream -> {
            Files.copy(file.toPath(), outputStream);
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Downloaded_" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(responseBody);
    }

    @GetMapping("/csv")
    public ResponseEntity<StreamingResponseBody> getCsvFile() {
        StreamingResponseBody stream = output -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write("name,rollNo" + "\n");
            for (int iteration = 1; iteration <= 100000; iteration++) {
                Student st = new Student("Name" + iteration, iteration);
                writer.write(st.getName() + "," + st.getRollNo() + "\n");
                writer.flush();
            }
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(stream);
    }

    @GetMapping("/zip")
    public ResponseEntity<StreamingResponseBody> getZipFileStream(){
        StreamingResponseBody stream = this::writeToStream;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    public void writeToStream(OutputStream os) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(os));
        ZipEntry entry = new ZipEntry("data.csv");
        zipOut.putNextEntry(entry);
        Writer writer = new BufferedWriter(new OutputStreamWriter(zipOut, Charset.forName("UTF-8").newEncoder()));
        for (int iteration = 1; iteration <= 10000000; iteration++) {
            Student st = new Student("Name" + iteration, iteration);
            writer.write(st.getName() + "," + st.getRollNo() + "\n");
            writer.flush();
        }
        if(writer != null){
            writer.flush();
            writer.close();
        }
    }
}
