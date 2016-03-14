package com.revolut.transfer;

import com.revolut.transfer.datamodel.Account;
import com.revolut.transfer.datamodel.Transfer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Client {

    public Account get(long accountId) throws IOException {
        System.out.println("Fetching account #" + accountId);

        String path = "/transferservice/accounts/" + accountId;
        String urlString = "http://localhost:8080" + path;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = connection.getResponseCode();

        System.out.println("Response code = " + responseCode);

        InputStream inputStream = connection.getInputStream();
        if (responseCode == 200) {
            Account account = Account.read(connection.getInputStream());
            inputStream.close();
            return account;
        } else {
            String error = readMessage(inputStream);
            inputStream.close();
            throw new RuntimeException("Failed to get account: " + error);
        }
    }

    public void insert(Account account) throws IOException {
        System.out.println("Inserting account " + account);

        String path = "/transferservice/accounts";
        String urlString = "http://localhost:8080" + path;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        connection.setDoOutput(true);

        // write account to request body
        Account.write(account, connection.getOutputStream());
        connection.getOutputStream().close();

        System.out.println("Sending POST request to server " + url);
        int responseCode = connection.getResponseCode();


        System.out.println("responseCode = " + responseCode);

        if (responseCode == 200) {
            return;
        } else {
            InputStream in = connection.getInputStream();
            String error = readMessage(in);
            throw new RuntimeException("Failed to insert account: " + error);
        }
    }

    public void transter(Transfer transfer) throws IOException {
        System.out.println("Sending transfer request " + transfer);

        String path = "/transferservice/transfers";
        String urlString = "http://localhost:8080" + path;
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("PUT");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        connection.setDoOutput(true);

        // write account to request body
        Transfer.write(transfer, connection.getOutputStream());
        connection.getOutputStream().close();

        int responseCode = connection.getResponseCode();
        System.out.println("Sending POST request to server " + url);

        System.out.println("responseCode = " + responseCode);

        if (responseCode == 200) {
            return;
        } else {
            InputStream in = connection.getInputStream();
            String error = readMessage(in);
            throw new RuntimeException("Failed to insert account: " + error);
        }
    }

    private static String readMessage(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        do {
            String line = br.readLine();
            if (line == null) {
                break;
            }

            sb.append(line).append("\n");
        } while (true);

        return sb.toString();
    }
}
