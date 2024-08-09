package com.latoken.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContentIngestor {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;

    // Method to download file
    public void downloadFile(String fileUrl, String destinationFile) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        log.info("downloadingnggggggggggggggggggggggggggggg");
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = httpConn.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
                log.info("tryinggggggggggggg");
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
        log.info("finisheed");
        httpConn.disconnect();
    }

    // Pseudo-method to transcribe video (implement with actual service)
    public String transcribeVideo(String videoUrl) {
        // Implement transcription logic here, e.g., using Google Cloud Speech-to-Text API
        return "Transcribed text from video";
    }

    // Main method to process and ingest content
    public void ingestContent(String fileUrl, String videoUrl) throws IOException {
        // Download and load the slides PDF
        String destinationFile = "latokenVid.pdf";
        downloadFile(fileUrl, destinationFile);

        String currentDir = System.getProperty("user.dir");
        String fileName = "/latokenVid.pdf";
        Document document = loadDocument(currentDir + fileName, new ApachePdfBoxDocumentParser());

        // Transcribe the video
        String transcribedText = transcribeVideo(videoUrl);
        Document videoDocument = new Document(transcribedText);

        // Ingest the slides
        int maxSegmentSizeInChars = 300;
        int maxOverlapSizeInChars = 10;
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        embeddingStoreIngestor.ingest(document);

        // Ingest the video transcription
        embeddingStoreIngestor.ingest(videoDocument);
    }
}
