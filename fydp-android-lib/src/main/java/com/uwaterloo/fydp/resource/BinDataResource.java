package com.uwaterloo.fydp.resource;

import com.google.gson.reflect.TypeToken;
import com.uwaterloo.fydp.api.ApplicationException;
import com.uwaterloo.fydp.api.Constants;
import com.uwaterloo.fydp.domain.BinData;
import com.uwaterloo.fydp.structure.Envelope;
import com.uwaterloo.fydp.util.HttpRequest;

import java.util.List;

/**
 * Created by Anthony on 15-02-10.
 */
public class BinDataResource extends RestResource {

    public static String URL = Constants.BASEURL + "/bin_data";

    public static List<BinData> getBinData(int binSystemId, int binId) throws ApplicationException {
        String requestUrl = URL + "/data" + "?binSystemId=" + binSystemId +"&binId=" + binId;

        String response;

        try {
            response = HttpRequest.sendGet(requestUrl);
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }

        Envelope<List<BinData>> envelope = gson.fromJson(response, new TypeToken<Envelope<List<BinData>>>(){}.getType());
        return envelope.getResult();
    }
}
