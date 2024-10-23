import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.MarkupBuilder

def Message processData(Message message) {
    // Get the message body as a string
    def body = message.getBody(java.lang.String) as String

    // Split the CSV data by lines
    def lines = body.split('\n')

    // Split the first line to determine the dynamic fields
    def headerFields = lines[0].split(',')
    def items = lines[1..-2]  // Everything except the first and last lines
    def trailerFields = lines[-1].split(',')

    // Build the XML dynamically
    def writer = new StringWriter()
    def xmlBuilder = new MarkupBuilder(writer)

    xmlBuilder.Root {
        // Dynamically add header fields
        Header {
            headerFields[1..-1].eachWithIndex { field, index ->
                // Assuming the second line contains the values for these fields
                "${field}"(lines[0].split(',')[index + 1])
            }
        }

        // Dynamically add item data for each line
        items.each { line ->
            def itemData = line.split(',')
            Item {
                itemData[1..-1].eachWithIndex { fieldValue, index ->
                    "${headerFields[index + 1]}"(fieldValue)
                }
            }
        }

        // Dynamically add trailer fields
        Trailer {
            trailerFields[1..-1].eachWithIndex { field, index ->
                "${field}"(trailerFields[index + 1])
            }
        }
    }

    // Set the built XML as the message body
    message.setBody(writer.toString())
    return message
}
