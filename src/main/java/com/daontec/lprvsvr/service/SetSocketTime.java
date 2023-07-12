package com.daontec.lprvsvr.service;

import com.daontec.lprvsvr.dto.Anpr;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.net.Socket;
import java.util.List;


class SetSocketTime extends Thread {

    private final int HEAD = 8;
    private List<Anpr> anprlist;
    private List<Socket> socketlist;
    private static boolean loop = true;
    Gson gson = new Gson();
    private static Long interval = 0L;

    public SetSocketTime() {
    }

    public SetSocketTime(List<Anpr> list, List<Socket> socketlist) {
        this.anprlist = list;
        this.socketlist = socketlist;
    }

    public void run() {
        loop = true;
        while (loop) {
            try {
                for (Socket i : socketlist) {
                    for (Anpr j : anprlist) {
                        OutputStream outputStream = i.getOutputStream();
                        try {
                            outputStream.write(LprVsvr.createPacket(gson.toJson(j)));
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                            socketlist.remove(i);
                            LprVsvr.removeClient(i);
                        }

                    }
                }
                if (interval == 0L) {
                    Thread.sleep(5000);
                } else {
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
    public void stoprun() {
        loop = false;
    }
    public void setInterval(Long setinterval){
        interval = setinterval;
    }


}
