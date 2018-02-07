package com.ingic.waterapp.interfaces;

/**
 * Created on 7/17/2017.
 */

public interface webServiceResponseLisener<T> {
    public void ResponseSuccess(T result, String tag, String message);
    public void  ResponseFailure(String tag);
}
