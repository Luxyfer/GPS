package com.isec.proto.heliumblue2;

import java.net.URL;

/**
 * Created by Rafael on 21/11/2015.
 */
public class Serie {

    private String titulo;
    private String desc;
    private URL url_imagem;
    private int id;

    public Serie(String tit, int id, URL address, String desc) {
        titulo = tit;
        this.id = id;
        url_imagem = address;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public URL getUrl_imagem() {
        return url_imagem;
    }

    public String getDesc() {
        return desc;
    }
}
