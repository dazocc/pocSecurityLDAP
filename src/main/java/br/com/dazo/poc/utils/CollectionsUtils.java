package br.com.dazo.poc.utils;

import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtils {

    public static <T> List<T> montaLista(T ...compoeLista) {

        List<T> lista = new ArrayList<T>();

        if(!ObjectUtils.isEmpty(compoeLista)){
            for (T t : compoeLista) {
                lista.add(t);
            }
        }

        return lista;
    }
}
