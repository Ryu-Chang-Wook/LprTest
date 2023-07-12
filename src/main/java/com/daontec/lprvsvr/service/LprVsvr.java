package com.daontec.lprvsvr.service;


import com.daontec.lprvsvr.dto.Anpr;
import com.daontec.lprvsvr.dto.AnprAlive;
import com.daontec.lprvsvr.dto.AnprResult;
import com.daontec.lprvsvr.dto.DeviceSn;
import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.io.*;

import java.net.Socket;

import java.util.*;

@Component
public class LprVsvr {

    private static List<Socket> list = new ArrayList<>();
    private final int HEAD = 8;
    private static Scanner in = new Scanner(System.in);
    private static int input;
    AnprResult anprResult = new AnprResult(new Anpr("40두1381", "20229754", "/pgi/2021/01/20/LPR2_20229754_051101100_1381.jpg"));
    AnprAlive anprAlive = new AnprAlive(new DeviceSn("20229754"));
    Gson gson = new Gson();
    private static boolean loop = true;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static boolean intervalActivate = false;


    @PostConstruct
    public void start() throws InterruptedException {
        LprSocketManager lprSocketManager = new LprSocketManager();
        lprSocketManager.start();
        Thread.sleep(1000);
        while (loop) {
            if (input != 7) {
                try {
                    System.out.println("=================================");
                    System.out.println("* Lpr 스트레스 테스트 프로그램 *\n1 - anpr_result 더미데이터 전송\n2 - anpr_alive 더미데이터 전송\n3 - 스트레스 테스트 시작\n4 - 스트레스 테스트 중지\n5 - 메세지 전송\n6 - 스트레스 메세지 전송속도 설정\n7 - 종료");
                    System.out.print("Select : ");
                    try {
                        input = in.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("1부터 7까지의 숫자만 입력가능합니다.");
                        in.nextLine();
                        continue;
                    }
                    System.out.println("=================================");
                    if (list.size() == 0) {
                        System.out.println("클라이언트가 없습니다.");
                        continue;
                    }
                    if (input < 1 || input > 8) continue;
                    byte[] packet = new byte[0];
                    switch (input) {
                        case 1:
                            packet = createPacket(gson.toJson(anprResult));
                            break;
                        case 2:
                            packet = createPacket(gson.toJson(anprAlive));
                            break;
                        case 3:
                            if (intervalActivate) {
                                System.out.println("인터벌 테스트가 이미 실행중입니다.");
                                break;
                            }
                            intervalActivate = true;
                            List<Anpr> anprs = new ArrayList<>();
                            ReadText readText = new ReadText();
                            String raw = readText.rawString();
                            String[] parse = raw.split(";");
                            for (String i : parse) {
                                String[] anprlist = i.split(",");
                                Anpr anpr = new Anpr(anprlist[0], anprlist[1], anprlist[2]);
                                anprs.add(anpr);
                            }
                            SetSocketTime setTime = new SetSocketTime(anprs, list);
                            setTime.start();
                            System.out.println("인터벌 스트레스 테스트 시작.");
                            break;
                        case 4:
                            intervalActivate = false;
                            SetSocketTime setTime1 = new SetSocketTime();
                            setTime1.stoprun();
                            System.out.println("인터벌 스트레스 테스트 중지.");
                            break;
                        case 5:
                            System.out.print("메세지 : ");
                            String read;
                            read = br.readLine();
                            packet = createPacket(read);
                            break;
                        case 6:
                            System.out.print("인터벌속도 (1sec = 1000입력) : ");
                            Long setSpeed;
                            setSpeed = in.nextLong();
                            SetSocketTime setTime3 = new SetSocketTime();
                            setTime3.setInterval(setSpeed);
                            break;
                        case 7:
                            intervalActivate = false;
                            SetSocketTime setTime2 = new SetSocketTime();
                            setTime2.stoprun();
                            loop = false;
                            packet = createPacket("사요나라..");
                            for (Socket i : list) {
                                OutputStream outputStream = i.getOutputStream();
                                outputStream.write(packet);
                                i.close();
                            }
                            System.out.println("서버가 종료되었습니다.");
                            break;
                        default:
                            break;
                    }

                    for (Socket i : list) {
                        OutputStream outputStream = i.getOutputStream();

                        // 데이터 전송
                        if (input != 4 && input != 3) {
                            try {
                                outputStream.write(packet);
                            } catch (Exception e) {
                                list.remove(i);
                                LprVsvr.removeClient(i);
                            }
                        }
                    }
                    System.out.println();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }


        }
    }

    public static void removeClient(Socket clientSocket) {
        list.removeIf(i -> i.equals(clientSocket));
    }

    public static void addlist(Socket clientSocket) {
        list.add(clientSocket);
    }

    public static byte[] createPacket(String body) throws UnsupportedEncodingException {
        List<Byte> packet = new ArrayList<>();
        byte[] bodybytes = body.getBytes("euc-kr");
        int length = Integer.parseInt(String.valueOf(String.valueOf(bodybytes.length).length()));
        String lengthbody = String.format("%-8d", bodybytes.length);

        byte[] lengthresult = lengthbody.getBytes("euc-kr");
        for (byte i : lengthresult) {
            packet.add(i);
        }
        for (byte i : bodybytes) {
            packet.add(i);
        }
        return Bytes.toArray(packet);
    }

}







