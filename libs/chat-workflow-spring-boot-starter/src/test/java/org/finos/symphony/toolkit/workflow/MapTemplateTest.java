package org.finos.symphony.toolkit.workflow;

import com.symphony.api.id.SymphonyIdentity;
import com.symphony.api.model.*;
import com.symphony.api.pod.UsersApi;
import org.finos.symphony.toolkit.workflow.sources.symphony.handlers.AttachmentHandler;
import org.finos.symphony.toolkit.workflow.sources.symphony.handlers.EntityJsonConverter;
import org.finos.symphony.toolkit.workflow.sources.symphony.handlers.SymphonyResponseHandler;
import org.finos.symphony.toolkit.workflow.sources.symphony.handlers.freemarker.FreemarkerFormMessageMLConverter;
import org.finos.symphony.toolkit.workflow.sources.symphony.messages.*;
import org.finos.symphony.toolkit.workflow.sources.symphony.room.SymphonyRooms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

class MapTemplateTest extends AbstractMockSymphonyTest {

    @Autowired
    Workflow workflow;

    @Autowired
    SymphonyIdentity symphonyIdentity;

    PresentationMLHandler presentationMLHandler;

    @Autowired
    UsersApi usersApi;

    @MockBean
    SymphonyRooms symphonyRooms;

    @MockBean
    AttachmentHandler attachmentHandler;

    @Autowired
    CommandPerformer commandPerformer;

    @Autowired
    FreemarkerFormMessageMLConverter freemarkerFormMessageMLConverter;

    @BeforeEach
    public void setup() {
        SimpleMessageParser simpleMessageParser = new SimpleMessageParser();
        EntityJsonConverter entityJsonConverter = new EntityJsonConverter(workflow);
        List<SimpleMessageConsumer> consumers = Arrays.asList(new HelpMessageConsumer(), new MethodCallMessageConsumer(commandPerformer));
        SymphonyResponseHandler symphonyResponseHandler = new SymphonyResponseHandler(messagesApi, freemarkerFormMessageMLConverter, entityJsonConverter, symphonyRooms, attachmentHandler);
        presentationMLHandler = new PresentationMLHandler(workflow, symphonyIdentity, usersApi, simpleMessageParser, entityJsonConverter, consumers, symphonyResponseHandler, symphonyRooms);
    }

    @Test
    void testForViewMap() {
        V4Event e = new V4Event()
                .payload(new V4Payload()
                        .messageSent(new V4MessageSent()
                                .message(new V4Message()
                                        .message("<div data-format=\"PresentationML\"><p>/viewmap</p></div>")
                                        .user(new V4User().email("rob@example.com"))
                                        .stream(new V4Stream()
                                                .streamId("abc123")
                                                .streamType("ROOM")
                                        ))));

        presentationMLHandler.accept(e);
        Mockito.verify(messagesApi).v4StreamSidMessageCreatePost(
                Mockito.isNull(), Mockito.isNull(),
                Mockito.argThat(s -> {
                    System.out.println(s);
                    return s.contains("<#list entity.workflow_001.stateCapitals?keys as iB>");
                }),
                Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull());
    }

    /*@Test
    public void testForAddNewEntryToViewMapWithNullArugment() {
        V4Event e = new V4Event()
                .payload(new V4Payload()
                        .messageSent(new V4MessageSent()
                                .message(new V4Message()
                                        .message("<div data-format=\"PresentationML\"><p>/addstatecapital</p></div>")
                                        .user(new V4User().email("rob@example.com"))
                                        .stream(new V4Stream()
                                                .streamId("abc123")
                                                .streamType("ROOM")
                                        ))));

        presentationMLHandler.accept(e);
        Mockito.verify(messagesApi).v4StreamSidMessageCreatePost(
                Mockito.isNull(), Mockito.isNull(),
                Mockito.argThat(s ->
                        s.contains("<messageML> - State or Capital should not ber null</messageML>")),
                Mockito.any(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull());
    }*/
}
