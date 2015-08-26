package br.com.androidpro.goldark.rest;

import java.util.List;

/**
 * @author Thiago Pagonha
 * @version 26/08/15.
 */
public class PageableResult<T> {
    List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
