package dev.vk.jfc.jfccommon.dto;

import lombok.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@AllArgsConstructor
@Builder
public class ImageMessage {


    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    public abstract static class DataValidation {

        @Getter
        private boolean isValid;

        public void invalidate() {
            isValid = false;
        }

        public void ensureValid() {
            if (!isValid()) {
                calculate();
            }
            isValid = true;
        }

        public abstract void calculate();

    }

    public static abstract class HeaderKeys {
        public static final String K_HOSTNAME = "hostname";
        public static final String K_SOURCE = "source";
        public static final String K_TIMESTAMP = "timestamp";
        public static final String K_BROKER_TIMESTAMP = "brokerTimestamp";
        public static final String K_FRAMENO = "frameNo";
        public static final String K_LOCALID = "localID";
        public static final String K_FRAME_STORAGE_PATH = "frameStoragePath";

    }


    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    public static class MessageHeaders extends DataValidation {

        private final Map<String, String> map = new TreeMap<>();

        private static String escapeHeader(String value) {
            if (null == value) return "NULL";
            value = value.replaceAll(" ", "_");
            value = value.replaceAll("\\:", "_#_");
            value = value.replaceAll("\\\\", "/");
            value = value.replaceAll("//", "/");
            return value;
        }

        @Override
        public void calculate() {
            String formatted = "local/jpgdata/%s/%s/frame_%s_%s_%s.jpg".formatted(
                    escapeHeader(map.get(HeaderKeys.K_HOSTNAME)),
                    escapeHeader(map.get(HeaderKeys.K_SOURCE)),
                    escapeHeader(map.get(HeaderKeys.K_TIMESTAMP)),
                    escapeHeader(map.get(HeaderKeys.K_FRAMENO)),
                    escapeHeader(map.get(HeaderKeys.K_LOCALID)));
            map.put(HeaderKeys.K_FRAME_STORAGE_PATH, formatted);
        }

        public String get(String key) {
            ensureValid();
            return map.get(key);
        }

        public void put(String key, Object value) {
            invalidate();
            map.put(key, String.valueOf(value));
        }

        @Override
        public String toString() {
            return "%s@%s : map: %s".formatted(this.getClass(), this.hashCode(), map);
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    public static class MessageFile extends DataValidation {

        @Getter
        @Setter
        private String mime;

        @Getter
        @Setter
        private byte[] binary;

        @Getter
        private String binaryString;

        @Override
        public void calculate() {
            byte[] encoded = Base64.getEncoder().encode(binary);
            binaryString = new String(encoded);
        }
    }
}