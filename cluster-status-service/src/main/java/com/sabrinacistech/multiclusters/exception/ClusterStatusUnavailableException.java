package com.sabrinacistech.multiclusters.exception;

import com.sabrinacistech.multiclusters.enums.*;

public class ClusterStatusUnavailableException extends RuntimeException {

   public ClusterStatusUnavailableException(ClusterExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
    }
    public ClusterStatusUnavailableException(ClusterExceptionMessage exceptionMessage, String context) {
        super(exceptionMessage.getMessage() + ": " + context);
    }
}
