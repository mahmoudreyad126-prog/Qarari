package com.qarari.app.integration;

import com.qarari.app.model.Offer;
import java.util.List;

/** Cloud sync contract. Implement behind authenticated HTTPS; never embed secrets in the APK. */
public interface SyncGateway {
    void pushOffers(List<Offer> offers) throws Exception;
    List<Offer> pullOffers() throws Exception;
}
