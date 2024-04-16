package dev.vk.jfc.jfccommon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vk.jfc.jfccommon.Jfc;
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
        @JsonIgnore
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


    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    public static class MessageHeaders extends DataValidation {

        @JsonProperty
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
                    escapeHeader(map.get(Jfc.K_HOSTNAME)),
                    escapeHeader(map.get(Jfc.K_SOURCE)),
                    escapeHeader(map.get(Jfc.K_TIMESTAMP)),
                    escapeHeader(map.get(Jfc.K_FRAMENO)),
                    escapeHeader(map.get(Jfc.K_LOCALID)));
            map.put(Jfc.K_FRAME_STORAGE_PATH, formatted);
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

        public Map<String, Object> copyMap() {
            return new HashMap<>(map);
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    public static class MessageFile extends DataValidation {

        @Getter
        @Setter
        @JsonProperty
        private String mime;

        @Getter
        @Setter
        @JsonIgnore
        private byte[] binary;

        @Getter
        @JsonProperty
        private String binaryString;

        @Override
        public void calculate() {
            byte[] encoded = Base64.getEncoder().encode(binary);
            binaryString = new String(encoded);
        }

        public void setBinaryString(String str) {
            byte[] encoded = str.getBytes();
            binary = Base64.getDecoder().decode(encoded);
        }

        public MessageFile(String mime, byte[] binary) {
            this.mime = mime;
            this.binary = binary;
        }

        public MessageFile(String mime, String binaryString) {
            this.mime = mime;
            setBinaryString(binaryString);
        }

        public MessageFile() {
        }
    }

    /**
     * -----------------------------------------------------------------------------------------------------------------
     */
    @AllArgsConstructor
    @Builder
    public static class Message extends DataValidation {

        @Getter
        @JsonIgnore
        private MessageHeaders headers;

        @JsonProperty("file")
        @Setter
        @Getter
        private MessageFile file;

        @Override
        public void calculate() {
            headers.calculate();
            file.calculate();
        }

        @Override
        public void ensureValid() {
            headers.ensureValid();
            file.ensureValid();
            super.ensureValid();
        }
    }

}