package com.aliensoldier;

import java.io.*;


public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide input and output files");
            System.exit(0);
        }

        String inputFile = args[0];
        String outputFile = args[1];

        try (
                DataInputStream in = new DataInputStream(new FileInputStream(inputFile));
                OutputStream outputStream = new FileOutputStream(outputFile);
        ) {

            int count = 0;
            byte byteRead = 0;
            short size = in.readShort();
            System.out.println(size);

            while ((byteRead = in.readByte()) != -1 && count <= size) {
                if (byteRead >= 0x7F) {
                    int tmp = byteRead;
                    int cnt = (byteRead >> 2L) % 256 & 0x1F + 1;
                    tmp = tmp << 8L; // ???
                }
                else {
                    if ((byteRead & (1L << 5)) != 0) {
                        if ((byteRead & (1L << 6)) != 0) {
                            int cnt = byteRead & 0x1F + 1;
                            byte s = in.readByte();
                            for (int k = 0; k <= cnt; k++) {
                                outputStream.write(s);
                                byte z = in.readByte();
                                outputStream.write(z);
                            }
                        }
                        else {
                            int cnt = byteRead & 0x1F + 1;
                            byte s = in.readByte();
                            for (int k = 0; k <= cnt; k++) {
                                outputStream.write(s);
                            }
                        }
                    }
                    else {
                        if ((byteRead & (1L << 6)) != 0) {
                            int cnt = byteRead & 0x1F + 1;
                            short s = in.readShort();
                            for (int k = 0; k <= cnt; k++) {
                                outputStream.write(s);
                            }
                        }
                        else {
                            int cnt = byteRead & 0x1F;
                            for (int k = 0; k <= cnt; k++) {
                                byte s = in.readByte();
                                outputStream.write(s);
                            }
                        }
                    }
                }

                outputStream.write(byteRead);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
