package com.example.felipe.buracometro_v5.modelo;


import android.os.Parcel;
import android.os.Parcelable;

public class Buraco implements Parcelable {


    private int    id;
    private int    idBuraco;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String Data_Registro;
    private String latitude;
    private String longitude;
    private String identificador;
    private int	   qtdOcorrencia;
    private String statusBuraco;
    private String dataTampado;
    private int    qtdReabertos;


    //----------------------------------------------------------------------------------------
    //                                      CONSTRUTORES
    //----------------------------------------------------------------------------------------

    public Buraco()
    {

    }

    public Buraco(int id, int idBuraco, String rua, String bairro, String cidade, String estado, String Data_Registro, String latitude, String longitude, String identificador, String statusBuraco, String dataTampado, int qtdReabertos)
    {
        super();
        this.id = id;
        this.idBuraco = idBuraco;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.Data_Registro = Data_Registro;
        this.latitude = latitude;
        this.longitude = longitude;
        this.identificador = identificador;
        this.statusBuraco = statusBuraco;
        this.dataTampado = dataTampado;
        this.qtdReabertos = qtdReabertos;
    }

    //Construtor para tornar o Objeto Parcelable
    //Esta classe esta implementando a interface Parcelable, para transporte de objetos entre activities
    protected Buraco(Parcel in) {
        id              = in.readInt();
        idBuraco        = in.readInt();
        rua             = in.readString();
        bairro          = in.readString();
        cidade          = in.readString();
        estado          = in.readString();
        Data_Registro   = in.readString();
        latitude        = in.readString();
        longitude       = in.readString();
        identificador   = in.readString();
        statusBuraco    = in.readString();
        dataTampado     = in.readString();
        qtdReabertos    = in.readInt();

    }


    //----------------------------------------------------------------------------------------
    //                                   GETTERS e SETTERS
    //----------------------------------------------------------------------------------------


    public int  getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int  getIdBuraco() {
        return idBuraco;
    }

    public void setIdBuraco(int idBuraco) {
        this.idBuraco = idBuraco;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getData_Registro() {
        return Data_Registro;
    }

    public void setData_Registro(String data_Registro) {
        Data_Registro = data_Registro;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getQtdOcorrencia() {
        return qtdOcorrencia;
    }

    public void setQtdOcorrencia(int qtdOcorrencia) {
        this.qtdOcorrencia = qtdOcorrencia;
    }

    public String getStatusBuraco() {
        return statusBuraco;
    }

    public void setStatusBuraco(String statusBuraco) {
        this.statusBuraco = statusBuraco;
    }

    public String getDataTampado() {
        return dataTampado;
    }

    public void setDataTampado(String dataTampado) {
        this.dataTampado = dataTampado;
    }

    public int getQtdReabertos() {
        return qtdReabertos;
    }

    public void setQtdReabertos(int qtdReabertos) {
        this.qtdReabertos = qtdReabertos;
    }

    @Override
    public String toString()
    {
        return "Buraco: "   + this.idBuraco + "\n" +
                this.rua + '\n' +
                "Cidade: " + this.cidade + '\n' +
                "Estado: " + this.estado + '\n' +
                "Data_Registro: " + this.Data_Registro;
    }


    //----------------------------------------------------------------------------------------
    //                            METODOS DA INTERFACE PARCELABLE
    //----------------------------------------------------------------------------------------

    //CREATOR que gera instancias da Parcel para transporte de objetos
    public static final Parcelable.Creator<Buraco> CREATOR = new Parcelable.Creator<Buraco>() {
        @Override
        public Buraco createFromParcel(Parcel in) {
            return new Buraco(in);
        }

        @Override
        public Buraco[] newArray(int size) {
            return new Buraco[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idBuraco);
        dest.writeString(rua);
        dest.writeString(bairro);
        dest.writeString(cidade);
        dest.writeString(estado);
        dest.writeString(Data_Registro);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(identificador);
        dest.writeString(statusBuraco);
        dest.writeString(dataTampado);
        dest.writeInt(qtdReabertos);

    }



}
