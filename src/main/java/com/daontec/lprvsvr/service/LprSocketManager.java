package com.daontec.lprvsvr.service;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class LprSocketManager extends Thread {
    int port = 7777;

    @Override
    public void run() {
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Lpr 가상서버 테스트 시작. 포트: " + port);
                while (true) {
                    System.out.println("접속대기중.");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("접속이 완료되었습니다. \nIP: " + clientSocket.getInetAddress());
                    LprVsvr.addlist(clientSocket);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
