package com.ternium.arquitectura;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Rubén Bermúdez adapted and completed by Rafa Urdaneta
 */
public class TelegramMessage {
    public static final String BASEURL = "https://api.telegram.org/bot";
    public static final String TOKEN = "362782729:AAE_ZC_t97Rc-4efnSOoGLQ7oDlKxa_gVOo";
    public static final String PATH = "sendmessage";


    /// Fields of Send Message
    public static final String CHATID_FIELD = "chat_id";
    private static Integer chatId=-269408175; ///< Unique identifier for the message recepient — User or GroupChat id
    public static final String TEXT_FIELD = "text";
    private static String text; ///< Text of the message to be sent
    public static final String DISABLEWEBPAGEPREVIEW_FIELD = "disable_web_page_preview";
    private static Boolean disableWebPagePreview=false; ///< Disables link previews for links in this message
    public static final String REPLYTOMESSAGEID_FIELD = "reply_to_message_id";
    private static Integer replyToMessageId; ///< Optional. If the message is a reply, ID of the original message
    public static final String REPLYMARKUP_FIELD = "reply_markup";

    /// Fieldsof ReplyKeyboardMarkup
    public static final String KEYBOARD_FIELD = "keyboard";
    private static List<List<String>> keyboard = new ArrayList<List<String>>(); ///< Array of button rows, each represented by an Array of Strings
    public static final String RESIZEKEYBOARD_FIELD = "resize_keyboard";
    /**
     * Optional.
     * Requests clients to resize the keyboard vertically for optimal fit
     * (e.g., make the keyboard smaller if there are just two rows of buttons).
     * Defaults to false.
     */
    private static Boolean resizeKeyboard;
    public static final String ONETIMEKEYBOARD_FIELD = "one_time_keyboard";
    private static Boolean oneTimeKeyboad; ///< Optional. Requests clients to hide the keyboard as soon as it's been used. Defaults to false.
    public static final String SELECTIVE_FIELD = "selective";
    /**
     * Optional. Use this parameter if you want to show the keyboard to specific users only.
     * Targets:
     *      1) users that are @mentioned in the text of the Message object;
     *      2) if the bot's message is a reply (has reply_to_message_id), sender of the original message.
     */
    private static Boolean selective;


    public String sendTelegramMessage(String mensaje) {
        /// TODO Fill keyboard, resizeKeyboard, selective and oneTimeKeyboard with content, I ommit this part here

        try {
            text=mensaje;
            /// Create Http POST method and set correct headers
            String url = BASEURL + TOKEN + "/" + PATH;
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.addHeader("charset", "UTF-8");

            /// Create list of parameters
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            /// Add chatid to the list
            nameValuePairs.add(new BasicNameValuePair(CHATID_FIELD, chatId + ""));
            /// Add text to the list
            nameValuePairs.add(new BasicNameValuePair(TEXT_FIELD, text));
            /// Add disable web preview if necessary
            if (disableWebPagePreview != null) {
                nameValuePairs.add(new BasicNameValuePair(DISABLEWEBPAGEPREVIEW_FIELD, disableWebPagePreview + ""));
            }
            /// Add ReplyMarkup. Calling toJson method and converting it toString
            nameValuePairs.add(new BasicNameValuePair(REPLYMARKUP_FIELD, toJson().toString()));
            /// Add Reply to message if necessary
            if (replyToMessageId != null) {
                nameValuePairs.add(new BasicNameValuePair(REPLYTOMESSAGEID_FIELD, replyToMessageId + ""));
            }

            /// Set list of parameters as entity of the Http POST method
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            sendPOST(httppost);

            /// TODO Execute httppost using, for example CloseableHttpClient#execute method, and then act according with the result, I ommit this steps here
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "mensaje enviado";
    }


    /**
     *  Creates a JSONObject with the content of a ReplyMarkupKeyboard
     */
    public static JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        /// Convert the List<List<String>> to JSONArray
        JSONArray jsonkeyboard = new JSONArray();
        for (List<String> innerRow : keyboard) {
            JSONArray innerJSONKeyboard = new JSONArray();
            for (String element: innerRow) {
                innerJSONKeyboard.put(element);
            }
            jsonkeyboard.put(innerJSONKeyboard);
        }
        /// Add the converted list to final JSON object
        jsonObject.put(KEYBOARD_FIELD, jsonkeyboard);

        /// Add oneTimeKeyboard if necessary
        if (oneTimeKeyboad != null) {
            jsonObject.put(ONETIMEKEYBOARD_FIELD, oneTimeKeyboad);
        }
        /// Add ResizeKeyboard if necessary
        if (resizeKeyboard != null) {
            jsonObject.put(RESIZEKEYBOARD_FIELD, resizeKeyboard);
        }
        /// Add selective if necessary
        if (selective != null) {
            jsonObject.put(SELECTIVE_FIELD, selective);
        }

        return jsonObject;
    }


    private static void sendPOST(HttpPost httpPost) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        System.out.println("POST Response Status:: "
                + httpResponse.getStatusLine().getStatusCode());

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        // print result
        System.out.println(response.toString());
        httpClient.close();

    }

}