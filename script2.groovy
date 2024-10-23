import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlSlurper
import groovy.xml.XmlUtil

def Message processData(Message message) {
    // Retrieve the current message body as a string (XML format)
    def body = message.getBody(java.lang.String) as String
    
    // Parse the XML body
    def xml = new XmlSlurper().parseText(body)
    
    // Check if the root node is <Root>, and extract its child nodes (like <Row>)
    if (xml.name() == 'Root') {
        // Extract all child nodes of <Root> (e.g., <Row> elements)
        def childNodes = xml.children()
        
        // Serialize the child nodes back to XML (without <Root>)
        def result = ""
        childNodes.each { node ->
            result += XmlUtil.serialize(node)
        }
        
        // Set the new message body without the <Root> tag
        message.setBody(result)
    }

    return message
}
