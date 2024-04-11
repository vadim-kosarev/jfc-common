package dev.vk.jfc.jfccommon.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ImageMessageTest {

    public static final Logger logger = LoggerFactory.getLogger(ImageMessageTest.class);


    ImageMessage.MessageHeaders msgHeaders;

    @BeforeEach
    void beforeEach() {
        msgHeaders = new ImageMessage.MessageHeaders();

        msgHeaders.put(ImageMessage.HeaderKeys.K_HOSTNAME, "HARON");
        msgHeaders.put(ImageMessage.HeaderKeys.K_SOURCE, "VideoCamera:0");
        msgHeaders.put(ImageMessage.HeaderKeys.K_FRAMENO, 79);
        msgHeaders.put(ImageMessage.HeaderKeys.K_LOCALID, 382991);
        msgHeaders.put(ImageMessage.HeaderKeys.K_TIMESTAMP, 1712372019);
        msgHeaders.put(ImageMessage.HeaderKeys.K_BROKER_TIMESTAMP, 1712372023);

        msgHeaders.ensureValid();
        logger.info("Msg: {}", msgHeaders);
    }

    @Test
    void testImageMessage_001() {
        String val = msgHeaders.get(ImageMessage.HeaderKeys.K_FRAME_STORAGE_PATH);
        assertEquals(val, "local/jpgdata/HARON/VideoCamera_#_0/frame_1712372019_79_382991.jpg");
    }


    @Test
    void test_ImageMessage_002() throws IOException {
        ImageMessage.Message msg = new ImageMessage.Message(msgHeaders, new ImageMessage.MessageFile());

        InputStream istr = this.getClass().getResourceAsStream("/test/dflt_image.jpg");
        byte[] memFile = istr.readAllBytes();

        msg.getFile().setBinary(memFile);
        msg.getFile().setMime("image/jpeg");
//        msg.getFile().ensureValid();

        msg.ensureValid();;

        ObjectMapper objectMapper = new ObjectMapper();
        String msgStr = objectMapper.writeValueAsString(msg);

        logger.info("Headers: {}", objectMapper.writeValueAsString(msgHeaders));
        logger.info("msgStr: {}", msgStr);

    }
}
