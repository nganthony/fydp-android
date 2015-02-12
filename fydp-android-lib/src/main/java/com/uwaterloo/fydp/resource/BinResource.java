package com.uwaterloo.fydp.resource;

import com.google.gson.reflect.TypeToken;
import com.uwaterloo.fydp.api.ApplicationException;
import com.uwaterloo.fydp.api.Constants;
import com.uwaterloo.fydp.domain.Bin;
import com.uwaterloo.fydp.structure.Envelope;
import com.uwaterloo.fydp.util.HttpRequest;
import java.util.List;

/**
 * Created by Anthony on 15-02-10.
 */
public class BinResource extends RestResource {

    public static String URL = Constants.BASEURL + "/bin";

    public static List<Bin> getBinsBySystemId(int binSystemId) throws ApplicationException {
        String requestUrl = URL + "/list" + "?binSystemId=" + binSystemId;

        String response;

        try {
            response = HttpRequest.sendGet(requestUrl);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

        Envelope<List<Bin>> envelope = gson.fromJson(response, new TypeToken<Envelope<List<Bin>>>(){}.getType());
        return envelope.getResult();
    }
}
