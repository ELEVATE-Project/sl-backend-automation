package org.shikshalokam.backend;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;
import io.restassured.path.json.JsonPath;


import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailAPI {

    private static final String APPLICATION_NAME = "RAJU";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String USER_ID = "slautoraj@gmail.com";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH =
            System.getProperty("user.dir") +
                    File.separator + "src" +
                    File.separator + "main" +
                    File.separator + "resources" +
                    File.separator + "credentials_new.json";

    private static final String TOKENS_DIRECTORY_PATH = System.getProperty("user.dir") +
            File.separator + "src" +
            File.separator + "main" +
            File.separator + "resources";


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream(new File(CREDENTIALS_FILE_PATH));
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9999).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    private static Gmail getService() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }

    private static List<Message> listMessagesMatchingQuery(Gmail service, String userId,
                                                           String query) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }
        return messages;
    }

    private static Message getMessage(Gmail service, String userId, List<Message> messages, int index)
            throws IOException {
        Message message = service.users().messages().get(userId, messages.get(index).getId()).execute();
        return message;
    }

    public static HashMap<String, String> getGmailData(String query) {
        try {
            Gmail service = getService();
            List<Message> messages = listMessagesMatchingQuery(service, USER_ID, query);
            Message message = getMessage(service, USER_ID, messages, 0);
            JsonPath jp = new JsonPath(message.toString());
            String subject = jp.getString("payload.headers.find { it.name == 'Subject' }.value");
            //String body = new String(Base64.getDecoder().decode(jp.getString("payload.parts[0].body.data")));
            String body = message.get("snippet").toString();
            //String body =jp.getString("payload.parts[1].body.data");
            String link = null;
            String[] arr;
           /* arr = body.split("\n");
            for (String s : arr) {
                s = s.trim();
                if (s.startsWith("http") || s.startsWith("https")) {
                    link = s.trim();
                }
            }*/
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("subject", subject);
            hm.put("body", body);
            hm.put("link", link);
            return hm;
        } catch (Exception e) {
            System.out.println("email not found....");
            throw new RuntimeException(e);
        }
    }

    public static String getOTP(String subjectLine) {

        HashMap<String, String> gmailData = GmailAPI.getGmailData(subjectLine);
        String gmailData1 = gmailData.get("body");
        return extractNumbers(gmailData1).get(0);


    }

    public static String getOTP() {

        String username = "slautoraj@gmail.com"; // Replace with your Gmail address
        String appPassword = "eocqxuqoeglncrbs"; // Replace with your generated app password
        // IMAP properties
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "false");

        Session session = Session.getDefaultInstance(properties);

        try {
            // Connect to Gmail's IMAP server
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, appPassword);

            // Open INBOX folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            String body = (String) inbox.getMessage(inbox.getMessageCount()).getContent();
            String[] split = body.split("<strong>");

            inbox.close(false);
            store.close();
            return extractNumbers(split[1]).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void deleteEmails() {

        String username = "slautoraj@gmail.com"; // Replace with your Gmail address
        String appPassword = "eocqxuqoeglncrbs"; // Replace with your generated app password
        // IMAP properties
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imap.host", "imap.gmail.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "false");

        Session session = Session.getDefaultInstance(properties);

        try {
            // Connect to Gmail's IMAP server
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", username, appPassword);

            // Open INBOX folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            inbox.setFlags(inbox.getMessages(), new Flags(Flags.Flag.DELETED), true);
            inbox.expunge();


            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    private static List<String> extractNumbers(String input) {
        List<String> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group();
            numbers.add(match);
        }
        return numbers;
    }

    public static int getTotalCountOfMails() {
        int size;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            List<Thread> threads = service.
                    users().
                    threads().
                    list("slautoraj@gmail.com").
                    execute().
                    getThreads();
            size = threads.size();
        } catch (Exception e) {
            System.out.println("Exception log " + e);
            size = -1;
        }
        return size;
    }

    public static boolean isMailExist(String messageTitle) {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ListMessagesResponse response = service.
                    users().
                    messages().
                    list("me").
                    setQ("subject:" + messageTitle).
                    execute();
            List<Message> messages = getMessages(response);
            return messages.size() != 0;
        } catch (Exception e) {
            System.out.println("Exception log" + e);
            return false;
        }
    }

    private static List<Message> getMessages(ListMessagesResponse response) {
        List<Message> messages = new ArrayList<Message>();
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(USER_ID)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }
            return messages;
        } catch (Exception e) {
            System.out.println("Exception log " + e);
            return messages;
        }
    }

    public static String getMimeMessage(Gmail service, String userId, List<Message> messages, int index)
            throws IOException {
        Message message = service.users().messages().get(userId, messages.get(index).getId()).execute();
        byte[] biteData = message.getPayload().getBody().decodeData();
        String text = new String(biteData, StandardCharsets.UTF_8);
        return text;
    }

    public static void deleteMessage(String query)
            throws IOException, GeneralSecurityException {
        Gmail service = getService();
        List<Message> messages = listMessagesMatchingQuery(service, USER_ID, query);
        service.users().messages().delete(USER_ID, messages.get(0).getId()).execute();
    }


}
