package com.qarari.app.integration;

import com.qarari.app.model.Offer;

/** Secure extension point for future OCR/AI extraction from offer images/PDFs. */
public interface AiOfferExtractor {
    Offer extract(byte[] documentBytes, String mimeType) throws Exception;
}
