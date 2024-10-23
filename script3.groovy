import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.util.XmlParser

def Message processData(Message message) {
    // Retrieve the current message body as XML string
    def body = message.getBody(java.lang.String) as String
    
    // Parse the XML body using XmlParser
    def xmlParser = new XmlParser()
    def rootNode = xmlParser.parseText(body)
    
    // Check if the root node is <Root>
    if (rootNode.name() == 'Root') {
        // Get the child nodes of <Root> (e.g., <Row> elements)
        def childNodes = rootNode.children()
        
        // Convert the child nodes to XML strings
        def result = ""
        childNodes.each { node ->
            result += XmlUtil.serialize(node)
        }
        
        // Set the new message body to contain only the child nodes
        message.setBody(result)
    } else {
        // If there's no <Root>, keep the original body
        message.setBody(body)
    }

    return message
}
