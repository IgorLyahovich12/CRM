package com.example.CRM_BOT.service;

import com.example.CRM_BOT.config.BotConfig;
import com.example.CRM_BOT.dao.ZakazRepository;
import com.example.CRM_BOT.model.Zakaz;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final ZakazRepository zakazRepository;
    private final Map<Long, String> userStates = new HashMap<>();

    public TelegramBot(BotConfig config, ZakazRepository zakazRepository) {
        this.config = config;
        this.zakazRepository = zakazRepository;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/office", "/source", "/medium", "/term", "/content", "/campaign", "/name", "/id", "/day" ->
                        handleCommand(chatId, messageText);
                default -> handleUserInput(chatId, messageText, userStates.getOrDefault(chatId, ""));
            }
        }
    }

    private void handleCommand(long chatId, String command) {
        switch (command) {
            case "/office" -> {
                setUserState(chatId, "AWAITING_OFFICE_NUMBER");
                sendMessage(chatId, "Please enter the office number:");
            }
            case "/source" -> {
                setUserState(chatId, "AWAITING_UTM_SOURCE");
                sendMessage(chatId, "Please enter the UTM source:");
            }
            case "/medium" -> {
                setUserState(chatId, "AWAITING_UTM_MEDIUM");
                sendMessage(chatId, "Please enter the UTM medium:");
            }
            case "/term" -> {
                setUserState(chatId, "AWAITING_UTM_TERM");
                sendMessage(chatId, "Please enter the UTM term:");
            }
            case "/content" -> {
                setUserState(chatId, "AWAITING_UTM_CONTENT");
                sendMessage(chatId, "Please enter the UTM content:");
            }
            case "/campaign" -> {
                setUserState(chatId, "AWAITING_UTM_CAMPAIGN");
                sendMessage(chatId, "Please enter the UTM campaign:");
            }
            case "/name" -> {
                setUserState(chatId, "AWAITING_PRODUCT_NAME");
                sendMessage(chatId, "Please enter the product name:");
            }
            case "/id" -> {
                setUserState(chatId, "AWAITING_PRODUCT_ID");
                sendMessage(chatId, "Please enter the product ID:");
            }
            case "/day" -> {
                setUserState(chatId, "AWAITING_DAY");
                sendMessage(chatId, "Please enter the day (YYYY-MM-DD):");
            }
            default -> sendMessage(chatId, "Unknown command.");
        }
    }

    private void handleUserInput(long chatId, String input, String state) {
        switch (state) {
            case "AWAITING_OFFICE_NUMBER" -> handleOfficeNumberInput(chatId, input);
            case "AWAITING_UTM_SOURCE" -> handleUtmSourceInput(chatId, input);
            case "AWAITING_UTM_MEDIUM" -> handleUtmMediumInput(chatId, input);
            case "AWAITING_UTM_TERM" -> handleUtmTermInput(chatId, input);
            case "AWAITING_UTM_CONTENT" -> handleUtmContentInput(chatId, input);
            case "AWAITING_UTM_CAMPAIGN" -> handleUtmCampaignInput(chatId, input);
            case "AWAITING_PRODUCT_NAME" -> handleProductNameInput(chatId, input);
            case "AWAITING_PRODUCT_ID" -> handleProductIdInput(chatId, input);
            case "AWAITING_DAY" -> handleDayInput(chatId, input);
            default -> sendMessage(chatId, "Unexpected input state.");
        }
        userStates.remove(chatId);
    }

    private void handleOfficeNumberInput(long chatId, String officeNumberInput) {
        try {
            int officeNumber = Integer.parseInt(officeNumberInput);
            List<Zakaz> orders = zakazRepository.findByOfficeNumber(officeNumber);
            sendOrdersList(chatId, orders);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Invalid office number. Please enter a valid number.");
        }
    }

    private void handleUtmSourceInput(long chatId, String utmSourceInput) {
        List<Zakaz> orders = zakazRepository.findByUtmSource(utmSourceInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified UTM source.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }

    private void handleUtmMediumInput(long chatId, String utmMediumInput) {
        List<Zakaz> orders = zakazRepository.findByUtmMedium(utmMediumInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified UTM medium.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }

    private void handleUtmTermInput(long chatId, String utmTermInput) {
        List<Zakaz> orders = zakazRepository.findByUtmTerm(utmTermInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified UTM term.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }

    private void handleUtmContentInput(long chatId, String utmContentInput) {
        List<Zakaz> orders = zakazRepository.findByUtmContent(utmContentInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified UTM content.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }

    private void handleUtmCampaignInput(long chatId, String utmCampaignInput) {
        List<Zakaz> orders = zakazRepository.findByUtmCampaign(utmCampaignInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified UTM campaign.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }

    private void handleProductNameInput(long chatId, String productNameInput) {
        List<Zakaz> orders = zakazRepository.findByProductName(productNameInput);
        if (orders.isEmpty()) {
            sendMessage(chatId, "No orders found for the specified product name.");
        } else {
            sendOrdersList(chatId, orders);
        }
    }


    private void handleProductIdInput(long chatId, String productIdInput) {
        try {
            int productId = Integer.parseInt(productIdInput);
            List<Zakaz> orders = zakazRepository.findByProductId(productId);
            sendOrdersList(chatId, orders);
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Invalid product ID. Please enter a valid number.");
        }
    }

    private void handleDayInput(long chatId, String dayInput) {
        try {
            LocalDate date = LocalDate.parse(dayInput);
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            List<Zakaz> orders = zakazRepository.findByOrderDateTimeBetween(start, end);
            sendOrdersList(chatId, orders);
        } catch (DateTimeParseException e) {
            sendMessage(chatId, "Invalid date format. Please enter the date in YYYY-MM-DD format.");
        }
    }

    private void sendOrdersList(long chatId, List<Zakaz> orders) {
        try {
            if (!orders.isEmpty()) {
                orders.sort(Comparator.comparing(Zakaz::getOrderDateTime));  // Sort orders by date and time
                byte[] excelData = createExcelFile(orders);
                if (excelData != null && excelData.length > 0) {
                    sendExcelFile(chatId, excelData);
                } else {
                    sendMessage(chatId, "Failed to generate Excel file. Please try again later.");
                }
            } else {
                sendMessage(chatId, "No orders found for the specified criteria.");
            }
        } catch (IOException e) {
            log.error("Error processing orders list: ", e);
            sendMessage(chatId, "An error occurred while processing your request. Please try again later.");
        }
    }

    public byte[] createExcelFile(List<Zakaz> orders) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");
        String[] columns = {"Дата і час замовлення", "Ім'я користувача", "Номер телефону", "Назва товару", "ID товару",
                "Ціна", "Кількість", "Номер офісу", "UTM Source", "UTM Medium", "UTM Term", "UTM Content", "UTM Campaign"};

        // Create header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        int rowNum = 1;
        for (Zakaz order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getOrderDateTime().format(formatter));
            row.createCell(1).setCellValue(order.getUserName());
            row.createCell(2).setCellValue(order.getPhoneNumber());
            row.createCell(3).setCellValue(order.getProductName());
            row.createCell(4).setCellValue(order.getProductId());
            row.createCell(5).setCellValue(order.getPrice());
            row.createCell(6).setCellValue(order.getQuantity());
            row.createCell(7).setCellValue(order.getOfficeNumber());
            row.createCell(8).setCellValue(order.getUtmSource());
            row.createCell(9).setCellValue(order.getUtmMedium());
            row.createCell(10).setCellValue(order.getUtmTerm());
            row.createCell(11).setCellValue(order.getUtmContent());
            row.createCell(12).setCellValue(order.getUtmCampaign());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    private void sendExcelFile(long chatId, byte[] excelData) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(String.valueOf(chatId));
        sendDocumentRequest.setDocument(new InputFile(new ByteArrayInputStream(excelData), "Orders.xlsx"));

        try {
            execute(sendDocumentRequest);
        } catch (TelegramApiException e) {
            log.error("Error sending document: ", e);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message: ", e);
        }
    }

    private void setUserState(long chatId, String state) {
        userStates.put(chatId, state);
    }
}
