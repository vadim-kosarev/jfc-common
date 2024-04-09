package dev.vk.jfc.jfccommon.dto;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ImageMessageTest {

    public static final Logger logger = LoggerFactory.getLogger(ImageMessageTest.class);

    @Test
    void testImageMessage_001() {
        ImageMessage.MessageHeaders msg = new ImageMessage.MessageHeaders();

        msg.put(ImageMessage.HeaderKeys.K_HOSTNAME, "HARON");
        msg.put(ImageMessage.HeaderKeys.K_SOURCE, "VideoCamera:0");
        msg.put(ImageMessage.HeaderKeys.K_FRAMENO, 79);
        msg.put(ImageMessage.HeaderKeys.K_LOCALID, 382991);
        msg.put(ImageMessage.HeaderKeys.K_TIMESTAMP, 1712372019);
        msg.put(ImageMessage.HeaderKeys.K_BROKER_TIMESTAMP, 1712372023);

        String val = msg.get(ImageMessage.HeaderKeys.K_FRAME_STORAGE_PATH);
        assertEquals(val, "local/jpgdata/HARON/VideoCamera_#_0/frame_1712372019_79_382991.jpg");
    }
}
