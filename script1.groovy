import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    // Retrieve the current message body, which is expected to be in XML format
    def body = message.getBody(java.lang.String) as String

    // Check if the payload already contains a root element to avoid double-wrapping
    if (!body.contains("<Root>")) {
        // Add the <Root> element around the current body
        def wrappedBody = "<Root>\n" + body + "\n</Root>"

        // Set the modified payload back to the message
        message.setBody(wrappedBody)
    }

    return message
}
