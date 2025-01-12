package entities;

import java.io.Serializable;

public class Produit implements Serializable{
	
	private int id;
	private String nom;
	private double prix;
	private String taille;
	private String couleur;
        private String img;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}
	public String getTaille() {
		return taille;
	}
	public void setTaille(String taille) {
		this.taille = taille;
	}
	public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Produit(int id,String nom, double prix, String taille, String couleur) {
		this.id=id;
		this.nom = nom;
		this.prix = prix;
		this.taille = taille;
		this.couleur = couleur;
	}

    public Produit(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
	
        
	

}
