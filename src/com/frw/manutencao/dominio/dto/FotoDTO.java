package com.frw.manutencao.dominio.dto;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class FotoDTO {
	
    private String foto;
	private String nome;

	public byte[] getBytesFoto() {
		return foto != null ? Base64.decodeBase64(foto) : null;
	}

	public String getNome() {
		return nome;
	}

	public void setBytesFotoToString(byte[] bytesFoto) throws UnsupportedEncodingException {
		this.foto = bytesFoto != null ? new String(Base64.encodeBase64(bytesFoto),"UTF-8") : null;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
}
