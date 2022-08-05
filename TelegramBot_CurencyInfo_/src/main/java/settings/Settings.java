package settings;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import serviceClasses.Bank;
import serviceClasses.CurrencyDataBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
public class Settings {
    public static Map<Long, OptionsUser> settings = new HashMap<>();
    private static final Gson settingGson = new Gson();
    private static final String SETTING_GSON_PATH = "src/main/resources/settings.json";
    private static final Object monitor = new Object();
    static ExecutorService service = Executors.newSingleThreadExecutor();
    public static String getInfo (Long chatId) {
        service.execute(new SaveSettings());
        StringBuilder messageToUser = new StringBuilder();
        OptionsUser userOptionsUser = settings.get(chatId);
        String bankName = userOptionsUser.getSelectedBank().getBankNameUA();
        messageToUser.append(bankName).append("\n");
        int numberDecPlaces = userOptionsUser.getNumberOfDecimalPlaces();
        List<Currency> currencies = userOptionsUser.getSelectedCurrency();
        Bank bankInfo = CurrencyDataBase.getCurrentInfo(userOptionsUser.getSelectedBank());
        for (Currency currency : currencies) {
            messageToUser.append("Курс купівлі ")
                    .append(currency.getCurrencyName())
                    .append(" - ")
                    .append(bankInfo.getBuyRate(currency) == 0 ? "немає купівлі" :
                            format("%." + numberDecPlaces + "f" , bankInfo.getBuyRate(currency)))
                    .append("\n");
            messageToUser.append("Курс продажу ")
                    .append(currency.getCurrencyName())
                    .append(" - ")
                    .append(bankInfo.getSellRate(currency) == 0 ? "немає продажу" :
                            format("%." + numberDecPlaces + "f" , bankInfo.getSellRate(currency)))
                    .append("\n");
        }
        return messageToUser.toString();
    }
    public static File fileSettingsGsonCheck() {
        File settingGsonFile = new File(SETTING_GSON_PATH);
        if (!settingGsonFile.exists()) {
            System.out.println("Create Path for Gson file Settings - " + settingGsonFile.getParentFile().mkdirs());
            try {
                System.out.println("Create new Gson file Settings - " + settingGsonFile.createNewFile());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return settingGsonFile;
    }
    public static void load() {
        synchronized (monitor) {
            try {
                IntermediateSettings.intermediateSettings = new ObjectMapper().readValue(fileSettingsGsonCheck(),
                        new TypeReference<Map<Long, IntermediateSetting>>() {
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void save() {
        synchronized (monitor) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSettingsGsonCheck()))) {
                writer.write(settingGson.toJson(Settings.settings));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void converter() {
        synchronized (monitor) {
            Map<Long, IntermediateSetting> inputMap = IntermediateSettings.intermediateSettings;
            Map<Long, OptionsUser> outputMap = Settings.settings;
            inputMap.forEach((k, v) -> {
                OptionsUser outputOptionsUser = new OptionsUser();

                outputOptionsUser.setChatId(v.getChatId());
                outputOptionsUser.setNumberOfDecimalPlaces(parseNumOfDecPlaces(v.getNumberOfDecimalPlaces()));
                outputOptionsUser.setSelectedBank(parseSelectedBank(v.getSelectedBank()));
                outputOptionsUser.setSelectedCurrency(parseCurrency(v.getSelectedCurrency()));
                outputOptionsUser.setNotificationTime(parseNotificationTime(v.getNotificationTime()));
                outputOptionsUser.setZoneId(parseZoneId(v.getZoneId()));
                outputMap.put(v.getChatId(), outputOptionsUser);
            });
        }
    }
    private static NumberOfDecimalPlaces parseNumOfDecPlaces(String inputStrNumOfDec) {
        for (NumberOfDecimalPlaces value : NumberOfDecimalPlaces.values()) {
            if (inputStrNumOfDec.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
    private static Banks parseSelectedBank(String inputStrBank) {
        for (Banks value : Banks.values()) {
            if (inputStrBank.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
    private static List<Currency> parseCurrency(List<String> inputListStrCurrency) {
        List<Currency> result = new ArrayList<>();
        for (Currency value : Currency.values()) {
            for (String oneCurrency : inputListStrCurrency) {
                if (oneCurrency.equals(value.name())) {
                    result.add(value);
                }
            }

        }
        return result;
    }
    private static NotificationTime parseNotificationTime(String inputStrNotificationTime) {
        for (NotificationTime value : NotificationTime.values()) {
            if (inputStrNotificationTime.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
    private static ZoneId parseZoneId(String inputStrZoneId) {
        for (ZoneId value : ZoneId.values()) {
            if (inputStrZoneId.equals(value.name())) {
                return value;
            }
        }
        return null;
    }
}
