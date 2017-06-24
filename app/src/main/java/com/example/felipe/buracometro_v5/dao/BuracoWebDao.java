package com.example.felipe.buracometro_v5.dao;

import android.util.Log;

import com.example.felipe.buracometro_v5.modelo.Buraco;
import com.example.felipe.buracometro_v5.modelo.DadosEstatisticos;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class BuracoWebDao {

    private static final String URL       = "http://192.168.1.105:8080/ServidorBuracometroV3/services/BuracoDAO?wsdl";
    private static final String NAMESPACE = "http://buracometro.ServidorBuracometroV3.com.br";

    private static final String METODO_INSERIR                  = "inserirBuraco";
    private static final String METODO_EXCLUIR                  = "excluirBuraco";
    private static final String METODO_EXCLUIRPORIDENTIFICADOR  = "excluirBuracoPorIdentificador";
    private static final String METODO_BUSCARRECENTES           = "buscarBuracosRecentes";
    private static final String METODO_BUSCARTAMPADOS           = "buscarBuracosTampados";
    private static final String METODO_BUSCARPORIDENTIFICADOR   = "buscarBuracoPorIdentificador";
    private static final String METODO_BUSCARMAISCRITICOS       = "buscarMaisCriticos";
    private static final String METODO_BUSCARTOTALBURACOS       = "buscaTotalDeBuracos";
    private static final String METODO_BUSCARTOTALTAMPADOS      = "buscaTotalBuracosTampados";
    private static final String METODO_BUSCARCIDADEMAIS         = "buscaCidadeMaisRegistrada";
    private static final String METODO_BUSCARCIDADEMENOS        = "buscaCidadeMenosRegistrada";
    private static final String METODO_BUSCARRUAMAIS            = "buscaRuaMaisEsburacada";
    private static final String METODO_BUSCARMAISANTIGOS        = "buscaBuracoMaisAntigo";
    private static final String METODO_TAMPAR                   = "tamparBuraco";
    private static final String METODO_REABRIR                  = "reabrirBuraco";
    private static final String METODO_BUSCARTOTALRECENTES      = "buscaTotalRecentes";
    private static final String METODO_BUSCARTOTALHOJE          = "buscaTotalAbertosHoje";

//    private static final String METODO_ATUALIZAR                = "atualizarBuraco";
//    private static final String METODO_BUSCATODOS               = "buscarTodosBuracos";
//    private static final String METODO_BUSCARPORID              = "buscarBuracoPorId";




    //----------------------------------------------------------------------------------------
    //       METODOS QUE FAZEM COMUNICACAO COM METODOS DO WEBSERVER PARA REALIZAR AS QUERYS
    //----------------------------------------------------------------------------------------

    public int inserirBuraco (Buraco buraco)
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject inserirBuraco = new SoapObject(NAMESPACE, METODO_INSERIR);

        //cria objeto SOAP para espelhar o objeto Buraco deste projeto
        SoapObject bura = new SoapObject(NAMESPACE, "buraco");

        //Passagem de propriedades para o objeto SOAP
        bura.addProperty("bairro",          buraco.getBairro());
        bura.addProperty("cidade",          buraco.getCidade());
        bura.addProperty("data_Registro",   buraco.getData_Registro());
        bura.addProperty("estado",          buraco.getEstado());
        bura.addProperty("id",              buraco.getId());
        bura.addProperty("rua",             buraco.getRua());
        bura.addProperty("latitude",        buraco.getLatitude());
        bura.addProperty("longitude",       buraco.getLongitude());
        bura.addProperty("identificador",   buraco.getIdentificador());
        bura.addProperty("statusBuraco",    buraco.getStatusBuraco());
        bura.addProperty("dataTampado",     buraco.getDataTampado());

        //Passagem de objeto espelho para objeto SOAP que utiliza os metodos do WEBSERVICE
        inserirBuraco.addSoapObject(bura);

        //Criacao de pacote para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_INSERIR, envelope);

            SoapPrimitive resposta  = (SoapPrimitive) envelope.getResponse();

            return Integer.parseInt(resposta.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 2;
        }
    }

    //--------------------------------------------------------------------
    //            METODO PARA ATUALIZAR BURACO NO BANCO DE DADOS
    //--------------------------------------------------------------------

    public boolean reabrirBuraco (Buraco buraco)
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject reabrirBuraco = new SoapObject(NAMESPACE, METODO_REABRIR);

        //cria objeto SOAP para espelhar o objeto Buraco deste projeto
        SoapObject bura = new SoapObject(NAMESPACE, "buraco");

        //Passagem de propriedades para o objeto SOAP
        bura.addProperty("bairro",          buraco.getBairro());
        bura.addProperty("cidade",          buraco.getCidade());
        bura.addProperty("data_Registro",   buraco.getData_Registro());
        bura.addProperty("estado",          buraco.getEstado());
        bura.addProperty("id",              buraco.getId());
        bura.addProperty("rua",             buraco.getRua());
        bura.addProperty("latitude",        buraco.getLatitude());
        bura.addProperty("longitude",       buraco.getLongitude());
        bura.addProperty("identificador",   buraco.getIdentificador());
        bura.addProperty("statusBuraco",    buraco.getStatusBuraco());
        bura.addProperty("dataTampado",     buraco.getDataTampado());

        //Passagem de objeto espelho para objeto SOAP que utiliza os metodos do WEBSERVICE
        reabrirBuraco.addSoapObject(bura);

        //Criacao de pacote para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(reabrirBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_REABRIR, envelope);

            SoapPrimitive resposta  = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(resposta.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }


    //--------------------------------------------------------------------
    //                     METODO PARA TAMPAR BURACO
    //--------------------------------------------------------------------
    public boolean tamparBuraco (Buraco buraco) throws Exception
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject tamparBuraco = new SoapObject(NAMESPACE, METODO_TAMPAR);

        //cria objeto SOAP para espelhar o objeto Buraco deste projeto
        SoapObject bura = new SoapObject(NAMESPACE, "buraco");

        //Passagem de propriedades para o objeto SOAP
        bura.addProperty("bairro",          buraco.getBairro());
        bura.addProperty("cidade",          buraco.getCidade());
        bura.addProperty("data_Registro",   buraco.getData_Registro());
        bura.addProperty("estado",          buraco.getEstado());
        bura.addProperty("id",              buraco.getId());
        bura.addProperty("rua",             buraco.getRua());
        bura.addProperty("latitude",        buraco.getLatitude());
        bura.addProperty("longitude",       buraco.getLongitude());
        bura.addProperty("identificador",   buraco.getIdentificador());
        bura.addProperty("statusBuraco",    buraco.getStatusBuraco());
        bura.addProperty("dataTampado",     buraco.getDataTampado());

        //Passagem de objeto espelho para objeto SOAP que utiliza os metodos do WEBSERVICE
        tamparBuraco.addSoapObject(bura);

        //Criacao de pacote para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(tamparBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_TAMPAR, envelope);
            SoapPrimitive resposta  = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(resposta.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

    }


    //---------------------------------------------------------------------
    //             METODO PARA EXLCLUIR BURACO NO BANCO DE DADOS
    //---------------------------------------------------------------------
    public boolean excluirBuraco (Buraco buraco) throws Exception
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject excluirBuraco = new SoapObject(NAMESPACE, METODO_EXCLUIR);

        //cria objeto SOAP para espelhar o objeto Buraco deste projeto
        SoapObject bura = new SoapObject(NAMESPACE, "buraco");

        //Passagem de propriedades para o objeto SOAP
        bura.addProperty("bairro",          buraco.getBairro());
        bura.addProperty("cidade",          buraco.getCidade());
        bura.addProperty("data_Registro",   buraco.getData_Registro());
        bura.addProperty("estado",          buraco.getEstado());
        bura.addProperty("id",              buraco.getId());
        bura.addProperty("rua",             buraco.getRua());
        bura.addProperty("latitude",        buraco.getLatitude());
        bura.addProperty("longitude",       buraco.getLongitude());
        bura.addProperty("identificador",   buraco.getIdentificador());

        //Passagem de objeto espelho para objeto SOAP que utiliza os metodos do WEBSERVICE
        excluirBuraco.addSoapObject(bura);

        //Criacao de pacote para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_EXCLUIR, envelope);

            SoapPrimitive resposta  = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(resposta.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

    }


    //---------------------------------------------------------------------
    //         METODO PARA EXLCLUIR TODOS OS BURACO NO SERVIDOR
    //---------------------------------------------------------------------
    public boolean excluirTodosServidor (String identificador) throws Exception
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject excluirBuraco = new SoapObject(NAMESPACE, METODO_EXCLUIRPORIDENTIFICADOR);
        excluirBuraco.addProperty("identificador", identificador);

        //Criacao de pacote para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_EXCLUIRPORIDENTIFICADOR, envelope);

            SoapPrimitive resposta  = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(resposta.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }


    //-------------------------------------------------------------------------
    //                  METODO PARA BUSCAR BURACO POR ID
    //-------------------------------------------------------------------------
    public ArrayList<Buraco> buscarBuracosPorIdentificador (String identificador) throws Exception
    {
        ArrayList<Buraco> lista = new ArrayList<Buraco>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarBuraco = new SoapObject(NAMESPACE, METODO_BUSCARPORIDENTIFICADOR);
        buscarBuraco.addProperty("identificador", identificador);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARPORIDENTIFICADOR, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                Buraco bura = new Buraco();
//                bura.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                bura.setIdBuraco(Integer.parseInt(soapObject.getProperty("id").toString()));
                bura.setRua(soapObject.getProperty("rua").toString());
                bura.setBairro(soapObject.getProperty("bairro").toString());
                bura.setCidade(soapObject.getProperty("cidade").toString());
                bura.setEstado(soapObject.getProperty("estado").toString());
                bura.setData_Registro(soapObject.getProperty("data_Registro").toString());
                bura.setLatitude(soapObject.getProperty("latitude").toString());
                bura.setLongitude(soapObject.getProperty("longitude").toString());
                bura.setIdentificador(soapObject.getProperty("identificador").toString());
                bura.setStatusBuraco(soapObject.getProperty("statusBuraco").toString());
                try{
                    bura.setDataTampado(soapObject.getProperty("dataTampado").toString());
                }catch(Exception e){

                }
                bura.setId(Integer.parseInt(soapObject.getProperty("qtdReabertos").toString()));

                lista.add(bura);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            throw e;
        }

        return lista;
    }


    //---------------------------------------------------------------------
    //          METODO PARA LISTAR BURACOS RECENTES DO BANCO DE DADOS
    //---------------------------------------------------------------------
    public ArrayList<Buraco> buscarBuracosRecentes (int pagina) throws Exception
    {
        ArrayList<Buraco> lista = new ArrayList<Buraco>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarBuraco = new SoapObject(NAMESPACE, METODO_BUSCARRECENTES);
        buscarBuraco.addProperty("pagina", pagina);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + METODO_BUSCARRECENTES, envelope);
            Vector<SoapObject> resposta = null;
            resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                try{

                    Buraco bura = new Buraco();
//                    bura.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                    bura.setIdBuraco(Integer.parseInt(soapObject.getProperty("id").toString()));
                    bura.setRua(soapObject.getProperty("rua").toString());
                    bura.setBairro(soapObject.getProperty("bairro").toString());
                    bura.setCidade(soapObject.getProperty("cidade").toString());
                    bura.setEstado(soapObject.getProperty("estado").toString());
                    bura.setData_Registro(soapObject.getProperty("data_Registro").toString());
                    bura.setLatitude(soapObject.getProperty("latitude").toString());
                    bura.setLongitude(soapObject.getProperty("longitude").toString());
                    bura.setIdentificador(soapObject.getProperty("identificador").toString());
                    bura.setStatusBuraco(soapObject.getProperty("statusBuraco").toString());
                    bura.setId(Integer.parseInt(soapObject.getProperty("qtdReabertos").toString()));

                    lista.add(bura);

                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return lista;
    }


    //---------------------------------------------------------------------
    //    METODO PARA LISTAR BURACOS RECENTES TAMPADOS DO BANCO DE DADOS
    //---------------------------------------------------------------------
    public ArrayList<Buraco> buscarBuracosTampados (int pagina) throws Exception
    {
        ArrayList<Buraco> lista = new ArrayList<Buraco>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarBuraco = new SoapObject(NAMESPACE, METODO_BUSCARTAMPADOS);
        buscarBuraco.addProperty("pagina", pagina);


        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARTAMPADOS, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                try{

                    Buraco bura = new Buraco();
//                    bura.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                    bura.setIdBuraco(Integer.parseInt(soapObject.getProperty("id").toString()));
                    bura.setRua(soapObject.getProperty("rua").toString());
                    bura.setBairro(soapObject.getProperty("bairro").toString());
                    bura.setCidade(soapObject.getProperty("cidade").toString());
                    bura.setEstado(soapObject.getProperty("estado").toString());
                    bura.setData_Registro(soapObject.getProperty("data_Registro").toString());
                    bura.setLatitude(soapObject.getProperty("latitude").toString());
                    bura.setLongitude(soapObject.getProperty("longitude").toString());
                    bura.setIdentificador(soapObject.getProperty("identificador").toString());
                    bura.setStatusBuraco(soapObject.getProperty("statusBuraco").toString());
                    bura.setDataTampado(soapObject.getProperty("dataTampado").toString());
                    bura.setId(Integer.parseInt(soapObject.getProperty("qtdReabertos").toString()));

                    lista.add(bura);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return lista;
    }


    //---------------------------------------------------------------------
    //          METODO PARA LISTAR TODOS OS BURACO DO BANCO DE DADOS
    //---------------------------------------------------------------------
    public ArrayList<Buraco> buscarMaisCriticos (int pagina) throws Exception
    {
        ArrayList<Buraco> lista = new ArrayList<Buraco>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarBuraco = new SoapObject(NAMESPACE, METODO_BUSCARMAISCRITICOS);
        buscarBuraco.addProperty("pagina", pagina);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarBuraco);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARMAISCRITICOS, envelope);
            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                Buraco bura = new Buraco();
