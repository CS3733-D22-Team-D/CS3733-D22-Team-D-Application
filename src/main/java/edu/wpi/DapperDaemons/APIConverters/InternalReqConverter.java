package edu.wpi.DapperDaemons.APIConverters;

import edu.wpi.DapperDaemons.entities.requests.PatientTransportRequest;
import edu.wpi.cs3733.D22.teamB.api.Request;

public class InternalReqConverter extends Converter {

    public InternalReqConverter(){}

    public static PatientTransportRequest convert(Request internalRequest) {
        //return new PatientTransportRequest(internalRequest.)
        // TODO: Implement this
        return null;
    }

    public edu.wpi.DapperDaemons.entities.requests.Request.Priority parsePriority(int priority) {
        return null;
    }
}
