package ru.gb.lesson5.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {

  // http -> 8080
  // https -> 443
  // smtp -> 25
  // ...

  public static final int PORT = 8282;

  public static void main(String[] args) {
    final Map<String, ClientHandler> clients = new HashMap<>();
    try (ServerSocket serverSocket = new ServerSocket(PORT)){
        System.out.println("ИНФО: Сервер запущен на порту " + PORT);
        while (true) {
          try {
            Socket clientSocket = serverSocket.accept();

            PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            clientOut.println("Подключение успешно. Пришлите свой идентификатор");

            Scanner clientIn = new Scanner(clientSocket.getInputStream());
            String clientId = clientIn.nextLine();
            System.out.println("ИНФО: Подключился новый клиент " + clientSocket + " с индентификатором " + clientId);
            String allClients = clients.entrySet().stream()
                    .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
                    .collect(Collectors.joining("\n"));
            if (allClients.length() != 0){
              clientOut.println("Список доступных клиентов: \n" + allClients);
            } else {
              clientOut.println("К серверу подключены только Вы");
            }
            ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
            new Thread(clientHandler).start();

            for (ClientHandler client : clients.values()) {
              client.send("Подключился новый клиент: " + clientSocket + ", id = " + clientId);
            }
            clients.put(clientId, clientHandler);

          } catch (IOException e) {
            System.err.println("ОШИБКА: Произошла ошибка при взаимодействии с клиентом: " + e.getMessage());
          }
        }
      } catch (IOException e) {
        throw new RuntimeException("ОШИБКА: Не удалось начать прослушивать порт " + PORT, e);
      }
    }
}



class ClientHandler implements Runnable {

  private final Socket clientSocket;
  private final PrintWriter out;
  private final Map<String, ClientHandler> clients;

  public ClientHandler(Socket clientSocket, Map<String, ClientHandler> clients) throws IOException {
    this.clientSocket = clientSocket;
    this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    this.clients = clients;
  }

  public Socket getClientSocket() {
    return clientSocket;
  }

  @Override
  public void run() {
    try (Scanner in = new Scanner(clientSocket.getInputStream())) {
      while (true) {
        String input = in.nextLine();
        if (isClosing(input)){
          break;
        }
        if (Objects.equals("/all", input)){
          getAllClients(input);
          System.out.println("Клиент " + clientSocket + " запросил список всех клиентов!");
        }
        else {
          receiveMsg(input);
        }
      }
    } catch (IOException e) {
      System.err.println("ОШИБКА: Произошла ошибка при взаимодействии с клиентом " + clientSocket + ": " + e.getMessage());
    }

    // FIXME: При отключении клиента нужно удалять его из Map и оповещать остальных
    closeClient();
  }

  public void send(String msg) {
    out.println(msg);
  }
  public boolean isClosing(String input){
    if (clientSocket.isClosed() || Objects.equals("/exit", input)) {
      System.out.println("Клиент " + clientSocket + " отключился");
      return true;
    }
    return false;
  }

  public void getAllClients(String input){
      String allClients = clients.entrySet().stream()
              .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
              .collect(Collectors.joining("\n"));
      if (allClients.length() != 0){
        out.println("Список доступных клиентов: \n" + allClients);
      } else {
        out.println("К серверу подключены только Вы");
      }
  }
  public void receiveMsg(String input){
    System.out.println("КЛИЕНТ " + clientSocket + ": " + input);
    String toClientId = null;
    if (input.startsWith("@")) {
      String[] parts = input.split("\\s+");
      if (parts.length > 0) {
        toClientId = parts[0].substring(1);
      }
    }

    if (toClientId == null) {
      // FIXME: Не отсылать самому себе
      clients.values().forEach(it -> it.send("Клиент " + clientSocket + " написал всем: " + input));
    } else {
      ClientHandler toClient = clients.get(toClientId);
      if (toClient != null) {
        toClient.send(input.replace("@" + toClientId + " ", ""));
      } else {
        System.err.println("ОШИБКА: Не найден клиент с идентфикатором: " + toClientId);
      }
    }
  }

  public void closeClient(){
    try {
      String key = clients.entrySet()
              .stream()
              .filter(entry -> this.equals(entry.getValue()))
              .map(Map.Entry::getKey)
              .findFirst().map(Object::toString)
              .orElse("");
      clients.remove(key);
      clients.values().forEach(it -> it.send("Клиент " + clientSocket + " отключился!"));
      clientSocket.close();
    } catch (IOException e) {
      System.err.println("ОШИБКА: Ошибка при отключении клиента " + clientSocket + ": " + e.getMessage());
    }
  }

}
