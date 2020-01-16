/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infra.conexao;

/**
 *
 * @author RHUAN
 */
public enum Schema {

    MYSQL_SISVENDASSIMPLES("jdbc:mysql://localhost/sisvendassimples?characterEncoding=UTF-8", "root", "root"),
    MYSQL_SISVENDAS("jdbc:mysql://localhost/sisvendas?characterEncoding=UTF-8","root","root"),
    DATA_BASE2("jdbc:mysql:ip:porta/schema2?characterEncoding=UTF-8", "root", "");

    private String url;
    private String usuario;
    private String senha;

    Schema(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;

    }

    public String getSenha() {
        return senha;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

}