//                bura.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                bura.setIdBuraco(Integer.parseInt(soapObject.getProperty("id").toString()));
                bura.setRua(soapObject.getProperty("rua").toString());
                bura.setBairro(soapObject.getProperty("bairro").toString());
                bura.setCidade(soapObject.getProperty("cidade").toString());
                bura.setEstado(soapObject.getProperty("estado").toString());
                bura.setData_Registro(soapObject.getProperty("data_Registro").toString());
                bura.setLatitude(soapObject.getProperty("latitude").toString());
                bura.setLongitude(soapObject.getProperty("longitude").toString());
                bura.setIdentificador(soapObject.getProperty("identificador").toString());
                bura.setStatusBuraco(soapObject.getProperty("statusBuraco").toString());
//                bura.setDataTampado(soapObject.getProperty("dataTampado").toString());
                bura.setId(Integer.parseInt(soapObject.getProperty("qtdReabertos").toString()));
                bura.setQtdOcorrencia(Integer.parseInt(soapObject.getProperty("qtdOcorrencia").toString()));

                lista.add(bura);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return lista;
    }



    //---------------------------------------------------------------------
    //          METODO PARA RETORNAR O TOTAL DE BURACOS DO BANCO
    //---------------------------------------------------------------------
    public int buscaTotalDeBuracos ()
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARTOTALBURACOS);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        int qtdDeBuracos;
        try
        {
            http.call("urn:" + METODO_BUSCARTOTALBURACOS, envelope);

//            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();
            SoapPrimitive resposta = (SoapPrimitive)envelope.getResponse();

            qtdDeBuracos = (Integer.parseInt(resposta.toString()));

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("NP BUSCARTOTALBURACOS",e + "");
            return -2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return -1;
        }

        return qtdDeBuracos;

    }


    //---------------------------------------------------------------------
    //          METODO PARA RETORNAR O TOTAL DE BURACOS DO BANCO
    //---------------------------------------------------------------------
    public int buscaTotalDeBuracosTampados ()
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARTOTALTAMPADOS);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        int qtdDeBuracos;

        try
        {
            http.call("urn:" + METODO_BUSCARTOTALTAMPADOS, envelope);

//            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();
            SoapPrimitive resposta = (SoapPrimitive)envelope.getResponse();

            qtdDeBuracos = (Integer.parseInt(resposta.toString()));

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("NP BUSCARTTAMPADOS",e + "");
            return -2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return -1;
        }

        return qtdDeBuracos;

    }


    //---------------------------------------------------------------------
    //          METODO PARA RETORNAR O TOTAL DE BURACOS DO BANCO
    //---------------------------------------------------------------------
    public int buscaTotalDeBuracosRecentes ()
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARTOTALRECENTES);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        int qtdDeBuracos;
        try
        {
            http.call("urn:" + METODO_BUSCARTOTALRECENTES, envelope);

//            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();
            SoapPrimitive resposta = (SoapPrimitive)envelope.getResponse();

            qtdDeBuracos = (Integer.parseInt(resposta.toString()));

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("NP TOTAL RECENTES",e + "");
            return -2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return -1;
        }

        return qtdDeBuracos;

    }


    //---------------------------------------------------------------------
    //          METODO PARA RETORNAR O TOTAL DE BURACOS DO BANCO
    //---------------------------------------------------------------------
    public int buscaTotalDeBuracosAbertoHoje ()
    {
        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARTOTALHOJE);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        int qtdDeBuracos;
        try
        {
            http.call("urn:" + METODO_BUSCARTOTALHOJE, envelope);

//            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();
            SoapPrimitive resposta = (SoapPrimitive)envelope.getResponse();

            qtdDeBuracos = (Integer.parseInt(resposta.toString()));

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("NP TOTAL hoje",e + "");
            return -2;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return -1;
        }

        return qtdDeBuracos;

    }


    //---------------------------------------------------------------------
    //          METODO PARA BUSCAR CIDADE MAIS REGISTRADA
    //---------------------------------------------------------------------
    public ArrayList<DadosEstatisticos> buscaCidadeMaisRegistrada ()
    {
        ArrayList<DadosEstatisticos> lista = new ArrayList<DadosEstatisticos>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARCIDADEMAIS);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARCIDADEMAIS, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                DadosEstatisticos de = new DadosEstatisticos();

                de.setDados(soapObject.getProperty("dados").toString());
                de.setDados2(soapObject.getProperty("dados2").toString());
                de.setQtdDados(Integer.parseInt(soapObject.getProperty("qtdDados").toString()));
                de.setPorcertagem(Double.parseDouble(soapObject.getProperty("porcertagem").toString()));

                lista.add(de);
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return lista;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return null;
        }


        return lista;

    }


    //---------------------------------------------------------------------
    //          METODO PARA BUSCAR CIDADE MENOS REGISTRADA
    //---------------------------------------------------------------------
    public ArrayList<DadosEstatisticos> buscaCidadeMenosRegistrada () throws Exception
    {
        ArrayList<DadosEstatisticos> lista = new ArrayList<DadosEstatisticos>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARCIDADEMENOS);

        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARCIDADEMENOS, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                DadosEstatisticos de = new DadosEstatisticos();

                de.setDados(soapObject.getProperty("dados").toString());
                de.setDados2(soapObject.getProperty("dados2").toString());
                de.setQtdDados(Integer.parseInt(soapObject.getProperty("qtdDados").toString()));
                de.setPorcertagem(Double.parseDouble(soapObject.getProperty("porcertagem").toString()));

                lista.add(de);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }

        return lista;
    }


    //---------------------------------------------------------------------
    //          METODO PARA BUSCAR RUA COM MAIS BURACO
    //---------------------------------------------------------------------
    public ArrayList<DadosEstatisticos> buscaRuaMaisEsburacada ()
    {
        ArrayList<DadosEstatisticos> lista = new ArrayList<DadosEstatisticos>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARRUAMAIS);


        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARRUAMAIS, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                DadosEstatisticos de = new DadosEstatisticos();

                de.setDados(soapObject.getProperty("dados").toString());
                de.setDados2(soapObject.getProperty("dados2").toString());
                de.setDados3(soapObject.getProperty("dados3").toString());
                de.setQtdDados(Integer.parseInt(soapObject.getProperty("qtdDados").toString()));

                lista.add(de);
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return lista;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return null;
        }

        return lista;

    }


    //---------------------------------------------------------------------
    //          METODO PARA BUSCAR RUA COM MAIS BURACO
    //---------------------------------------------------------------------

    public ArrayList<DadosEstatisticos> buscaBuracoMaisAntigo ()
    {
        ArrayList<DadosEstatisticos> lista = new ArrayList<DadosEstatisticos>();

        //cria objeto SOAP para utilizar metodos do webservice
        SoapObject buscarDados = new SoapObject(NAMESPACE, METODO_BUSCARMAISANTIGOS);


        //Criacao de pacote(envelope) para enviar ao WEBSERVICE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarDados);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try
        {
            http.call("urn:" + METODO_BUSCARMAISANTIGOS, envelope);

            Vector<SoapObject> resposta = (Vector<SoapObject>)envelope.getResponse();

            for(SoapObject soapObject : resposta)
            {
                DadosEstatisticos de = new DadosEstatisticos();

                de.setDados(soapObject.getProperty("dados").toString());
                de.setDados2(soapObject.getProperty("dados2").toString());
                de.setDados3(soapObject.getProperty("dados3").toString());
                de.setQtdDados(Integer.parseInt(soapObject.getProperty("qtdDados").toString()));

                lista.add(de);
            }

        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return lista;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Erro na query: ", e + "");
            return null;
        }

        return lista;

    }



}
