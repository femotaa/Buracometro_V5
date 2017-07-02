package com.example.felipe.buracometro_v5.dao;


import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

public class BuracoDaoPusher {

    Pusher pusher;
    PusherOptions options;

    public void criaConexaoComPusher() {
        options = new PusherOptions();
        options.setCluster("us2");

        pusher = new Pusher("2f2959e67ea6665968ce", options);
        pusher.connect();
    }

    Channel channel = pusher.subscribe("my-channel");

    public void pegarInformacoes(){

        channel.bind("my-event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {
                System.out.println(data);
            }
        });
    }

}

