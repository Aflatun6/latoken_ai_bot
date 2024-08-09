package com.latoken.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Component
//@AllArgsConstructor
@RequiredArgsConstructor
public class EmbeddingComponent {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;

    @Value("${rag.document.maxSegmentSizeInChars}")
    private int maxSegmentSizeInChars;
    @Value("${rag.document.maxOverlapSizeInChars}")
    private int maxOverlapSizeInChars;


    public void loadSingleDocument() {
        String currentDir = System.getProperty("user.dir");
        String fileName = "/latoken.pdf";
        Document document = loadDocument(currentDir + fileName, new ApachePdfBoxDocumentParser());

        maxSegmentSizeInChars = 300;
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        embeddingStoreIngestor.ingest(document);
    }

    public void loadLatokenDocument() {
        String currentDir = System.getProperty("user.dir");
        String fileName = "/latoken.pdf";
        Document document = loadDocument(currentDir + fileName, new ApachePdfBoxDocumentParser());

        maxSegmentSizeInChars = 300;
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(maxSegmentSizeInChars, maxOverlapSizeInChars))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        embeddingStoreIngestor.ingest(document);
    }
}